package com.paylogic.paywalletlite.dto.response;

import com.paylogic.paywalletlite.domain.notification.enums.AuditEventType;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO pour la réponse de consultation des logs d'audit.
 */
public class AuditLogResponseDto {

    private UUID auditId;
    private AuditEventType eventType;
    private String eventCategory;
    private String eventSeverity;
    private String eventDescription;
    private UUID actorId;
    private String actorType;
    private UUID targetId;
    private String targetType;
    private String action;
    private String detailsJson;
    private LocalDateTime timestamp;
    private String integrityHash;

    public AuditLogResponseDto() {}

    // Getters et Setters
    public UUID getAuditId() { return auditId; }
    public void setAuditId(UUID auditId) { this.auditId = auditId; }

    public AuditEventType getEventType() { return eventType; }
    public void setEventType(AuditEventType eventType) { this.eventType = eventType; }

    public String getEventCategory() { return eventCategory; }
    public void setEventCategory(String eventCategory) { this.eventCategory = eventCategory; }

    public String getEventSeverity() { return eventSeverity; }
    public void setEventSeverity(String eventSeverity) { this.eventSeverity = eventSeverity; }

    public String getEventDescription() { return eventDescription; }
    public void setEventDescription(String eventDescription) { this.eventDescription = eventDescription; }

    public UUID getActorId() { return actorId; }
    public void setActorId(UUID actorId) { this.actorId = actorId; }

    public String getActorType() { return actorType; }
    public void setActorType(String actorType) { this.actorType = actorType; }

    public UUID getTargetId() { return targetId; }
    public void setTargetId(UUID targetId) { this.targetId = targetId; }

    public String getTargetType() { return targetType; }
    public void setTargetType(String targetType) { this.targetType = targetType; }

    public String getAction() { return action; }
    public void setAction(String action) { this.action = action; }

    public String getDetailsJson() { return detailsJson; }
    public void setDetailsJson(String detailsJson) { this.detailsJson = detailsJson; }

    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public String getIntegrityHash() { return integrityHash; }
    public void setIntegrityHash(String integrityHash) { this.integrityHash = integrityHash; }
}