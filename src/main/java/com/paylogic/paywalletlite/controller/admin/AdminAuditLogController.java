package com.paylogic.paywalletlite.controller.admin;

import com.paylogic.paywalletlite.domain.notification.AuditLog;
import com.paylogic.paywalletlite.domain.notification.enums.AuditEventType;
import com.paylogic.paywalletlite.dto.response.AuditLogResponseDto;
import com.paylogic.paywalletlite.exception.BusinessException;
import com.paylogic.paywalletlite.mapper.AuditLogMapper;
import com.paylogic.paywalletlite.security.AuthenticationFacade;
import com.paylogic.paywalletlite.service.audit.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Contrôleur REST pour les opérations d'administration des logs d'audit.
 *
 * Tous les endpoints nécessitent le rôle ADMIN.
 * Permet la consultation complète, le filtrage, et la vérification d'intégrité.
 *
 * Route de base : /v1/admin/audit-logs
 */
@RestController
@RequestMapping("/v1/admin/audit-logs")
@PreAuthorize("hasRole('ADMIN')")
public class AdminAuditLogController {

    private final AuditService auditService;
    private final AuthenticationFacade authenticationFacade;
    private final AuditLogMapper auditLogMapper;

    @Autowired
    public AdminAuditLogController(AuditService auditService,
                                   AuthenticationFacade authenticationFacade,
                                   AuditLogMapper auditLogMapper) {
        this.auditService = auditService;
        this.authenticationFacade = authenticationFacade;
        this.auditLogMapper = auditLogMapper;
    }

    // ============================================================
    // CONSULTATION GLOBALE
    // ============================================================

    /**
     * GET /v1/admin/audit-logs
     *
     * Retourne les logs d'audit récents avec filtres optionnels.
     *
     * @param eventType Filtre par type d'événement (optionnel)
     * @param targetId  Filtre par ID cible (optionnel)
     * @param actorId   Filtre par ID acteur (optionnel)
     * @param limit     Nombre max de résultats (défaut 200)
     * @return Liste de AuditLogResponseDto
     */
    @GetMapping
    public ResponseEntity<List<AuditLogResponseDto>> getAllAuditLogs(
            @RequestParam(value = "eventType", required = false) AuditEventType eventType,
            @RequestParam(value = "targetId", required = false) UUID targetId,
            @RequestParam(value = "actorId", required = false) UUID actorId,
            @RequestParam(value = "limit", defaultValue = "200") int limit) {

        assertAdminAccess();

        List<AuditLog> logs;

        if (eventType != null) {
            logs = auditService.findByEventType(eventType);
        } else if (targetId != null) {
            logs = auditService.findByTargetId(targetId);
        } else if (actorId != null) {
            logs = auditService.findByActorId(actorId);
        } else {
            logs = auditService.findRecentEvents(limit);
        }

        if (logs.size() > limit) {
            logs = logs.subList(0, limit);
        }

        List<AuditLogResponseDto> response = logs.stream()
                .map(auditLogMapper::toResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * GET /v1/admin/audit-logs/{id}
     *
     * Retourne un log d'audit spécifique.
     *
     * @param auditId ID du log d'audit
     * @return AuditLogResponseDto
     */
    @GetMapping("/{id}")
    public ResponseEntity<AuditLogResponseDto> getAuditLogById(@PathVariable("id") UUID auditId) {
        assertAdminAccess();
        AuditLog log = auditService.findById(auditId);
        return ResponseEntity.ok(auditLogMapper.toResponseDto(log));
    }

    // ============================================================
    // CONSULTATION PAR TYPE
    // ============================================================

    /**
     * GET /v1/admin/audit-logs/type/{eventType}
     *
     * Retourne les logs d'audit filtrés par type d'événement.
     *
     * @param eventType Type d'événement
     * @param limit     Nombre max de résultats (défaut 100)
     * @return Liste de AuditLogResponseDto
     */
    @GetMapping("/type/{eventType}")
    public ResponseEntity<List<AuditLogResponseDto>> getAuditLogsByType(
            @PathVariable("eventType") AuditEventType eventType,
            @RequestParam(value = "limit", defaultValue = "100") int limit) {

        assertAdminAccess();

        List<AuditLog> logs = auditService.findByEventType(eventType);

        if (logs.size() > limit) {
            logs = logs.subList(0, limit);
        }

        List<AuditLogResponseDto> response = logs.stream()
                .map(auditLogMapper::toResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * GET /v1/admin/audit-logs/target/{targetId}
     *
     * Retourne les logs d'audit pour une entité cible.
     *
     * @param targetId ID de l'entité cible
     * @return Liste de AuditLogResponseDto
     */
    @GetMapping("/target/{targetId}")
    public ResponseEntity<List<AuditLogResponseDto>> getAuditLogsByTarget(
            @PathVariable("targetId") UUID targetId) {

        assertAdminAccess();

        List<AuditLog> logs = auditService.findByTargetId(targetId);

        List<AuditLogResponseDto> response = logs.stream()
                .map(auditLogMapper::toResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * GET /v1/admin/audit-logs/actor/{actorId}
     *
     * Retourne les logs d'audit pour un acteur spécifique.
     *
     * @param actorId ID de l'acteur
     * @return Liste de AuditLogResponseDto
     */
    @GetMapping("/actor/{actorId}")
    public ResponseEntity<List<AuditLogResponseDto>> getAuditLogsByActor(
            @PathVariable("actorId") UUID actorId) {

        assertAdminAccess();

        List<AuditLog> logs = auditService.findByActorId(actorId);

        List<AuditLogResponseDto> response = logs.stream()
                .map(auditLogMapper::toResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // ============================================================
    // SÉCURITÉ & FRAUDE
    // ============================================================

    /**
     * GET /v1/admin/audit-logs/security/fraud
     *
     * Retourne les logs liés à la fraude (double spend, fraud alerts).
     *
     * @param limit Nombre max de résultats (défaut 50)
     * @return Liste de AuditLogResponseDto
     */
    @GetMapping("/security/fraud")
    public ResponseEntity<List<AuditLogResponseDto>> getFraudAuditLogs(
            @RequestParam(value = "limit", defaultValue = "50") int limit) {

        assertAdminAccess();

        List<AuditLog> logs = auditService.findByEventType(AuditEventType.DOUBLE_SPEND_DETECTED);
        logs.addAll(auditService.findByEventType(AuditEventType.FRAUD_ALERT_TRIGGERED));

        // Trier par date décroissante
        logs.sort((a, b) -> b.getTimestamp().compareTo(a.getTimestamp()));

        if (logs.size() > limit) {
            logs = logs.subList(0, limit);
        }

        List<AuditLogResponseDto> response = logs.stream()
                .map(auditLogMapper::toResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * GET /v1/admin/audit-logs/security/auth-failures
     *
     * Retourne les logs d'échecs d'authentification.
     *
     * @param limit Nombre max de résultats (défaut 50)
     * @return Liste de AuditLogResponseDto
     */
    @GetMapping("/security/auth-failures")
    public ResponseEntity<List<AuditLogResponseDto>> getAuthFailureLogs(
            @RequestParam(value = "limit", defaultValue = "50") int limit) {

        assertAdminAccess();

        List<AuditLog> logs = auditService.findByEventType(AuditEventType.AUTH_FAILURE);

        if (logs.size() > limit) {
            logs = logs.subList(0, limit);
        }

        List<AuditLogResponseDto> response = logs.stream()
                .map(auditLogMapper::toResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    // ============================================================
    // INTÉGRITÉ
    // ============================================================

    /**
     * GET /v1/admin/audit-logs/integrity/verify
     *
     * Vérifie l'intégrité de la chaîne d'audit.
     *
     * @return Résultat de la vérification
     */
    @GetMapping("/integrity/verify")
    public ResponseEntity<AuditIntegrityResponse> verifyAuditIntegrity() {
        assertAdminAccess();

        boolean integrityOk = auditService.verifyChainIntegrity();

        AuditIntegrityResponse response = new AuditIntegrityResponse();
        response.setIntegrityValid(integrityOk);
        response.setCheckedAt(java.time.LocalDateTime.now());
        response.setMessage(integrityOk
                ? "La chaîne d'audit est intègre."
                : "ALERTE : La chaîne d'audit est corrompue !");

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

    // ============================================================
    // DTO INTERNE
    // ============================================================

    /**
     * DTO pour la réponse de vérification d'intégrité.
     */
    public static class AuditIntegrityResponse {
        private boolean integrityValid;
        private java.time.LocalDateTime checkedAt;
        private String message;

        public boolean isIntegrityValid() { return integrityValid; }
        public void setIntegrityValid(boolean integrityValid) { this.integrityValid = integrityValid; }
        public java.time.LocalDateTime getCheckedAt() { return checkedAt; }
        public void setCheckedAt(java.time.LocalDateTime checkedAt) { this.checkedAt = checkedAt; }
        public String getMessage() { return message; }
        public void setMessage(String message) { this.message = message; }
    }
}