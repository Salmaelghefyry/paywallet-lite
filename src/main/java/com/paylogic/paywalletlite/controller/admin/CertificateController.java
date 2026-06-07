package com.paylogic.paywalletlite.controller.admin;

import com.paylogic.paywalletlite.domain.crypto.Certificate;
import com.paylogic.paywalletlite.domain.crypto.CertificateAuthority;
import com.paylogic.paywalletlite.dto.response.ApiErrorResponseDto;
import com.paylogic.paywalletlite.dto.response.CertificateAuthorityResponseDto;
import com.paylogic.paywalletlite.dto.response.CertificateResponseDto;
import com.paylogic.paywalletlite.exception.BusinessException;
import com.paylogic.paywalletlite.mapper.CertificateAuthorityMapper;
import com.paylogic.paywalletlite.mapper.CertificateMapper;
import com.paylogic.paywalletlite.security.AuthenticationFacade;
import com.paylogic.paywalletlite.service.security.CertificateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Contrôleur REST pour l'administration des certificats et de l'autorité de certification.
 *
 * Tous les endpoints nécessitent le rôle ADMIN.
 * Gère l'infrastructure PKI interne : CA racine, émission, validation, révocation.
 *
 * Route de base : /v1/admin/certificates
 */
@RestController
@RequestMapping("/v1/admin/certificates")
@PreAuthorize("hasRole('ADMIN')")
public class CertificateController {

    private final CertificateService certificateService;
    private final CertificateMapper certificateMapper;
    private final CertificateAuthorityMapper caMapper;
    private final AuthenticationFacade authenticationFacade;

    @Autowired
    public CertificateController(CertificateService certificateService,
                                 CertificateMapper certificateMapper,
                                 CertificateAuthorityMapper caMapper,
                                 AuthenticationFacade authenticationFacade) {
        this.certificateService = certificateService;
        this.certificateMapper = certificateMapper;
        this.caMapper = caMapper;
        this.authenticationFacade = authenticationFacade;
    }

    // ============================================================
    // AUTORITÉ DE CERTIFICATION (CA)
    // ============================================================

    /**
     * POST /v1/admin/certificates/ca/initialize
     *
     * Initialise la CA racine.
     * À exécuter une seule fois au premier démarrage du système.
     *
     * @param caName Nom de l'autorité de certification
     * @return CertificateAuthorityResponseDto
     */
    @PostMapping("/ca/initialize")
    public ResponseEntity<?> initializeRootCA(@RequestParam String caName) {
        assertAdminAccess();
        try {
            CertificateAuthority ca = certificateService.initializeRootCA(caName);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(caMapper.toResponseDto(ca));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponseDto("CA_INIT_ERROR", e.getMessage(), null));
        }
    }

    /**
     * GET /v1/admin/certificates/ca/active
     *
     * Récupère l'autorité de certification active.
     *
     * @return CertificateAuthorityResponseDto
     */
    @GetMapping("/ca/active")
    public ResponseEntity<?> getActiveCA() {
        assertAdminAccess();
        try {
            CertificateAuthority ca = certificateService.getActiveCA();
            return ResponseEntity.ok(caMapper.toResponseDto(ca));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponseDto("NOT_FOUND", e.getMessage(), null));
        }
    }

    // ============================================================
    // GESTION DES CERTIFICATS
    // ============================================================

    /**
     * POST /v1/admin/certificates/issue
     *
     * Émet un nouveau certificat pour un wallet.
     *
     * @param walletId ID du wallet destinataire
     * @param caId     ID de l'autorité de certification émettrice
     * @return CertificateResponseDto
     */
    @PostMapping("/issue")
    public ResponseEntity<?> issueCertificate(
            @RequestParam UUID walletId,
            @RequestParam UUID caId) {
        assertAdminAccess();
        try {
            Certificate cert = certificateService.issueCertificate(walletId, caId);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(certificateMapper.toDto(cert));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponseDto("ISSUE_ERROR", e.getMessage(), null));
        }
    }

    /**
     * GET /v1/admin/certificates/{certId}
     *
     * Retourne les détails d'un certificat.
     *
     * @param certId ID du certificat
     * @return CertificateResponseDto
     */
    @GetMapping("/{certId}")
    public ResponseEntity<?> getCertificate(@PathVariable UUID certId) {
        assertAdminAccess();
        try {
            Certificate cert = certificateService.findById(certId);
            return ResponseEntity.ok(certificateMapper.toDto(cert));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponseDto("NOT_FOUND", e.getMessage(), null));
        }
    }

    /**
     * POST /v1/admin/certificates/{certId}/validate
     *
     * Valide un certificat (vérifie signature, expiration, révocation).
     *
     * @param certId ID du certificat à valider
     * @return Résultat de la validation
     */
    @PostMapping("/{certId}/validate")
    public ResponseEntity<?> validateCertificate(@PathVariable UUID certId) {
        assertAdminAccess();
        try {
            boolean valid = certificateService.validateCertificate(certId);
            return ResponseEntity.ok(new ApiErrorResponseDto(
                    valid ? "VALID" : "INVALID",
                    valid ? "Certificate is valid" : "Certificate is invalid, expired, or revoked",
                    null
            ));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponseDto("ERROR", e.getMessage(), null));
        }
    }

    /**
     * POST /v1/admin/certificates/{certId}/revoke
     *
     * Révoque un certificat.
     *
     * @param certId ID du certificat à révoquer
     * @param reason Raison de la révocation (obligatoire)
     * @return Confirmation
     */
    @PostMapping("/{certId}/revoke")
    public ResponseEntity<?> revokeCertificate(
            @PathVariable UUID certId,
            @RequestParam String reason) {
        assertAdminAccess();

        if (reason == null || reason.trim().isEmpty()) {
            throw new BusinessException("Revocation reason is required");
        }

        try {
            certificateService.revokeCertificate(certId, reason);
            return ResponseEntity.ok(new ApiErrorResponseDto("REVOKED",
                    "Certificate revoked: " + reason, null));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponseDto("ERROR", e.getMessage(), null));
        }
    }

    // ============================================================
    // CONSULTATION PAR WALLET
    // ============================================================

    /**
     * GET /v1/admin/certificates/wallet/{walletId}
     *
     * Liste tous les certificats associés à un wallet.
     *
     * @param walletId ID du wallet
     * @return Liste de CertificateResponseDto
     */
    @GetMapping("/wallet/{walletId}")
    public ResponseEntity<List<CertificateResponseDto>> getWalletCertificates(
            @PathVariable UUID walletId) {
        assertAdminAccess();

        List<Certificate> certificates = certificateService.findByWalletId(walletId);

        List<CertificateResponseDto> response = certificates.stream()
                .map(certificateMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // ============================================================
    // CONSULTATION GLOBALE
    // ============================================================

    /**
     * GET /v1/admin/certificates
     *
     * Liste tous les certificats avec filtre optionnel par statut.
     *
     * @param status Filtre par statut (VALID, EXPIRED, REVOKED) - optionnel
     * @return Liste de CertificateResponseDto
     */
    @GetMapping
    public ResponseEntity<List<CertificateResponseDto>> getAllCertificates(
            @RequestParam(value = "status", required = false) String status) {
        assertAdminAccess();

        List<Certificate> certificates;

        if (status != null) {
            try {
                com.paylogic.paywalletlite.domain.crypto.enums.CertificateStatus certStatus =
                        com.paylogic.paywalletlite.domain.crypto.enums.CertificateStatus.valueOf(status.toUpperCase());
                certificates = certificateService.findByStatus(certStatus);
            } catch (IllegalArgumentException e) {
                throw new BusinessException("Invalid status: " + status
                        + ". Allowed values: VALID, EXPIRED, REVOKED");
            }
        } else {
            certificates = certificateService.findAll();
        }

        List<CertificateResponseDto> response = certificates.stream()
                .map(certificateMapper::toDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
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