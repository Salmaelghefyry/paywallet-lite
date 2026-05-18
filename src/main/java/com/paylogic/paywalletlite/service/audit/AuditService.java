package com.paylogic.paywalletlite.service.audit;

import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class AuditService {
    public void logEvent(String eventType, UUID actorId, String actorType,
                         UUID targetId, String targetType, String details) {
        // Implementation later
    }
}