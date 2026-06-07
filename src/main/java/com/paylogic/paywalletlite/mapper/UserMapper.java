package com.paylogic.paywalletlite.mapper;

import com.paylogic.paywalletlite.domain.identity.User;
import com.paylogic.paywalletlite.dto.response.UserProfileResponseDto;
import org.springframework.stereotype.Component;

/**
 * Mapper pour convertir les entités User en DTOs de réponse.
 *
 * Garantit que les informations sensibles (password_hash, pin_hash)
 * ne sont jamais exposées dans les réponses API.
 */
@Component
public class UserMapper {

    /**
     * Convertit une entité User en UserProfileResponseDto.
     *
     * @param user Entité User (ne doit pas être null)
     * @return UserProfileResponseDto sans informations sensibles
     */
    public UserProfileResponseDto toProfileResponseDto(User user) {
        if (user == null) {
            return null;
        }

        UserProfileResponseDto dto = new UserProfileResponseDto();
        dto.setUserId(user.getUserId());
        dto.setRole(user.getRole());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setEmail(user.getEmail());
        dto.setNationalIdNumber(user.getNationalIdNumber());
        dto.setStatus(user.getStatus());
        dto.setKycVerificationStatus(user.getKycVerificationStatus());
        dto.setRegistrationTimestamp(user.getRegistrationTimestamp());
        dto.setLastLogin(user.getLastLogin());
        dto.setLocked(user.getLockedUntil() != null
                && user.getLockedUntil().isAfter(java.time.LocalDateTime.now()));
        dto.setFailedLoginAttempts(user.getFailedLoginAttempts());

        return dto;
    }
}