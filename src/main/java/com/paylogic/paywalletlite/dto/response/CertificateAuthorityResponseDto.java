package com.paylogic.paywalletlite.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO pour la réponse de consultation d'une autorité de certification.
 */
public class CertificateAuthorityResponseDto {

    private UUID caId;
    private String caName;
    private String keyAlgorithm;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private String status;

    public CertificateAuthorityResponseDto() {}

    // Getters et Setters
    public UUID getCaId() { return caId; }
    public void setCaId(UUID caId) { this.caId = caId; }

    public String getCaName() { return caName; }
    public void setCaName(String caName) { this.caName = caName; }

    public String getKeyAlgorithm() { return keyAlgorithm; }
    public void setKeyAlgorithm(String keyAlgorithm) { this.keyAlgorithm = keyAlgorithm; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
}