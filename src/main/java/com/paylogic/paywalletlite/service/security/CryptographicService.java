package com.paylogic.paywalletlite.service.security;

import com.paylogic.paywalletlite.domain.wallet.Wallet;
import org.springframework.stereotype.Service;

@Service
public class CryptographicService {
    public String generateWalletKeyPair(Wallet wallet) {
        // Return mock key for now
        return "mock-public-key-" + wallet.getWalletId();
    }
}