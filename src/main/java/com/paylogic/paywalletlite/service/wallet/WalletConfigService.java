package com.paylogic.paywalletlite.service.wallet;

import com.paylogic.paywalletlite.domain.wallet.WalletConfig;
import com.paylogic.paywalletlite.dto.request.WalletConfigCreateRequestDto;
import com.paylogic.paywalletlite.dto.request.WalletConfigUpdateRequestDto;

import java.util.Optional;
import java.util.UUID;

public interface WalletConfigService {
    WalletConfig createConfig(WalletConfigCreateRequestDto dto);
    WalletConfig updateConfig(UUID configId, WalletConfigUpdateRequestDto dto);
    Optional<WalletConfig> getConfigById(UUID configId);
    void deprecateConfig(UUID configId);
}