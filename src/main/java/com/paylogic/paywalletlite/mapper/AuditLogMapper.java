package com.paylogic.paywalletlite.mapper;

import com.paylogic.paywalletlite.domain.notification.AuditLog;
import com.paylogic.paywalletlite.dto.response.AuditLogResponseDto;
import org.springframework.stereotype.Component;

@Component
public class AuditLogMapper {

    public AuditLogResponseDto toResponseDto(AuditLog entity) {
        if (entity == null) return null;

        AuditLogResponseDto dto = new AuditLogResponseDto();
        dto.setAuditId(entity.getAuditId());
        dto.setEventType(entity.getEventType());
        dto.setEventCategory(entity.getEventType().getCategory());
        dto.setEventSeverity(entity.getEventType().getSeverity());
        dto.setEventDescription(entity.getEventType().getDescription());
        dto.setActorId(entity.getActorId());
        dto.setActorType(entity.getActorType());
        dto.setTargetId(entity.getTargetId());
        dto.setTargetType(entity.getTargetType());
        dto.setAction(entity.getAction());
        dto.setDetailsJson(entity.getDetailsJson());
        dto.setTimestamp(entity.getTimestamp());
        dto.setIntegrityHash(entity.getIntegrityHash());

        return dto;
    }
}