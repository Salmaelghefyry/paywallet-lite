package com.paylogic.paywalletlite.repository.wallet;

import com.paylogic.paywalletlite.domain.wallet.WalletConfig;

import java.util.Optional;
import java.util.UUID;

public interface WalletConfigRepository {
    WalletConfig save(WalletConfig config);
    Optional<WalletConfig> findById(UUID configId);
}