package com.paylogic.paywalletlite.dto.request;

import com.paylogic.paywalletlite.domain.wallet.enums.WalletType;

import jakarta.validation.constraints.NotNull;
import java.io.Serializable;

public class CreateWalletRequestDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @NotNull(message = "Wallet type is required")
    private WalletType walletType;

    // Optional: custom config override (null = use defaults)
    private String configName;

    public WalletType getWalletType() {
        return walletType;
    }

    public void setWalletType(WalletType walletType) {
        this.walletType = walletType;
    }

    public String getConfigName() {
        return configName;
    }

    public void setConfigName(String configName) {
        this.configName = configName;
    }
}