package com.paylogic.paywalletlite.controller.token;

import com.paylogic.paywalletlite.domain.token.TokenAllocationConfig;
import com.paylogic.paywalletlite.domain.wallet.enums.WalletType;
import com.paylogic.paywalletlite.dto.response.ApiErrorResponseDto;
import com.paylogic.paywalletlite.dto.response.TokenAllocationConfigResponseDto;
import com.paylogic.paywalletlite.dto.response.TokenDenominationResponseDto;
import com.paylogic.paywalletlite.exception.BusinessException;
import com.paylogic.paywalletlite.mapper.TokenAllocationConfigMapper;
import com.paylogic.paywalletlite.mapper.TokenDenominationMapper;
import com.paylogic.paywalletlite.service.token.TokenAllocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Contrôleur REST pour la consultation des configurations d'allocation de tokens.
 *
 * Accessible à tous les utilisateurs authentifiés.
 * Consultation uniquement (read-only).
 *
 * Route de base : /v1/token-configs
 */
@RestController
@RequestMapping("/v1/token-configs")
public class TokenAllocationController {

    private final TokenAllocationService configService;
    private final TokenAllocationConfigMapper configMapper;
    private final TokenDenominationMapper denominationMapper;

    @Autowired
    public TokenAllocationController(TokenAllocationService configService,
                                     TokenAllocationConfigMapper configMapper,
                                     TokenDenominationMapper denominationMapper) {
        this.configService = configService;
        this.configMapper = configMapper;
        this.denominationMapper = denominationMapper;
    }

    // ============================================================
    // CONSULTATION DES CONFIGURATIONS
    // ============================================================

    /**
     * GET /v1/token-configs
     *
     * Liste toutes les configurations actives.
     *
     * @return Liste de TokenAllocationConfigResponseDto
     */
    @GetMapping
    public ResponseEntity<List<TokenAllocationConfigResponseDto>> getAllActiveConfigs() {
        List<TokenAllocationConfig> configs = configService.findAllActiveConfigs();
        List<TokenAllocationConfigResponseDto> response = configs.stream()
                .map(configMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * GET /v1/token-configs/{configId}
     *
     * Récupère une configuration par son ID.
     *
     * @param configId ID de la configuration
     * @return TokenAllocationConfigResponseDto
     */
    @GetMapping("/{configId}")
    public ResponseEntity<?> getConfig(@PathVariable UUID configId) {
        try {
            TokenAllocationConfig config = configService.findConfigById(configId);
            return ResponseEntity.ok(configMapper.toResponseDto(config));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponseDto("NOT_FOUND", e.getMessage(), null));
        }
    }

    /**
     * GET /v1/token-configs/wallet-type/{walletType}
     *
     * Liste les configurations par type de wallet.
     *
     * @param walletType Type de wallet (GOLD, SILVER, BASIC)
     * @return Liste de TokenAllocationConfigResponseDto
     */
    @GetMapping("/wallet-type/{walletType}")
    public ResponseEntity<List<TokenAllocationConfigResponseDto>> getConfigsByWalletType(
            @PathVariable WalletType walletType) {
        List<TokenAllocationConfig> configs = configService.findConfigsByWalletType(walletType);
        List<TokenAllocationConfigResponseDto> response = configs.stream()
                .map(configMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * GET /v1/token-configs/{configId}/denominations
     *
     * Récupère les dénominations d'une configuration.
     *
     * @param configId ID de la configuration
     * @return Liste des valeurs de dénominations
     */
    @GetMapping("/{configId}/denominations")
    public ResponseEntity<?> getConfigDenominations(@PathVariable UUID configId) {
        try {
            List<BigDecimal> values = configService.getSortedDenominationValues(configId);
            return ResponseEntity.ok(values);
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponseDto("NOT_FOUND", e.getMessage(), null));
        }
    }

    // ============================================================
    // CONSULTATION DES DÉNOMINATIONS
    // ============================================================

    /**
     * GET /v1/token-configs/denominations
     *
     * Liste toutes les dénominations actives.
     *
     * @return Liste de TokenDenominationResponseDto
     */
    @GetMapping("/denominations")
    public ResponseEntity<List<TokenDenominationResponseDto>> getAllActiveDenominations() {
        List<com.paylogic.paywalletlite.domain.token.TokenDenomination> denominations = configService.findAllActiveDenominations();
        List<TokenDenominationResponseDto> response = denominations.stream()
                .map(denominationMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * GET /v1/token-configs/denominations/currency/{currencyCode}
     *
     * Liste les dénominations par code devise.
     *
     * @param currencyCode Code de la devise (ex: MAD, XAF)
     * @return Liste de TokenDenominationResponseDto
     */
    @GetMapping("/denominations/currency/{currencyCode}")
    public ResponseEntity<List<TokenDenominationResponseDto>> getDenominationsByCurrency(
            @PathVariable String currencyCode) {
        List<com.paylogic.paywalletlite.domain.token.TokenDenomination> denominations = configService.findDenominationsByCurrencyCode(currencyCode);
        List<TokenDenominationResponseDto> response = denominations.stream()
                .map(denominationMapper::toResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
}