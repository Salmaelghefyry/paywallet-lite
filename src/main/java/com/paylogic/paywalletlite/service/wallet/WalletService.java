package com.paylogic.paywalletlite.service.wallet;

import com.paylogic.paywalletlite.dto.request.CreateWalletRequestDto;
import com.paylogic.paywalletlite.dto.response.WalletResponseDto;

import java.util.List;
import java.util.UUID;

public interface WalletService {

    WalletResponseDto requestWalletCreation(UUID userId, CreateWalletRequestDto request);

    WalletResponseDto getWalletById(UUID walletId);

    List<WalletResponseDto> getWalletsByUserId(UUID userId);

    List<WalletResponseDto> getPendingWallets();

    WalletResponseDto approveWallet(UUID walletId);

    WalletResponseDto rejectWallet(UUID walletId, String reason);
}