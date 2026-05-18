package com.paylogic.paywalletlite.service.wallet;

import com.paylogic.paywalletlite.domain.identity.User;
import com.paylogic.paywalletlite.domain.wallet.Wallet;
import com.paylogic.paywalletlite.domain.wallet.WalletConfig;
import com.paylogic.paywalletlite.domain.wallet.enums.WalletConfigStatus;
import com.paylogic.paywalletlite.domain.wallet.enums.WalletStatus;
import com.paylogic.paywalletlite.domain.wallet.enums.WalletType;
import com.paylogic.paywalletlite.dto.request.CreateWalletRequestDto;
import com.paylogic.paywalletlite.dto.response.WalletResponseDto;
import com.paylogic.paywalletlite.exception.BusinessException;
import com.paylogic.paywalletlite.repository.wallet.WalletRepository;
import com.paylogic.paywalletlite.service.audit.AuditService;
import com.paylogic.paywalletlite.service.security.CryptographicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class WalletServiceImpl implements WalletService {

    private final WalletRepository walletRepository;
    private final CryptographicService cryptographicService;
    private final AuditService auditService;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    public WalletServiceImpl(WalletRepository walletRepository,
                             CryptographicService cryptographicService,
                             AuditService auditService) {
        this.walletRepository = walletRepository;
        this.cryptographicService = cryptographicService;
        this.auditService = auditService;
    }

    @Override
    @Transactional
    public WalletResponseDto requestWalletCreation(UUID userId, CreateWalletRequestDto request) {
        User user = entityManager.find(User.class, userId);
        if (user == null) {
            throw new BusinessException("User not found with id: " + userId);
        }

        long walletCount = walletRepository.countByUserId(userId);
        if (walletCount >= 5) {
            throw new BusinessException("Maximum wallet limit reached for user");
        }

        WalletConfig config = createDefaultWalletConfig(request.getWalletType());

        Wallet wallet = new Wallet();
        wallet.setUserId(userId);
        wallet.setWalletConfigId(config.getConfigId());
        wallet.setTokenAllocationConfigId(UUID.randomUUID());
        wallet.setStatus(WalletStatus.PENDING_APPROVAL);
        wallet.setOnlineBalance(BigDecimal.ZERO);
        wallet.setOfflineBalance(BigDecimal.ZERO);
        wallet.setPendingBalance(BigDecimal.ZERO);

        Wallet savedWallet = walletRepository.save(wallet);

        auditService.logEvent(
                "WALLET_CREATION_REQUESTED",
                userId,
                "USER",
                savedWallet.getWalletId(),
                "WALLET",
                "Wallet creation requested with type: " + request.getWalletType()
        );

        return mapToResponseDto(savedWallet);
    }

    @Override
    public WalletResponseDto getWalletById(UUID walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new BusinessException("Wallet not found: " + walletId));
        return mapToResponseDto(wallet);
    }

    @Override
    public List<WalletResponseDto> getWalletsByUserId(UUID userId) {
        return walletRepository.findByUserId(userId)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    public List<WalletResponseDto> getPendingWallets() {
        return walletRepository.findByStatus(WalletStatus.PENDING_APPROVAL)
                .stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public WalletResponseDto approveWallet(UUID walletId) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new BusinessException("Wallet not found: " + walletId));

        if (wallet.getStatus() != WalletStatus.PENDING_APPROVAL) {
            throw new BusinessException("Wallet is not pending approval. Current status: " + wallet.getStatus());
        }

        String publicKey = cryptographicService.generateWalletKeyPair(wallet);
        wallet.setPublicKey(publicKey);
        wallet.setStatus(WalletStatus.ACTIVE);

        Wallet updated = walletRepository.save(wallet);

        auditService.logEvent(
                "WALLET_APPROVED",
                null,
                "ADMIN",
                walletId,
                "WALLET",
                "Wallet approved and activated"
        );

        return mapToResponseDto(updated);
    }

    @Override
    @Transactional
    public WalletResponseDto rejectWallet(UUID walletId, String reason) {
        Wallet wallet = walletRepository.findById(walletId)
                .orElseThrow(() -> new BusinessException("Wallet not found: " + walletId));

        if (wallet.getStatus() != WalletStatus.PENDING_APPROVAL) {
            throw new BusinessException("Wallet is not pending approval");
        }

        wallet.setStatus(WalletStatus.REJECTED);
        wallet.setRejectionReason(reason);

        Wallet updated = walletRepository.save(wallet);

        auditService.logEvent(
                "WALLET_REJECTED",
                null,
                "ADMIN",
                walletId,
                "WALLET",
                "Wallet rejected. Reason: " + reason
        );

        return mapToResponseDto(updated);
    }

    private WalletConfig createDefaultWalletConfig(WalletType type) {
        WalletConfig config = new WalletConfig();
        config.setWalletType(type);
        config.setStatus(WalletConfigStatus.ACTIVE);
        config.setCreatedAt(LocalDateTime.now());
        config.setUpdatedAt(LocalDateTime.now());

        switch (type) {
            case GOLD:
                config.setDailySpendingLimit(new BigDecimal("10000.00"));
                config.setMonthlySpendingLimit(new BigDecimal("100000.00"));
                config.setMaxSingleTransaction(new BigDecimal("5000.00"));
                config.setMaxOfflineBalance(new BigDecimal("2000.00"));
                break;
            case SILVER:
                config.setDailySpendingLimit(new BigDecimal("5000.00"));
                config.setMonthlySpendingLimit(new BigDecimal("50000.00"));
                config.setMaxSingleTransaction(new BigDecimal("2000.00"));
                config.setMaxOfflineBalance(new BigDecimal("1000.00"));
                break;
            case BASIC:
            default:
                config.setDailySpendingLimit(new BigDecimal("1000.00"));
                config.setMonthlySpendingLimit(new BigDecimal("10000.00"));
                config.setMaxSingleTransaction(new BigDecimal("500.00"));
                config.setMaxOfflineBalance(new BigDecimal("500.00"));
                break;
        }

        config.setKeyRotationPeriodDays(90);
        config.setRequiresBiometricForOffline(true);
        config.setPinMaxAttempts(3);
        config.setOfflineTransactionTimeoutMinutes(30);
        config.setAllowTokenTransfer(true);
        config.setAllowMerchantPayment(true);

        entityManager.persist(config);
        return config;
    }

    private WalletResponseDto mapToResponseDto(Wallet wallet) {
        WalletResponseDto dto = new WalletResponseDto();
        dto.setWalletId(wallet.getWalletId());

        // UUID direct (pas de relation JPA)
        dto.setUserId(wallet.getUserId());

        // Récupérer le type depuis WalletConfig en base
        WalletConfig config = entityManager.find(WalletConfig.class, wallet.getWalletConfigId());
        dto.setWalletType(config != null ? config.getWalletType() : null);

        dto.setStatus(wallet.getStatus());
        dto.setOnlineBalance(wallet.getOnlineBalance());
        dto.setOfflineBalance(wallet.getOfflineBalance());
        dto.setPendingBalance(wallet.getPendingBalance());
        dto.setCreatedAt(wallet.getCreatedAt());
        dto.setPublicKey(wallet.getPublicKey());
        dto.setRejectionReason(wallet.getRejectionReason());
        return dto;
    }
}
