package com.paylogic.paywalletlite.service.identity;

import com.paylogic.paywalletlite.domain.identity.User;
import com.paylogic.paywalletlite.domain.identity.enums.AccountStatus;
import com.paylogic.paywalletlite.domain.identity.enums.RoleType;
import com.paylogic.paywalletlite.dto.request.RegisterRequestDto;
import com.paylogic.paywalletlite.dto.request.UpdateProfileRequestDto;
import com.paylogic.paywalletlite.exception.BusinessException;

import java.util.List;
import java.util.UUID;

public interface UserService {

    // ============================================================
    // CRÉATION
    // ============================================================
    User registerUser(RegisterRequestDto request) throws BusinessException;

    // ============================================================
    // RECHERCHE
    // ============================================================
    User findById(UUID userId);
    User findByPhoneNumber(String phoneNumber);
    User findByEmail(String email);
    List<User> findAll();
    List<User> findByStatus(AccountStatus status);
    List<User> findByRole(RoleType role);
    List<User> findByKycStatus(String kycStatus);
    List<User> searchUsers(String searchTerm);

    // ============================================================
    // MISE À JOUR DU PROFIL (UTILISATEUR)
    // ============================================================
    User updateProfile(UUID userId, UpdateProfileRequestDto request) throws BusinessException;
    void changePassword(UUID userId, String currentPassword, String newPassword) throws BusinessException;
    void changePin(UUID userId, String currentPin, String newPin) throws BusinessException;

    // ============================================================
    // GESTION DU COMPTE (UTILISATEUR)
    // ============================================================
    void deactivateAccount(UUID userId) throws BusinessException;
    void requestAccountClosure(UUID userId, String reason) throws BusinessException;

    // ============================================================
    // GESTION ADMINISTRATIVE
    // ============================================================
    void updateStatus(UUID userId, AccountStatus status);
    void updateRole(UUID userId, RoleType newRole);
    void lockAccount(UUID userId, int minutes);
    void unlockAccount(UUID userId);
    void deleteUser(UUID userId);

    // ============================================================
    // KYC
    // ============================================================
    void approveKyc(UUID userId);
    void rejectKyc(UUID userId, String reason);

    // ============================================================
    // VÉRIFICATIONS
    // ============================================================
    boolean existsByPhoneNumber(String phoneNumber);
    boolean existsByEmail(String email);

    // ============================================================
    // SÉCURITÉ
    // ============================================================
    void recordSuccessfulLogin(UUID userId);
    void recordFailedLogin(UUID userId);
    boolean isAccountLocked(UUID userId);
}