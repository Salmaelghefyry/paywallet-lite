package com.paylogic.paywalletlite.controller.wallet;

import com.paylogic.paywalletlite.dto.request.CreateWalletRequestDto;
import com.paylogic.paywalletlite.dto.response.WalletResponseDto;
import com.paylogic.paywalletlite.security.AuthenticationContext;
import com.paylogic.paywalletlite.service.wallet.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService walletService;

    @Autowired
    public WalletController(WalletService walletService) {
        this.walletService = walletService;
    }

    @PostMapping("/request")
    public ResponseEntity<WalletResponseDto> requestWalletCreation(
            @Valid @RequestBody CreateWalletRequestDto request) {

        UUID currentUserId = AuthenticationContext.getCurrentUserId();
        WalletResponseDto response = walletService.requestWalletCreation(currentUserId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<WalletResponseDto> getWalletById(@PathVariable("id") UUID walletId) {
        WalletResponseDto response = walletService.getWalletById(walletId);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<WalletResponseDto>> getWalletsByUser(
            @PathVariable("userId") UUID userId) {
        List<WalletResponseDto> wallets = walletService.getWalletsByUserId(userId);
        return ResponseEntity.ok(wallets);
    }
}