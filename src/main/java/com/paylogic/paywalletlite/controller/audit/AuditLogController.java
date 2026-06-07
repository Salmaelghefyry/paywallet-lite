package com.paylogic.paywalletlite.controller.audit;

import com.paylogic.paywalletlite.domain.notification.AuditLog;
import com.paylogic.paywalletlite.dto.response.AuditLogResponseDto;
import com.paylogic.paywalletlite.exception.BusinessException;
import com.paylogic.paywalletlite.mapper.AuditLogMapper;
import com.paylogic.paywalletlite.security.AuthenticationFacade;
import com.paylogic.paywalletlite.service.audit.AuditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour la consultation des logs d'audit par l'utilisateur.
 *
 * L'utilisateur ne peut voir que les logs liés à son propre wallet.
 *
 * Route de base : /v1/audit-logs
 */
@RestController
@RequestMapping("/v1/audit-logs")
public class AuditLogController {

    private final AuditService auditService;
    private final AuthenticationFacade authenticationFacade;
    private final AuditLogMapper auditLogMapper;

    @Autowired
    public AuditLogController(AuditService auditService,
                              AuthenticationFacade authenticationFacade,
                              AuditLogMapper auditLogMapper) {
        this.auditService = auditService;
        this.authenticationFacade = authenticationFacade;
        this.auditLogMapper = auditLogMapper;
    }

    /**
     * GET /v1/audit-logs/me
     *
     * Retourne les logs d'audit liés à l'utilisateur connecté.
     *
     * @param limit Nombre max de résultats (défaut 50)
     * @return Liste de AuditLogResponseDto
     */
    @GetMapping("/me")
    public ResponseEntity<List<AuditLogResponseDto>> getMyAuditLogs(
            @RequestParam(value = "limit", defaultValue = "50") int limit) {
        UUID currentUserId = authenticationFacade.getCurrentUserId();

        List<AuditLog> logs = auditService.findByActorId(currentUserId);

        if (logs.size() > limit) {
            logs = logs.subList(0, limit);
        }

        List<AuditLogResponseDto> response = logs.stream()
                .map(auditLogMapper::toResponseDto)
                .collect(java.util.stream.Collectors.toList());

        return ResponseEntity.ok(response);
    }
}