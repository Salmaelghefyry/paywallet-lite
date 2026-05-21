package com.paylogic.paywalletlite.service.wallet;

import com.paylogic.paywalletlite.domain.identity.User;
import com.paylogic.paywalletlite.domain.wallet.Wallet;
import com.paylogic.paywalletlite.domain.wallet.WalletConfig;
import com.paylogic.paywalletlite.domain.wallet.enums.WalletStatus;
import com.paylogic.paywalletlite.dto.request.CreateWalletRequestDto;
import com.paylogic.paywalletlite.dto.request.WalletConfigCreateRequestDto;
import com.paylogic.paywalletlite.dto.response.WalletConfigResponseDto;
import com.paylogic.paywalletlite.dto.response.WalletResponseDto;
import com.paylogic.paywalletlite.exception.BusinessException;
import com.paylogic.paywalletlite.repository.wallet.WalletRepository;
import com.paylogic.paywalletlite.service.audit.AuditService;
import com.paylogic.paywalletlite.service.security.CryptographicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.math.BigDecimal;
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

    private final WalletConfigService walletConfigService;

    @Autowired
    public WalletServiceImpl(WalletRepository walletRepository,
                             CryptographicService cryptographicService,
                             AuditService auditService,
                             WalletConfigService walletConfigService) {
        this.walletRepository = walletRepository;
        this.cryptographicService = cryptographicService;
        this.auditService = auditService;
        this.walletConfigService = walletConfigService;
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

        // Get or create config
        WalletConfig config;
        if (request.getConfigId() != null) {
            config = walletConfigService.getConfigById(request.getConfigId())
                    .orElseThrow(() -> new BusinessException("Config not found: " + request.getConfigId()));
        } else {
            // Create default config for this wallet
            WalletConfigCreateRequestDto configDto = new WalletConfigCreateRequestDto();
            configDto.setWalletType(request.getWalletType());
            config = walletConfigService.createConfig(configDto);
        }

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

    // --- Methodes privees ---

    private WalletConfigResponseDto buildConfigDto(WalletConfig config) {
        if (config == null) return null;
        WalletConfigResponseDto dto = new WalletConfigResponseDto();
        dto.setConfigId(config.getConfigId());
        dto.setWalletType(config.getWalletType());
        dto.setDailySpendingLimit(config.getDailySpendingLimit());
        dto.setMonthlySpendingLimit(config.getMonthlySpendingLimit());
        dto.setMaxSingleTransaction(config.getMaxSingleTransaction());
        dto.setMaxOfflineBalance(config.getMaxOfflineBalance());
        dto.setKeyRotationPeriodDays(config.getKeyRotationPeriodDays());
        dto.setRequiresBiometricForOffline(config.getRequiresBiometricForOffline());
        dto.setPinMaxAttempts(config.getPinMaxAttempts());
        dto.setOfflineTransactionTimeoutMinutes(config.getOfflineTransactionTimeoutMinutes());
        dto.setAllowTokenTransfer(config.getAllowTokenTransfer());
        dto.setAllowMerchantPayment(config.getAllowMerchantPayment());
        dto.setStatus(config.getStatus());
        return dto;
    }

    private WalletResponseDto mapToResponseDto(Wallet wallet) {
        WalletResponseDto dto = new WalletResponseDto();
        dto.setWalletId(wallet.getWalletId());
        dto.setUserId(wallet.getUserId());
        dto.setStatus(wallet.getStatus());
        dto.setOnlineBalance(wallet.getOnlineBalance());
        dto.setOfflineBalance(wallet.getOfflineBalance());
        dto.setPendingBalance(wallet.getPendingBalance());
        dto.setCreatedAt(wallet.getCreatedAt());
        dto.setPublicKey(wallet.getPublicKey());
        dto.setRejectionReason(wallet.getRejectionReason());

        WalletConfig config = walletConfigService.getConfigById(wallet.getWalletConfigId()).orElse(null);
        if (config != null) {
            dto.setWalletType(config.getWalletType());
            dto.setConfig(mapConfigToDto(config));
        }

        return dto;
    }
    private WalletConfigResponseDto mapConfigToDto(WalletConfig config) {
        if (config == null) return null;
        WalletConfigResponseDto dto = new WalletConfigResponseDto();
        dto.setConfigId(config.getConfigId());
        dto.setWalletType(config.getWalletType());
        dto.setDailySpendingLimit(config.getDailySpendingLimit());
        dto.setMonthlySpendingLimit(config.getMonthlySpendingLimit());
        dto.setMaxSingleTransaction(config.getMaxSingleTransaction());
        dto.setMaxOfflineBalance(config.getMaxOfflineBalance());
        dto.setKeyRotationPeriodDays(config.getKeyRotationPeriodDays());
        dto.setRequiresBiometricForOffline(config.getRequiresBiometricForOffline());
        dto.setPinMaxAttempts(config.getPinMaxAttempts());
        dto.setOfflineTransactionTimeoutMinutes(config.getOfflineTransactionTimeoutMinutes());
        dto.setAllowTokenTransfer(config.getAllowTokenTransfer());
        dto.setAllowMerchantPayment(config.getAllowMerchantPayment());
        dto.setStatus(config.getStatus());
        return dto;
    }

}