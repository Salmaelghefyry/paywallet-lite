package com.paylogic.paywalletlite.mapper;

import com.paylogic.paywalletlite.domain.crypto.ServerKey;
import com.paylogic.paywalletlite.dto.response.ServerKeyResponseDto;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir les entités ServerKey en DTOs de réponse.
 *
 * Ne sérialise JAMAIS la clé privée.
 * La clé publique n'est pas incluse dans le DTO standard
 * (elle est accessible via un endpoint dédié).
 */
@Component
public class ServerKeyMapper {

    /**
     * Convertit une entité ServerKey en ServerKeyResponseDto.
     *
     * @param serverKey Entité ServerKey
     * @return ServerKeyResponseDto sans données sensibles
     */
    public ServerKeyResponseDto toResponseDto(ServerKey serverKey) {
        if (serverKey == null) {
            return null;
        }

        ServerKeyResponseDto dto = new ServerKeyResponseDto();
        dto.setServerKeyId(serverKey.getServerKeyId());
        dto.setKeyPurpose(serverKey.getKeyPurpose());
        dto.setKeyAlgorithm(serverKey.getKeyAlgorithm());
        dto.setStatus(serverKey.getStatus());
        dto.setCreatedAt(serverKey.getCreatedAt());
        dto.setExpiresAt(serverKey.getExpiresAt());
        dto.setRotatedAt(serverKey.getRotatedAt());
        dto.setKmsReference(serverKey.getKmsReference());
        dto.setWalletId(serverKey.getWalletId());
        dto.setHasPublicKey(serverKey.getPublicKeyPem() != null && !serverKey.getPublicKeyPem().isEmpty());

        return dto;
    }
}