package com.paylogic.paywalletlite.controller.wallet;

import com.paylogic.paywalletlite.domain.wallet.Wallet;
import com.paylogic.paywalletlite.domain.wallet.WalletConfig;
import com.paylogic.paywalletlite.dto.request.CreateWalletRequestDto;
import com.paylogic.paywalletlite.dto.response.WalletConfigResponseDto;
import com.paylogic.paywalletlite.dto.response.WalletResponseDto;
import com.paylogic.paywalletlite.exception.BusinessException;
import com.paylogic.paywalletlite.security.AuthenticationContext;
import com.paylogic.paywalletlite.service.wallet.WalletConfigService;
import com.paylogic.paywalletlite.service.wallet.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {

    private final WalletService walletService;

    private final WalletConfigService walletConfigService;

    @Autowired
    public WalletController(WalletService walletService, WalletConfigService walletConfigService) {
        this.walletService = walletService;
        this.walletConfigService = walletConfigService;
    }

    @PostMapping("/request")
    public ResponseEntity<WalletResponseDto> requestWalletCreation(
            @Valid @RequestBody CreateWalletRequestDto request) {

        UUID userId = request.getUserId();
        WalletResponseDto response = walletService.requestWalletCreation(userId, request);
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

    @GetMapping("/{id}/config")
    public ResponseEntity<WalletConfigResponseDto> getWalletConfig(@PathVariable("id") UUID walletId) {
        WalletResponseDto wallet = walletService.getWalletById(walletId);
        if (wallet.getConfig() == null) {
            throw new BusinessException("No config found for wallet: " + walletId);
        }
        return ResponseEntity.ok(wallet.getConfig());
    }

}