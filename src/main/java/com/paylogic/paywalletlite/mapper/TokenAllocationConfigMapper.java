package com.paylogic.paywalletlite.mapper;

import com.paylogic.paywalletlite.domain.token.TokenAllocationConfig;
import com.paylogic.paywalletlite.dto.response.TokenAllocationConfigResponseDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

/**
 * Mapper pour convertir les entités TokenAllocationConfig en DTOs de réponse.
 */
@Component
public class TokenAllocationConfigMapper {

    private final TokenDenominationMapper denominationMapper;

    @Autowired
    public TokenAllocationConfigMapper(TokenDenominationMapper denominationMapper) {
        this.denominationMapper = denominationMapper;
    }

    /**
     * Convertit une entité TokenAllocationConfig en TokenAllocationConfigResponseDto.
     *
     * @param config Entité TokenAllocationConfig
     * @return TokenAllocationConfigResponseDto
     */
    public TokenAllocationConfigResponseDto toResponseDto(TokenAllocationConfig config) {
        if (config == null) {
            return null;
        }

        TokenAllocationConfigResponseDto dto = new TokenAllocationConfigResponseDto();
        dto.setConfigId(config.getConfigId());
        dto.setConfigName(config.getConfigName());
        dto.setWalletType(config.getWalletType());
        dto.setDensityThreshold(config.getDensityThreshold());
        dto.setSlidingWindowSize(config.getSlidingWindowSize());
        dto.setMaxTokenCount(config.getMaxTokenCount());
        dto.setMinSingleTokenValue(config.getMinSingleTokenValue());
        dto.setMaxSingleTokenValue(config.getMaxSingleTokenValue());
        dto.setMaxTransfersPerToken(config.getMaxTransfersPerToken());
        dto.setTokenLifetimeHours(config.getTokenLifetimeHours());
        dto.setAllowOverpayment(config.getAllowOverpayment());
        dto.setMaxOverpaymentThreshold(config.getMaxOverpaymentThreshold());
        dto.setStatus(config.getStatus());
        dto.setCreatedAt(config.getCreatedAt());
        dto.setCreatedBy(config.getCreatedBy());

        // Mapper les dénominations
        if (config.getDenominations() != null) {
            dto.setDenominations(
                    config.getDenominations().stream()
                            .map(denominationMapper::toResponseDto)
                            .collect(Collectors.toList())
            );
        }

        return dto;
    }
}