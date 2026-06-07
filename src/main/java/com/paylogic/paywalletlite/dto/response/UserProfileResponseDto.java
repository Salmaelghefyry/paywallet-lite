package com.paylogic.paywalletlite.dto.response;

import com.paylogic.paywalletlite.domain.identity.enums.AccountStatus;
import com.paylogic.paywalletlite.domain.identity.enums.KYCStatus;
import com.paylogic.paywalletlite.domain.identity.enums.RoleType;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO pour la réponse de consultation du profil utilisateur.
 *
 * Exclut les informations sensibles (password_hash, pin_hash).
 */
public class UserProfileResponseDto {

    private UUID userId;
    private RoleType role;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private String email;
    private String nationalIdNumber;
    private AccountStatus status;
    private KYCStatus kycVerificationStatus;
    private LocalDateTime registrationTimestamp;
    private LocalDateTime lastLogin;
    private boolean isLocked;
    private Integer failedLoginAttempts;

    public UserProfileResponseDto() {}

    // Getters et Setters
    public UUID getUserId() { return userId; }
    public void setUserId(UUID userId) { this.userId = userId; }

    public RoleType getRole() { return role; }
    public void setRole(RoleType role) { this.role = role; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getPhoneNumber() { return phoneNumber; }
    public void setPhoneNumber(String phoneNumber) { this.phoneNumber = phoneNumber; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getNationalIdNumber() { return nationalIdNumber; }
    public void setNationalIdNumber(String nationalIdNumber) { this.nationalIdNumber = nationalIdNumber; }

    public AccountStatus getStatus() { return status; }
    public void setStatus(AccountStatus status) { this.status = status; }

    public KYCStatus getKycVerificationStatus() { return kycVerificationStatus; }
    public void setKycVerificationStatus(KYCStatus kycVerificationStatus) { this.kycVerificationStatus = kycVerificationStatus; }

    public LocalDateTime getRegistrationTimestamp() { return registrationTimestamp; }
    public void setRegistrationTimestamp(LocalDateTime registrationTimestamp) { this.registrationTimestamp = registrationTimestamp; }

    public LocalDateTime getLastLogin() { return lastLogin; }
    public void setLastLogin(LocalDateTime lastLogin) { this.lastLogin = lastLogin; }

    public boolean isLocked() { return isLocked; }
    public void setLocked(boolean locked) { isLocked = locked; }

    public Integer getFailedLoginAttempts() { return failedLoginAttempts; }
    public void setFailedLoginAttempts(Integer failedLoginAttempts) { this.failedLoginAttempts = failedLoginAttempts; }
}