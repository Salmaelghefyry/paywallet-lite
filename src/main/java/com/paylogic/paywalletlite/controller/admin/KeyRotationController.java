package com.paylogic.paywalletlite.controller.admin;

import com.paylogic.paywalletlite.domain.crypto.ServerKey;
import com.paylogic.paywalletlite.domain.crypto.enums.ServerKeyPurpose;
import com.paylogic.paywalletlite.dto.response.ApiErrorResponseDto;
import com.paylogic.paywalletlite.exception.BusinessException;
import com.paylogic.paywalletlite.mapper.ServerKeyMapper;
import com.paylogic.paywalletlite.security.AuthenticationFacade;
import com.paylogic.paywalletlite.service.security.CryptographicService;
import com.paylogic.paywalletlite.service.security.KeyRotationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/key-rotation")
public class KeyRotationController {

    private final KeyRotationService keyRotationService;
    private final CryptographicService cryptographicService;
    private final AuthenticationFacade authenticationFacade;
    private final ServerKeyMapper serverKeyMapper;


    @Autowired
    public KeyRotationController(KeyRotationService keyRotationService,
                                 CryptographicService cryptographicService,
                                 AuthenticationFacade authenticationFacade,
                                 ServerKeyMapper serverKeyMapper) {
        this.keyRotationService = keyRotationService;
        this.cryptographicService = cryptographicService;
        this.authenticationFacade = authenticationFacade;
        this.serverKeyMapper = serverKeyMapper;
    }

    /**
     * POST /v1/admin/crypto/keys/{keyId}/rotate
     *
     * Effectue une rotation de clé : génère une nouvelle clé et marque l'ancienne comme PENDING_ROTATION.
     *
     * @param keyId ID de la clé à faire tourner
     * @param purpose usage de la clé à faire tourner
     * @return Nouvelle ServerKeyResponseDto
     */
    @PostMapping("/keys/{keyId}/purpose/{purpose}/rotate")
    public ResponseEntity<?> rotateKey(@PathVariable UUID keyId,
                                       @PathVariable ServerKeyPurpose purpose) {
        assertAdminAccess();
        try {
            ServerKey oldKey = cryptographicService.findById(keyId);
            ServerKey newKey = keyRotationService.rotateKey(keyId, purpose);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(serverKeyMapper.toResponseDto(newKey));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponseDto("ROTATION_ERROR", e.getMessage(), null));
        }
    }

    /**
     * GET /api/v1/admin/key-rotation/pending
     * Liste les clés nécessitant une rotation.
     */
    @GetMapping("/pending")
    public ResponseEntity<List<ServerKey>> getPendingRotations() {
        return ResponseEntity.ok(keyRotationService.findKeysNeedingRotation());
    }

    /**
     * POST /api/v1/admin/key-rotation/check
     * Déclenche la vérification et rotation automatique.
     */
    @PostMapping("/check")
    public ResponseEntity<?> checkAndRotate() {
        keyRotationService.checkAndRotateExpiringKeys();
        return ResponseEntity.ok(new ApiErrorResponseDto("SUCCESS", "Rotation check completed", null));
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