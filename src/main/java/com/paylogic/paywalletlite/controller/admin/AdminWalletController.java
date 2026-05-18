package com.paylogic.paywalletlite.controller.admin;

import com.paylogic.paywalletlite.dto.response.WalletResponseDto;
import com.paylogic.paywalletlite.service.wallet.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/wallets")
public class AdminWalletController {

    private final WalletService walletService;

    @Autowired
    public AdminWalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @GetMapping("/pending")
    public ResponseEntity<List<WalletResponseDto>> getPendingWallets() {
        List<WalletResponseDto> pendingWallets = walletService.getPendingWallets();
        return ResponseEntity.ok(pendingWallets);
    }

    @PostMapping("/{walletId}/approve")
    public ResponseEntity<WalletResponseDto> approveWallet(
            @PathVariable UUID walletId) {
        WalletResponseDto response = walletService.approveWallet(walletId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{walletId}/reject")
    public ResponseEntity<WalletResponseDto> rejectWallet(
            @PathVariable UUID walletId,
            @RequestParam String reason) {
        WalletResponseDto response = walletService.rejectWallet(walletId, reason);
        return ResponseEntity.ok(response);
    }
}