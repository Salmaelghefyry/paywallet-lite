package com.paylogic.paywalletlite.controller.auth;

import com.paylogic.paywalletlite.domain.identity.User;
import com.paylogic.paywalletlite.dto.request.UpdateProfileRequestDto;
import com.paylogic.paywalletlite.dto.response.ApiErrorResponseDto;
import com.paylogic.paywalletlite.dto.response.UserProfileResponseDto;
import com.paylogic.paywalletlite.exception.BusinessException;
import com.paylogic.paywalletlite.mapper.UserMapper;
import com.paylogic.paywalletlite.security.AuthenticationFacade;
import com.paylogic.paywalletlite.service.identity.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Contrôleur REST pour les opérations utilisateur sur son propre compte.
 *
 * Tous les endpoints utilisent l'identité de l'utilisateur connecté
 * via le JWT. L'utilisateur ne peut gérer que son propre profil.
 *
 * Route de base : /v1/users
 */
@RestController
@RequestMapping("/v1/users")
public class UserController {

    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;
    private final UserMapper userMapper;

    @Autowired
    public UserController(UserService userService,
                          AuthenticationFacade authenticationFacade,
                          UserMapper userMapper) {
        this.userService = userService;
        this.authenticationFacade = authenticationFacade;
        this.userMapper = userMapper;
    }

    // ============================================================
    // CONSULTATION DE SON PROPRE PROFIL
    // ============================================================

    /**
     * GET /v1/users/me
     *
     * Retourne le profil complet de l'utilisateur connecté.
     *
     * @return UserProfileResponseDto
     */
    @GetMapping("/me")
    public ResponseEntity<UserProfileResponseDto> getMyProfile() {
        UUID currentUserId = authenticationFacade.getCurrentUserId();
        User user = userService.findById(currentUserId);
        return ResponseEntity.ok(userMapper.toProfileResponseDto(user));
    }

    /**
     * GET /v1/users/me/status
     *
     * Retourne le statut du compte de l'utilisateur connecté.
     *
     * @return Statut du compte
     */
    @GetMapping("/me/status")
    public ResponseEntity<?> getMyAccountStatus() {
        UUID currentUserId = authenticationFacade.getCurrentUserId();
        User user = userService.findById(currentUserId);

        Map<String, Object> status = new HashMap<>();
        status.put("userId", user.getUserId());
        status.put("accountStatus", user.getStatus());
        status.put("kycStatus", user.getKycVerificationStatus());
        status.put("isLocked", userService.isAccountLocked(currentUserId));
        status.put("failedLoginAttempts", user.getFailedLoginAttempts());
        status.put("lastLogin", user.getLastLogin());
        status.put("role", user.getRole());

        return ResponseEntity.ok(status);
    }

    /**
     * GET /v1/users/me/kyc-status
     *
     * Retourne le statut KYC de l'utilisateur connecté.
     *
     * @return Statut KYC
     */
    @GetMapping("/me/kyc-status")
    public ResponseEntity<?> getMyKycStatus() {
        UUID currentUserId = authenticationFacade.getCurrentUserId();
        User user = userService.findById(currentUserId);

        Map<String, Object> kyc = new HashMap<>();
        kyc.put("kycStatus", user.getKycVerificationStatus());
        kyc.put("userId", user.getUserId());

        return ResponseEntity.ok(kyc);
    }

    // ============================================================
    // MISE À JOUR DE SON PROPRE PROFIL
    // ============================================================

    /**
     * PUT /v1/users/me
     *
     * Met à jour le profil de l'utilisateur connecté.
     * Ne permet pas de changer le rôle, le statut, ou les informations sensibles.
     *
     * @param request DTO contenant les champs modifiables
     * @return Profil mis à jour
     */
    @PutMapping("/me")
    public ResponseEntity<?> updateMyProfile(@Valid @RequestBody UpdateProfileRequestDto request) {
        try {
            UUID currentUserId = authenticationFacade.getCurrentUserId();
            User updatedUser = userService.updateProfile(currentUserId, request);
            return ResponseEntity.ok(userMapper.toProfileResponseDto(updatedUser));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponseDto("ERROR", e.getMessage(), null));
        }
    }

    /**
     * PUT /v1/users/me/password
     *
     * Change le mot de passe de l'utilisateur connecté.
     *
     * @param currentPassword Mot de passe actuel
     * @param newPassword     Nouveau mot de passe
     * @return Confirmation
     */
    @PutMapping("/me/password")
    public ResponseEntity<?> changeMyPassword(
            @RequestParam("currentPassword") String currentPassword,
            @RequestParam("newPassword") String newPassword) {
        try {
            UUID currentUserId = authenticationFacade.getCurrentUserId();
            userService.changePassword(currentUserId, currentPassword, newPassword);
            return ResponseEntity.ok(new ApiErrorResponseDto("SUCCESS", "Password changed successfully", null));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponseDto("ERROR", e.getMessage(), null));
        }
    }

    /**
     * PUT /v1/users/me/pin
     *
     * Change le code PIN de l'utilisateur connecté.
     *
     * @param currentPin PIN actuel
     * @param newPin     Nouveau PIN
     * @return Confirmation
     */
    @PutMapping("/me/pin")
    public ResponseEntity<?> changeMyPin(
            @RequestParam("currentPin") String currentPin,
            @RequestParam("newPin") String newPin) {
        try {
            UUID currentUserId = authenticationFacade.getCurrentUserId();
            userService.changePin(currentUserId, currentPin, newPin);
            return ResponseEntity.ok(new ApiErrorResponseDto("SUCCESS", "PIN changed successfully", null));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponseDto("ERROR", e.getMessage(), null));
        }
    }

    // ============================================================
    // GESTION DU COMPTE
    // ============================================================

    /**
     * POST /v1/users/me/deactivate
     *
     * Désactive temporairement le compte de l'utilisateur connecté.
     *
     * @return Confirmation
     */
    @PostMapping("/me/deactivate")
    public ResponseEntity<?> deactivateMyAccount() {
        try {
            UUID currentUserId = authenticationFacade.getCurrentUserId();
            userService.deactivateAccount(currentUserId);
            return ResponseEntity.ok(new ApiErrorResponseDto("SUCCESS", "Account deactivated successfully", null));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponseDto("ERROR", e.getMessage(), null));
        }
    }

    /**
     * POST /v1/users/me/request-closure
     *
     * Demande la fermeture définitive du compte.
     * Nécessite une validation administrative.
     *
     * @param reason Raison de la fermeture
     * @return Confirmation
     */
    @PostMapping("/me/request-closure")
    public ResponseEntity<?> requestAccountClosure(@RequestParam("reason") String reason) {
        try {
            UUID currentUserId = authenticationFacade.getCurrentUserId();
            userService.requestAccountClosure(currentUserId, reason);
            return ResponseEntity.ok(new ApiErrorResponseDto("SUCCESS", "Account closure requested", null));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponseDto("ERROR", e.getMessage(), null));
        }
    }
}