package com.paylogic.paywalletlite.controller.admin;

import com.paylogic.paywalletlite.domain.token.TokenAllocationConfig;
import com.paylogic.paywalletlite.domain.token.TokenDenomination;
import com.paylogic.paywalletlite.domain.wallet.enums.WalletType;
import com.paylogic.paywalletlite.dto.request.TokenAllocationConfigRequestDto;
import com.paylogic.paywalletlite.dto.request.TokenDenominationRequestDto;
import com.paylogic.paywalletlite.dto.response.ApiErrorResponseDto;
import com.paylogic.paywalletlite.dto.response.TokenAllocationConfigResponseDto;
import com.paylogic.paywalletlite.dto.response.TokenDenominationResponseDto;
import com.paylogic.paywalletlite.exception.BusinessException;
import com.paylogic.paywalletlite.mapper.TokenAllocationConfigMapper;
import com.paylogic.paywalletlite.mapper.TokenDenominationMapper;
import com.paylogic.paywalletlite.security.AuthenticationFacade;
import com.paylogic.paywalletlite.service.token.TokenAllocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Contrôleur REST pour l'administration des configurations d'allocation de tokens.
 *
 * Tous les endpoints nécessitent le rôle ADMIN.
 * Permet la création, modification, activation et dépréciation des configurations.
 *
 * Route de base : /v1/admin/token-configs
 */
@RestController
@RequestMapping("/v1/admin/token-configs")
@PreAuthorize("hasRole('ADMIN')")
public class AdminTokenAllocationController {

    private final TokenAllocationService configService;
    private final AuthenticationFacade authenticationFacade;
    private final TokenAllocationConfigMapper configMapper;
    private final TokenDenominationMapper denominationMapper;

    @Autowired
    public AdminTokenAllocationController(TokenAllocationService configService,
                                          AuthenticationFacade authenticationFacade,
                                          TokenAllocationConfigMapper configMapper,
                                          TokenDenominationMapper denominationMapper) {
        this.configService = configService;
        this.authenticationFacade = authenticationFacade;
        this.configMapper = configMapper;
        this.denominationMapper = denominationMapper;
    }

    // ============================================================
    // GESTION DES CONFIGURATIONS (ADMIN)
    // ============================================================


    /**
     * GET /v1/admin/token-configs
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
     * GET /v1/admin/token-configs/wallet-type/{walletType}
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
     * GET /v1/admin/token-configs/{configId}/denominations
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


    /**
     * POST /v1/admin/token-configs
     *
     * Crée une nouvelle configuration d'allocation.
     *
     * @param request   DTO de création
     * @param createdBy Créateur (défaut SYSTEM)
     * @return TokenAllocationConfigResponseDto
     */
    @PostMapping
    public ResponseEntity<?> createConfig(
            @Valid @RequestBody TokenAllocationConfigRequestDto request,
            @RequestParam(defaultValue = "SYSTEM") String createdBy) {
        assertAdminAccess();
        try {
            TokenAllocationConfig config = configService.createConfig(request, createdBy);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(configMapper.toResponseDto(config));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponseDto("CONFIG_ERROR", e.getMessage(), null));
        }
    }

    /**
     * PUT /v1/admin/token-configs/{configId}
     *
     * Met à jour une configuration existante.
     *
     * @param configId ID de la configuration
     * @param request  DTO de mise à jour
     * @return TokenAllocationConfigResponseDto
     */
    @PutMapping("/{configId}")
    public ResponseEntity<?> updateConfig(
            @PathVariable UUID configId,
            @Valid @RequestBody TokenAllocationConfigRequestDto request) {
        assertAdminAccess();
        try {
            TokenAllocationConfig config = configService.updateConfig(configId, request);
            return ResponseEntity.ok(configMapper.toResponseDto(config));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponseDto("CONFIG_ERROR", e.getMessage(), null));
        }
    }

    /**
     * POST /v1/admin/token-configs/{configId}/activate
     *
     * Active une configuration.
     *
     * @param configId ID de la configuration
     * @return Confirmation
     */
    @PostMapping("/{configId}/activate")
    public ResponseEntity<?> activateConfig(@PathVariable UUID configId) {
        assertAdminAccess();
        try {
            configService.activateConfig(configId);
            return ResponseEntity.ok(new ApiErrorResponseDto("ACTIVATED", "Configuration activated", null));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponseDto("ERROR", e.getMessage(), null));
        }
    }

    /**
     * POST /v1/admin/token-configs/{configId}/deprecate
     *
     * Déprécie une configuration.
     *
     * @param configId ID de la configuration
     * @return Confirmation
     */
    @PostMapping("/{configId}/deprecate")
    public ResponseEntity<?> deprecateConfig(@PathVariable UUID configId) {
        assertAdminAccess();
        try {
            configService.deprecateConfig(configId);
            return ResponseEntity.ok(new ApiErrorResponseDto("DEPRECATED", "Configuration deprecated", null));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponseDto("ERROR", e.getMessage(), null));
        }
    }

    // ============================================================
    // GESTION DES DÉNOMINATIONS (ADMIN)
    // ============================================================

    /**
     * GET /v1/admin/token-configs/denominations
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
     * POST /v1/admin/token-configs/denominations
     *
     * Crée une nouvelle dénomination.
     *
     * @param request DTO de création
     * @return TokenDenominationResponseDto
     */
    @PostMapping("/denominations")
    public ResponseEntity<?> createDenomination(
            @Valid @RequestBody TokenDenominationRequestDto request) {
        assertAdminAccess();
        try {
            TokenDenomination denomination = configService.createDenomination(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(denominationMapper.toResponseDto(denomination));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponseDto("DENOMINATION_ERROR", e.getMessage(), null));
        }
    }

    /**
     * PUT /v1/admin/token-configs/denominations/{denominationId}
     *
     * Met à jour une dénomination.
     *
     * @param denominationId ID de la dénomination
     * @param request        DTO de mise à jour
     * @return TokenDenominationResponseDto
     */
    @PutMapping("/denominations/{denominationId}")
    public ResponseEntity<?> updateDenomination(
            @PathVariable UUID denominationId,
            @Valid @RequestBody TokenDenominationRequestDto request) {
        assertAdminAccess();
        try {
            TokenDenomination denomination = configService.updateDenomination(denominationId, request);
            return ResponseEntity.ok(denominationMapper.toResponseDto(denomination));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponseDto("DENOMINATION_ERROR", e.getMessage(), null));
        }
    }

    /**
     * POST /v1/admin/token-configs/denominations/{denominationId}/activate
     *
     * Active une dénomination.
     *
     * @param denominationId ID de la dénomination
     * @return Confirmation
     */
    @PostMapping("/denominations/{denominationId}/activate")
    public ResponseEntity<?> activateDenomination(@PathVariable UUID denominationId) {
        assertAdminAccess();
        try {
            configService.activateDenomination(denominationId);
            return ResponseEntity.ok(new ApiErrorResponseDto("ACTIVATED", "Denomination activated", null));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponseDto("ERROR", e.getMessage(), null));
        }
    }

    /**
     * POST /v1/admin/token-configs/denominations/{denominationId}/deactivate
     *
     * Désactive une dénomination.
     *
     * @param denominationId ID de la dénomination
     * @return Confirmation
     */
    @PostMapping("/denominations/{denominationId}/deactivate")
    public ResponseEntity<?> deactivateDenomination(@PathVariable UUID denominationId) {
        assertAdminAccess();
        try {
            configService.deactivateDenomination(denominationId);
            return ResponseEntity.ok(new ApiErrorResponseDto("DEACTIVATED", "Denomination deactivated", null));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponseDto("ERROR", e.getMessage(), null));
        }
    }

    // ============================================================
    // MÉTHODE PRIVÉE
    // ============================================================

    private void assertAdminAccess() {
        if (!authenticationFacade.isCurrentUserAdmin()) {
            throw new BusinessException("Access denied: administrative privileges required");
        }
    }
}