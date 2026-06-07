package com.paylogic.paywalletlite.service.transaction;

import com.paylogic.paywalletlite.domain.notification.enums.AuditEventType;
import com.paylogic.paywalletlite.domain.transaction.Transaction;
import com.paylogic.paywalletlite.domain.transaction.enums.OverpaymentStatus;
import com.paylogic.paywalletlite.domain.transaction.enums.TransactionStatus;
import com.paylogic.paywalletlite.domain.transaction.enums.TransactionType;
import com.paylogic.paywalletlite.dto.request.TransactionCreateRequestDto;
import com.paylogic.paywalletlite.dto.response.TransactionResponseDto;
import com.paylogic.paywalletlite.exception.BusinessException;
import com.paylogic.paywalletlite.mapper.TransactionMapper;
import com.paylogic.paywalletlite.repository.transaction.TransactionRepository;
import com.paylogic.paywalletlite.service.audit.AuditService;
import com.paylogic.paywalletlite.service.wallet.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Implémentation du service Transaction.
 * Gère le cycle de vie complet des transactions selon le modèle 3 phases.
 */
@Service
@Transactional
public class TransactionServiceImpl implements TransactionService {

    private static final Logger logger = LoggerFactory.getLogger(TransactionServiceImpl.class);

    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final WalletService walletService;
    private final OverpaymentHandler overpaymentHandler;
    private final AuditService auditService;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository,
                                  TransactionMapper transactionMapper,
                                  WalletService walletService,
                                  AuditService auditService,
                                  OverpaymentHandler overpaymentHandler) {
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
        this.walletService = walletService;
        this.auditService = auditService;
        this.overpaymentHandler = overpaymentHandler;
    }

    @Override
    public TransactionResponseDto createTransaction(TransactionCreateRequestDto request) {
        logger.info("Création d'une transaction: sender={}, receiver={}, amount={}, type={}, offline={}",
                request.getSenderWalletId(), request.getReceiverWalletId(),
                request.getRequestedAmount(), request.getType(), request.getIsOffline());

        // Validation: vérifier l'existence des wallets
        validateWalletsExist(request.getSenderWalletId(), request.getReceiverWalletId());

        // Validation: vérifier les fonds suffisants pour les transactions online
        if (!Boolean.TRUE.equals(request.getIsOffline())) {
            validateSufficientFunds(request.getSenderWalletId(), request.getRequestedAmount());
        }

        Transaction transaction = transactionMapper.toEntity(request);

        // Calcul du hash de transaction pour traçabilité
        String txHash = computeTransactionHash(transaction);
        transaction.setTransactionHash(txHash);

        Transaction saved = transactionRepository.save(transaction);

        logger.info("Transaction créée avec succès: id={}, hash={}", saved.getTransactionId(), txHash);

        return transactionMapper.toResponseDto(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public TransactionResponseDto getTransactionById(UUID transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new BusinessException("Transaction non trouvée: " + transactionId));
        return transactionMapper.toResponseDto(transaction);
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransactionsByWalletId(UUID walletId) {
        List<Transaction> sent = transactionRepository.findBySenderWalletId(walletId);
        List<Transaction> received = transactionRepository.findByReceiverWalletId(walletId);

        sent.addAll(received);
        return sent.stream()
                .distinct()
                .map(transactionMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransactionsByStatus(TransactionStatus status) {
        return transactionRepository.findByStatus(status).stream()
                .map(transactionMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public void assignToSyncBatch(UUID transactionId, UUID syncBatchId) {
        logger.info("Association de la transaction {} au lot de sync {}", transactionId, syncBatchId);
        transactionRepository.updateSyncBatchId(transactionId, syncBatchId);
    }

    @Override
    public BigDecimal calculateOverpayment(UUID transactionId) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new BusinessException("Transaction non trouvée"));

        if (transaction.getTransferredAmount() == null || transaction.getRequestedAmount() == null) {
            return BigDecimal.ZERO;
        }

        BigDecimal diff = transaction.getTransferredAmount().subtract(transaction.getRequestedAmount());
        return diff.compareTo(BigDecimal.ZERO) > 0 ? diff : BigDecimal.ZERO;
    }

    // ============ Méthodes privées ============

    private void validateWalletsExist(UUID senderId, UUID receiverId) {
        if (!walletService.existsById(senderId)) {
            throw new BusinessException("Wallet expéditeur introuvable: " + senderId);
        }
        if (!walletService.existsById(receiverId)) {
            throw new BusinessException("Wallet destinataire introuvable: " + receiverId);
        }
    }

    private void validateSufficientFunds(UUID walletId, BigDecimal amount) {
        BigDecimal balance = walletService.getOnlineBalance(walletId);
        if (balance.compareTo(amount) < 0) {
            throw new BusinessException("Fonds insuffisants. Solde: " + balance + ", Requis: " + amount);
        }
    }

    private String computeTransactionHash(Transaction transaction) {
        String data = transaction.getSenderWalletId().toString() +
                transaction.getReceiverWalletId().toString() +
                transaction.getRequestedAmount().toString() +
                transaction.getInitiatedAt().toString();
        try {
            java.security.MessageDigest digest = java.security.MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(data.getBytes(java.nio.charset.StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (Exception e) {
            logger.error("Erreur lors du calcul du hash de transaction", e);
            return UUID.randomUUID().toString().replace("-", "");
        }
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransactionsByUserId(UUID userId) {
        // Récupérer tous les wallets de l'utilisateur
        List<UUID> walletIds = walletService.getWalletIdsByUserId(userId);

        if (walletIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<Transaction> allTransactions = new ArrayList<>();
        for (UUID walletId : walletIds) {
            allTransactions.addAll(transactionRepository.findBySenderWalletId(walletId));
            allTransactions.addAll(transactionRepository.findByReceiverWalletId(walletId));
        }

        // Dédupliquer
        Set<Transaction> uniqueTransactions = new LinkedHashSet<>(allTransactions);

        return uniqueTransactions.stream()
                .sorted(Comparator.comparing(Transaction::getInitiatedAt).reversed())
                .map(transactionMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransactionsBySenderId(UUID senderWalletId) {
        return transactionRepository.findBySenderWalletId(senderWalletId).stream()
                .sorted(Comparator.comparing(Transaction::getInitiatedAt).reversed())
                .map(transactionMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransactionsByReceiverId(UUID receiverWalletId) {
        return transactionRepository.findByReceiverWalletId(receiverWalletId).stream()
                .sorted(Comparator.comparing(Transaction::getInitiatedAt).reversed())
                .map(transactionMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getOfflineTransactionsByUserId(UUID userId) {
        List<UUID> walletIds = walletService.getWalletIdsByUserId(userId);

        if (walletIds.isEmpty()) {
            return Collections.emptyList();
        }

        List<Transaction> allOffline = new ArrayList<>();
        for (UUID walletId : walletIds) {
            allOffline.addAll(transactionRepository.findOfflineByWalletId(walletId));
        }

        return new LinkedHashSet<>(allOffline).stream()
                .sorted(Comparator.comparing(Transaction::getInitiatedAt).reversed())
                .map(transactionMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ============================================================
    // CONSULTATION — ADMINISTRATION
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getAllTransactions() {
        return transactionRepository.findAll().stream()
                .sorted(Comparator.comparing(Transaction::getInitiatedAt).reversed())
                .map(transactionMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransactionsByType(TransactionType type) {
        return transactionRepository.findByType(type).stream()
                .sorted(Comparator.comparing(Transaction::getInitiatedAt).reversed())
                .map(transactionMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<TransactionResponseDto> getTransactionsByDateRange(
            LocalDateTime fromDate, LocalDateTime toDate) {
        return transactionRepository.findByInitiatedAtBetween(fromDate, toDate).stream()
                .sorted(Comparator.comparing(Transaction::getInitiatedAt).reversed())
                .map(transactionMapper::toResponseDto)
                .collect(Collectors.toList());
    }

    // ============================================================
    // GESTION DU CYCLE DE VIE
    // ============================================================

    @Override
    public TransactionResponseDto updateTransactionStatus(UUID transactionId, TransactionStatus newStatus) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new BusinessException("Transaction not found: " + transactionId));

        TransactionStatus oldStatus = transaction.getStatus();

        logger.info("Mise à jour statut transaction {}: {} → {}",
                transactionId, oldStatus, newStatus);

        transactionRepository.updateStatus(transactionId, newStatus);

        // Recharger
        Transaction updated = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new BusinessException("Transaction not found after update"));

        // Audit
        auditService.logEvent(AuditEventType.TRANSACTION_COMPLETED,
                updated.getSenderWalletId(), "WALLET",
                transactionId, "TRANSACTION",
                "Status changed: " + oldStatus + " → " + newStatus);

        return transactionMapper.toResponseDto(updated);
    }

    @Override
    public TransactionResponseDto completeTransaction(UUID transactionId) {
        logger.info("Finalisation transaction: {}", transactionId);

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new BusinessException("Transaction not found: " + transactionId));

        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setCompletedAt(LocalDateTime.now());

        // Gestion du surpaiement
        if (transaction.getOverpaymentAmount() != null
                && transaction.getOverpaymentAmount().compareTo(BigDecimal.ZERO) > 0) {
            overpaymentHandler.detectOverpayment(transaction, transaction.getTransferredAmount());
        }

        Transaction saved = transactionRepository.save(transaction);

        // Audit
        auditService.logEvent(AuditEventType.TRANSACTION_COMPLETED,
                saved.getSenderWalletId(), "WALLET",
                transactionId, "TRANSACTION",
                "Transaction completed. Amount: " + saved.getTransferredAmount());

        logger.info("Transaction finalisée: {}", transactionId);
        return transactionMapper.toResponseDto(saved);
    }

    @Override
    public TransactionResponseDto cancelTransaction(UUID transactionId) {
        logger.info("Annulation transaction: {}", transactionId);

        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new BusinessException("Transaction not found: " + transactionId));

        if (transaction.getStatus() != TransactionStatus.PENDING
                && transaction.getStatus() != TransactionStatus.OFFLINE_PENDING) {
            throw new BusinessException(
                    "Cannot cancel transaction with status: " + transaction.getStatus());
        }

        transaction.setStatus(TransactionStatus.FAILED);
        transaction.setCompletedAt(LocalDateTime.now());

        Transaction saved = transactionRepository.save(transaction);

        // Audit
        auditService.logEvent(AuditEventType.TRANSACTION_FAILED,
                saved.getSenderWalletId(), "WALLET",
                transactionId, "TRANSACTION",
                "Transaction cancelled");

        return transactionMapper.toResponseDto(saved);
    }


    // ============================================================
    // GESTION DES LITIGES (ADMIN)
    // ============================================================

    @Override
    public TransactionResponseDto disputeTransaction(UUID transactionId, String reason) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new BusinessException("Transaction not found: " + transactionId));

        if (transaction.getStatus() == TransactionStatus.DISPUTED) {
            throw new BusinessException("Transaction is already disputed");
        }

        transaction.setStatus(TransactionStatus.DISPUTED);
        Transaction saved = transactionRepository.save(transaction);

        // Audit
        Map<String, Object> details = new HashMap<>();
        details.put("reason", reason);
        auditService.logEvent(AuditEventType.TRANSACTION_FAILED,
                null, "ADMIN",
                transactionId, "TRANSACTION",
                "Transaction disputed: " + reason,
                details);

        logger.info("Transaction contestée: {} — {}", transactionId, reason);
        return transactionMapper.toResponseDto(saved);
    }

    @Override
    public TransactionResponseDto resolveDispute(UUID transactionId, String resolution) {
        Transaction transaction = transactionRepository.findById(transactionId)
                .orElseThrow(() -> new BusinessException("Transaction not found: " + transactionId));

        if (transaction.getStatus() != TransactionStatus.DISPUTED) {
            throw new BusinessException("Transaction is not disputed. Current status: "
                    + transaction.getStatus());
        }

        // Résoudre en COMPLETED (le litige est clos)
        transaction.setStatus(TransactionStatus.COMPLETED);
        transaction.setCompletedAt(LocalDateTime.now());
        Transaction saved = transactionRepository.save(transaction);

        // Audit
        Map<String, Object> details = new HashMap<>();
        details.put("resolution", resolution);
        auditService.logEvent(AuditEventType.TRANSACTION_COMPLETED,
                null, "ADMIN",
                transactionId, "TRANSACTION",
                "Dispute resolved: " + resolution,
                details);

        logger.info("Litige résolu: {} — {}", transactionId, resolution);
        return transactionMapper.toResponseDto(saved);
    }

    // ============================================================
    // ENTITÉ (USAGE INTERNE)
    // ============================================================

    @Override
    @Transactional(readOnly = true)
    public Transaction findEntityById(UUID transactionId) {
        return transactionRepository.findById(transactionId)
                .orElseThrow(() -> new BusinessException("Transaction not found: " + transactionId));
    }

}