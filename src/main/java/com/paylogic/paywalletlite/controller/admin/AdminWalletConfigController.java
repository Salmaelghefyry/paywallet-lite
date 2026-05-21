package com.paylogic.paywalletlite.controller.admin;

import com.paylogic.paywalletlite.domain.wallet.WalletConfig;
import com.paylogic.paywalletlite.dto.request.WalletConfigCreateRequestDto;
import com.paylogic.paywalletlite.dto.request.WalletConfigUpdateRequestDto;
import com.paylogic.paywalletlite.dto.response.WalletConfigResponseDto;
import com.paylogic.paywalletlite.exception.BusinessException;
import com.paylogic.paywalletlite.service.wallet.WalletConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/wallet-configs")
public class AdminWalletConfigController {

    private final WalletConfigService walletConfigService;

    @Autowired
    public AdminWalletConfigController(WalletConfigService walletConfigService) {
        this.walletConfigService = walletConfigService;
    }

    @PostMapping
    public ResponseEntity<WalletConfigResponseDto> createConfig(
            @Valid @RequestBody WalletConfigCreateRequestDto dto) {
        WalletConfig config = walletConfigService.createConfig(dto);
        return ResponseEntity.status(HttpStatus.CREATED).body(toDto(config));
    }

    @GetMapping("/{configId}")
    public ResponseEntity<WalletConfigResponseDto> getConfig(@PathVariable UUID configId) {
        WalletConfig config = walletConfigService.getConfigById(configId)
                .orElseThrow(() -> new BusinessException("Config not found: " + configId));
        return ResponseEntity.ok(toDto(config));
    }

    @PutMapping("/{configId}")
    public ResponseEntity<WalletConfigResponseDto> updateConfig(
            @PathVariable UUID configId,
            @Valid @RequestBody WalletConfigUpdateRequestDto dto) {
        WalletConfig updated = walletConfigService.updateConfig(configId, dto);
        return ResponseEntity.ok(toDto(updated));
    }

    @PatchMapping("/{configId}/deprecate")
    public ResponseEntity<Void> deprecateConfig(@PathVariable UUID configId) {
        walletConfigService.deprecateConfig(configId);
        return ResponseEntity.noContent().build();
    }

    private WalletConfigResponseDto toDto(WalletConfig config) {
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