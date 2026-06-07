package com.paylogic.paywalletlite.controller.admin;

import com.paylogic.paywalletlite.domain.crypto.ServerKey;
import com.paylogic.paywalletlite.domain.crypto.enums.ServerKeyPurpose;
import com.paylogic.paywalletlite.domain.crypto.enums.ServerKeyStatus;
import com.paylogic.paywalletlite.dto.response.ApiErrorResponseDto;
import com.paylogic.paywalletlite.dto.response.ServerKeyResponseDto;
import com.paylogic.paywalletlite.exception.BusinessException;
import com.paylogic.paywalletlite.mapper.ServerKeyMapper;
import com.paylogic.paywalletlite.security.AuthenticationFacade;
import com.paylogic.paywalletlite.service.security.CryptographicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Contrôleur REST pour l'administration des clés cryptographiques serveur.
 *
 * Tous les endpoints nécessitent le rôle ADMIN.
 * Gère la génération, consultation, rotation et révocation des clés.
 *
 * Route de base : /v1/admin/crypto
 */
@RestController
@RequestMapping("/v1/admin/crypto")
@PreAuthorize("hasRole('ADMIN')")
public class CryptographicController {

    private final CryptographicService cryptographicService;
    private final ServerKeyMapper serverKeyMapper;
    private final AuthenticationFacade authenticationFacade;

    @Autowired
    public CryptographicController(CryptographicService cryptographicService,
                                   ServerKeyMapper serverKeyMapper,
                                   AuthenticationFacade authenticationFacade) {
        this.cryptographicService = cryptographicService;
        this.serverKeyMapper = serverKeyMapper;
        this.authenticationFacade = authenticationFacade;
    }

    // ============================================================
    // GÉNÉRATION DE CLÉS
    // ============================================================

    /**
     * POST /v1/admin/crypto/keys/generate
     *
     * Génère une nouvelle paire de clés pour un usage donné.
     *
     * @param purpose  Usage de la clé (TOKEN_SIGNING, API_SIGNING, JWT_SIGNING, CERTIFICATE_SIGNING)
     * @param walletId ID du wallet associé (optionnel, pour les clés par wallet)
     * @return ServerKeyResponseDto
     */
    @PostMapping("/keys/generate")
    public ResponseEntity<?> generateKey(
            @RequestParam ServerKeyPurpose purpose,
            @RequestParam(required = false) UUID walletId) {
        assertAdminAccess();
        try {
            ServerKey key = cryptographicService.generateKeyPair(purpose, walletId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(serverKeyMapper.toResponseDto(key));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponseDto("KEY_GENERATION_ERROR", e.getMessage(), null));
        }
    }

    // ============================================================
    // CONSULTATION DES CLÉS
    // ============================================================

    /**
     * GET /v1/admin/crypto/keys
     *
     * Retourne toutes les clés serveur avec filtres optionnels.
     *
     * @param purpose Filtre par usage (optionnel)
     * @param status  Filtre par statut (optionnel)
     * @return Liste de ServerKeyResponseDto
     */
    @GetMapping("/keys")
    public ResponseEntity<List<ServerKeyResponseDto>> getAllKeys(
            @RequestParam(value = "purpose", required = false) ServerKeyPurpose purpose,
            @RequestParam(value = "status", required = false) ServerKeyStatus status) {
        assertAdminAccess();

        List<ServerKey> keys;

        if (purpose != null) {
            keys = cryptographicService.findByPurpose(purpose);
        } else if (status != null) {
            keys = cryptographicService.findByStatus(status);
        } else {
            keys = cryptographicService.findAll();
        }

        // Filtrer par statut si les deux filtres sont fournis
        if (purpose != null && status != null) {
            keys = keys.stream()
                    .filter(k -> k.getStatus() == status)
                    .collect(Collectors.toList());
        }

        List<ServerKeyResponseDto> response = keys.stream()
                .map(serverKeyMapper::toResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * GET /v1/admin/crypto/keys/active
     *
     * Récupère la clé active pour un usage donné.
     *
     * @param purpose Usage de la clé
     * @return ServerKeyResponseDto
     */
    @GetMapping("/keys/active")
    public ResponseEntity<?> getActiveKey(@RequestParam ServerKeyPurpose purpose) {
        assertAdminAccess();
        try {
            ServerKey key = cryptographicService.getActiveKey(purpose);
            return ResponseEntity.ok(serverKeyMapper.toResponseDto(key));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponseDto("NOT_FOUND", e.getMessage(), null));
        }
    }

    /**
     * GET /v1/admin/crypto/keys/{keyId}
     *
     * Retourne les détails d'une clé spécifique.
     *
     * @param keyId ID de la clé
     * @return ServerKeyResponseDto
     */
    @GetMapping("/keys/{keyId}")
    public ResponseEntity<?> getKey(@PathVariable UUID keyId) {
        assertAdminAccess();
        try {
            ServerKey key = cryptographicService.findById(keyId);
            return ResponseEntity.ok(serverKeyMapper.toResponseDto(key));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponseDto("NOT_FOUND", e.getMessage(), null));
        }
    }

    /**
     * GET /v1/admin/crypto/keys/{keyId}/public
     *
     * Récupère la clé publique (pour distribution aux clients).
     * Endpoint sécurisé : ne retourne que la clé publique, jamais la privée.
     *
     * @param keyId ID de la clé
     * @return Clé publique au format PEM
     */
    @GetMapping("/keys/{keyId}/public")
    public ResponseEntity<?> getPublicKey(@PathVariable UUID keyId) {
        assertAdminAccess();
        try {
            String publicKey = cryptographicService.getPublicKey(keyId);
            return ResponseEntity.ok(new ApiErrorResponseDto("SUCCESS", publicKey, null));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponseDto("NOT_FOUND", e.getMessage(), null));
        }
    }

    /**
     * GET /v1/admin/crypto/keys/purpose/{purpose}
     *
     * Liste les clés filtrées par usage.
     *
     * @param purpose Usage des clés
     * @return Liste de ServerKeyResponseDto
     */
    @GetMapping("/keys/purpose/{purpose}")
    public ResponseEntity<List<ServerKeyResponseDto>> getKeysByPurpose(
            @PathVariable ServerKeyPurpose purpose) {
        assertAdminAccess();

        List<ServerKey> keys = cryptographicService.findByPurpose(purpose);
        List<ServerKeyResponseDto> response = keys.stream()
                .map(serverKeyMapper::toResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * GET /v1/admin/crypto/keys/status/{status}
     *
     * Liste les clés filtrées par statut.
     *
     * @param status Statut des clés
     * @return Liste de ServerKeyResponseDto
     */
    @GetMapping("/keys/status/{status}")
    public ResponseEntity<List<ServerKeyResponseDto>> getKeysByStatus(
            @PathVariable ServerKeyStatus status) {
        assertAdminAccess();

        List<ServerKey> keys = cryptographicService.findByStatus(status);
        List<ServerKeyResponseDto> response = keys.stream()
                .map(serverKeyMapper::toResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // ============================================================
    // GESTION DU CYCLE DE VIE
    // ============================================================

    /**
     * POST /v1/admin/crypto/keys/{keyId}/revoke
     *
     * Révoque une clé.
     *
     * @param keyId  ID de la clé à révoquer
     * @param reason Raison de la révocation (obligatoire)
     * @return Confirmation
     */
    @PostMapping("/keys/{keyId}/revoke")
    public ResponseEntity<?> revokeKey(
            @PathVariable UUID keyId,
            @RequestParam String reason) {
        assertAdminAccess();

        if (reason == null || reason.trim().isEmpty()) {
            throw new BusinessException("Revocation reason is required");
        }

        try {
            cryptographicService.revokeKey(keyId, reason);
            return ResponseEntity.ok(new ApiErrorResponseDto("REVOKED",
                    "Key revoked: " + reason, null));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponseDto("ERROR", e.getMessage(), null));
        }
    }

    /**
     * DELETE /v1/admin/crypto/keys/{keyId}
     *
     * Supprime une clé (uniquement si EXPIRED ou REVOKED).
     *
     * @param keyId ID de la clé à supprimer
     * @return Confirmation
     */
    @DeleteMapping("/keys/{keyId}")
    public ResponseEntity<?> deleteKey(@PathVariable UUID keyId) {
        assertAdminAccess();
        try {
            cryptographicService.deleteKey(keyId);
            return ResponseEntity.ok(new ApiErrorResponseDto("DELETED", "Key deleted successfully", null));
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