package com.paylogic.paywalletlite.mapper;

import com.paylogic.paywalletlite.domain.crypto.CertificateAuthority;
import com.paylogic.paywalletlite.dto.response.CertificateAuthorityResponseDto;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir les entités CertificateAuthority en DTOs de réponse.
 *
 * Ne sérialise jamais les clés privées ou le certificat PEM complet.
 */
@Component
public class CertificateAuthorityMapper {

    /**
     * Convertit une entité CertificateAuthority en CertificateAuthorityResponseDto.
     *
     * @param ca Entité CertificateAuthority
     * @return CertificateAuthorityResponseDto sans données sensibles
     */
    public CertificateAuthorityResponseDto toResponseDto(CertificateAuthority ca) {
        if (ca == null) {
            return null;
        }

        CertificateAuthorityResponseDto dto = new CertificateAuthorityResponseDto();
        dto.setCaId(ca.getCaId());
        dto.setCaName(ca.getCaName());
        dto.setKeyAlgorithm(ca.getKeyAlgorithm());
        dto.setCreatedAt(ca.getCreatedAt());
        dto.setExpiresAt(ca.getExpiresAt());
        dto.setStatus(ca.getStatus() != null ? ca.getStatus().name() : "UNKNOWN");

        return dto;
    }
}