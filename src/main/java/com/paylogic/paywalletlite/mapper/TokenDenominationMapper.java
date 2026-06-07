package com.paylogic.paywalletlite.mapper;

import com.paylogic.paywalletlite.domain.token.TokenDenomination;
import com.paylogic.paywalletlite.dto.response.TokenDenominationResponseDto;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir les entités TokenDenomination en DTOs de réponse.
 */
@Component
public class TokenDenominationMapper {

    /**
     * Convertit une entité TokenDenomination en TokenDenominationResponseDto.
     *
     * @param denomination Entité TokenDenomination
     * @return TokenDenominationResponseDto
     */
    public TokenDenominationResponseDto toResponseDto(TokenDenomination denomination) {
        if (denomination == null) {
            return null;
        }

        TokenDenominationResponseDto dto = new TokenDenominationResponseDto();
        dto.setDenominationId(denomination.getDenominationId());
        dto.setValue(denomination.getValue());
        dto.setCurrencyCode(denomination.getCurrencyCode());
        dto.setIsActive(denomination.getIsActive());
        dto.setPriorityOrder(denomination.getPriorityOrder());
        dto.setDensityWeight(denomination.getDensityWeight());
        dto.setMinAllocationAmount(denomination.getMinAllocationAmount());
        dto.setMaxAllocationAmount(denomination.getMaxAllocationAmount());
        dto.setCreatedAt(denomination.getCreatedAt());

        return dto;
    }
}