package com.paylogic.paywalletlite.controller.admin;

import com.paylogic.paywalletlite.domain.identity.User;
import com.paylogic.paywalletlite.domain.identity.enums.AccountStatus;
import com.paylogic.paywalletlite.domain.identity.enums.RoleType;
import com.paylogic.paywalletlite.dto.request.RegisterRequestDto;
import com.paylogic.paywalletlite.dto.response.ApiErrorResponseDto;
import com.paylogic.paywalletlite.dto.response.UserProfileResponseDto;
import com.paylogic.paywalletlite.exception.BusinessException;
import com.paylogic.paywalletlite.mapper.UserMapper;
import com.paylogic.paywalletlite.security.AuthenticationFacade;
import com.paylogic.paywalletlite.service.identity.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * Contrôleur REST pour les opérations d'administration des utilisateurs.
 *
 * Tous les endpoints nécessitent le rôle ADMIN.
 * Permet la gestion complète des comptes utilisateurs.
 *
 * Route de base : /v1/admin/users
 */
@RestController
@RequestMapping("/v1/admin/users")
@PreAuthorize("hasRole('ADMIN')")
public class AdminUserController {

    private final UserService userService;
    private final AuthenticationFacade authenticationFacade;
    private final UserMapper userMapper;

    @Autowired
    public AdminUserController(UserService userService,
                               AuthenticationFacade authenticationFacade,
                               UserMapper userMapper) {
        this.userService = userService;
        this.authenticationFacade = authenticationFacade;
        this.userMapper = userMapper;
    }

    // ============================================================
    // CRÉATION (ADMIN)
    // ============================================================

    /**
     * POST /v1/admin/users
     *
     * Crée un nouvel utilisateur (admin seulement).
     * Utile pour créer des comptes administrateur ou marchand.
     *
     * @param request DTO d'inscription
     * @return Utilisateur créé
     */
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody RegisterRequestDto request) {
        try {
            assertAdminAccess();
            User user = userService.registerUser(request);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(userMapper.toProfileResponseDto(user));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponseDto("ERROR", e.getMessage(), null));
        }
    }

    // ============================================================
    // CONSULTATION (ADMIN)
    // ============================================================

    /**
     * GET /v1/admin/users
     *
     * Retourne tous les utilisateurs avec filtres optionnels.
     *
     * @param status Filtre par statut (optionnel)
     * @param role   Filtre par rôle (optionnel)
     * @param search Recherche par nom, prénom, téléphone ou email (optionnel)
     * @param limit  Nombre max de résultats (défaut 100)
     * @return Liste de UserProfileResponseDto
     */
    @GetMapping
    public ResponseEntity<List<UserProfileResponseDto>> getAllUsers(
            @RequestParam(value = "status", required = false) AccountStatus status,
            @RequestParam(value = "role", required = false) RoleType role,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "limit", defaultValue = "100") int limit) {

        assertAdminAccess();

        List<User> users;

        if (status != null) {
            users = userService.findByStatus(status);
        } else if (role != null) {
            users = userService.findByRole(role);
        } else if (search != null && !search.trim().isEmpty()) {
            users = userService.searchUsers(search);
        } else {
            users = userService.findAll();
        }

        // Appliquer la limite
        if (users.size() > limit) {
            users = users.subList(0, limit);
        }

        List<UserProfileResponseDto> response = users.stream()
                .map(userMapper::toProfileResponseDto)
                .collect(Collectors.toList());

        return ResponseEntity.ok(response);
    }

    /**
     * GET /v1/admin/users/{userId}
     *
     * Retourne le profil complet d'un utilisateur.
     *
     * @param userId ID de l'utilisateur
     * @return UserProfileResponseDto
     */
    @GetMapping("/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable UUID userId) {
        assertAdminAccess();
        try {
            User user = userService.findById(userId);
            return ResponseEntity.ok(userMapper.toProfileResponseDto(user));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponseDto("NOT_FOUND", e.getMessage(), null));
        }
    }

    /**
     * GET /v1/admin/users/phone/{phoneNumber}
     *
     * Recherche un utilisateur par numéro de téléphone.
     *
     * @param phoneNumber Numéro de téléphone
     * @return UserProfileResponseDto
     */
    @GetMapping("/phone/{phoneNumber}")
    public ResponseEntity<?> getUserByPhone(@PathVariable String phoneNumber) {
        assertAdminAccess();
        try {
            User user = userService.findByPhoneNumber(phoneNumber);
            return ResponseEntity.ok(userMapper.toProfileResponseDto(user));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponseDto("NOT_FOUND", e.getMessage(), null));
        }
    }

    /**
     * GET /v1/admin/users/email/{email}
     *
     * Recherche un utilisateur par email.
     *
     * @param email Adresse email
     * @return UserProfileResponseDto
     */
    @GetMapping("/email/{email}")
    public ResponseEntity<?> getUserByEmail(@PathVariable String email) {
        assertAdminAccess();
        try {
            User user = userService.findByEmail(email);
            return ResponseEntity.ok(userMapper.toProfileResponseDto(user));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponseDto("NOT_FOUND", e.getMessage(), null));
        }
    }

    /**
     * GET /v1/admin/users/status/{status}
     *
     * Retourne les utilisateurs filtrés par statut.
     *
     * @param status Statut du compte
     * @return Liste de UserProfileResponseDto
     */
    @GetMapping("/status/{status}")
    public ResponseEntity<List<UserProfileResponseDto>> getUsersByStatus(
            @PathVariable AccountStatus status) {
        assertAdminAccess();
        List<User> users = userService.findByStatus(status);
        List<UserProfileResponseDto> response = users.stream()
                .map(userMapper::toProfileResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * GET /v1/admin/users/role/{role}
     *
     * Retourne les utilisateurs filtrés par rôle.
     *
     * @param role Rôle utilisateur
     * @return Liste de UserProfileResponseDto
     */
    @GetMapping("/role/{role}")
    public ResponseEntity<List<UserProfileResponseDto>> getUsersByRole(
            @PathVariable RoleType role) {
        assertAdminAccess();
        List<User> users = userService.findByRole(role);
        List<UserProfileResponseDto> response = users.stream()
                .map(userMapper::toProfileResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * GET /v1/admin/users/{userId}/details
     *
     * Retourne les détails complets d'un utilisateur (incluant infos sensibles).
     *
     * @param userId ID de l'utilisateur
     * @return Détails complets
     */
    @GetMapping("/{userId}/details")
    public ResponseEntity<?> getUserDetails(@PathVariable UUID userId) {
        assertAdminAccess();
        try {
            User user = userService.findById(userId);

            Map<String, Object> details = new HashMap<>();
            details.put("userId", user.getUserId());
            details.put("firstName", user.getFirstName());
            details.put("lastName", user.getLastName());
            details.put("phoneNumber", user.getPhoneNumber());
            details.put("email", user.getEmail());
            details.put("nationalIdNumber", user.getNationalIdNumber());
            details.put("role", user.getRole());
            details.put("status", user.getStatus());
            details.put("kycStatus", user.getKycVerificationStatus());
            details.put("registrationTimestamp", user.getRegistrationTimestamp());
            details.put("lastLogin", user.getLastLogin());
            details.put("failedLoginAttempts", user.getFailedLoginAttempts());
            details.put("isLocked", userService.isAccountLocked(userId));

            return ResponseEntity.ok(details);
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ApiErrorResponseDto("NOT_FOUND", e.getMessage(), null));
        }
    }

    // ============================================================
    // GESTION DES STATUTS (ADMIN)
    // ============================================================

    /**
     * PUT /v1/admin/users/{userId}/status
     *
     * Met à jour le statut d'un compte utilisateur.
     *
     * @param userId ID de l'utilisateur
     * @param status Nouveau statut
     * @return Confirmation
     */
    @PutMapping("/{userId}/status")
    public ResponseEntity<?> updateUserStatus(
            @PathVariable UUID userId,
            @RequestParam AccountStatus status) {
        assertAdminAccess();
        try {
            userService.updateStatus(userId, status);
            return ResponseEntity.ok(new ApiErrorResponseDto("SUCCESS",
                    "User status updated to " + status, null));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponseDto("ERROR", e.getMessage(), null));
        }
    }

    /**
     * PUT /v1/admin/users/{userId}/role
     *
     * Change le rôle d'un utilisateur.
     *
     * @param userId  ID de l'utilisateur
     * @param newRole Nouveau rôle
     * @return Confirmation
     */
    @PutMapping("/{userId}/role")
    public ResponseEntity<?> updateUserRole(
            @PathVariable UUID userId,
            @RequestParam RoleType newRole) {
        assertAdminAccess();
        try {
            userService.updateRole(userId, newRole);
            return ResponseEntity.ok(new ApiErrorResponseDto("SUCCESS",
                    "User role updated to " + newRole, null));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponseDto("ERROR", e.getMessage(), null));
        }
    }

    /**
     * POST /v1/admin/users/{userId}/lock
     *
     * Verrouille un compte utilisateur.
     *
     * @param userId  ID de l'utilisateur
     * @param minutes Durée du verrouillage en minutes (défaut 30)
     * @param reason  Raison du verrouillage (obligatoire)
     * @return Confirmation
     */
    @PostMapping("/{userId}/lock")
    public ResponseEntity<?> lockUserAccount(
            @PathVariable UUID userId,
            @RequestParam(defaultValue = "30") int minutes,
            @RequestParam("reason") String reason) {
        assertAdminAccess();

        if (reason == null || reason.trim().isEmpty()) {
            throw new BusinessException("Lock reason is required");
        }

        try {
            userService.lockAccount(userId, minutes);
            return ResponseEntity.ok(new ApiErrorResponseDto("SUCCESS",
                    "Account locked for " + minutes + " minutes. Reason: " + reason, null));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponseDto("ERROR", e.getMessage(), null));
        }
    }

    /**
     * POST /v1/admin/users/{userId}/unlock
     *
     * Déverrouille un compte utilisateur.
     *
     * @param userId ID de l'utilisateur
     * @return Confirmation
     */
    @PostMapping("/{userId}/unlock")
    public ResponseEntity<?> unlockUserAccount(@PathVariable UUID userId) {
        assertAdminAccess();
        try {
            userService.unlockAccount(userId);
            return ResponseEntity.ok(new ApiErrorResponseDto("SUCCESS", "Account unlocked", null));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponseDto("ERROR", e.getMessage(), null));
        }
    }

    /**
     * POST /v1/admin/users/{userId}/suspend
     *
     * Suspend un compte utilisateur.
     *
     * @param userId ID de l'utilisateur
     * @param reason Raison de la suspension
     * @return Confirmation
     */
    @PostMapping("/{userId}/suspend")
    public ResponseEntity<?> suspendUserAccount(
            @PathVariable UUID userId,
            @RequestParam("reason") String reason) {
        assertAdminAccess();

        if (reason == null || reason.trim().isEmpty()) {
            throw new BusinessException("Suspension reason is required");
        }

        try {
            userService.updateStatus(userId, AccountStatus.SUSPENDED);
            return ResponseEntity.ok(new ApiErrorResponseDto("SUCCESS",
                    "Account suspended. Reason: " + reason, null));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponseDto("ERROR", e.getMessage(), null));
        }
    }

    /**
     * POST /v1/admin/users/{userId}/reactivate
     *
     * Réactive un compte suspendu.
     *
     * @param userId ID de l'utilisateur
     * @return Confirmation
     */
    @PostMapping("/{userId}/reactivate")
    public ResponseEntity<?> reactivateUserAccount(@PathVariable UUID userId) {
        assertAdminAccess();
        try {
            userService.updateStatus(userId, AccountStatus.ACTIVE);
            return ResponseEntity.ok(new ApiErrorResponseDto("SUCCESS", "Account reactivated", null));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponseDto("ERROR", e.getMessage(), null));
        }
    }

    // ============================================================
    // FERMETURE DE COMPTE (ADMIN)
    // ============================================================

    /**
     * POST /v1/admin/users/{userId}/close
     *
     * Ferme définitivement un compte utilisateur.
     *
     * @param userId ID de l'utilisateur
     * @param reason Raison de la fermeture (obligatoire)
     * @return Confirmation
     */
    @PostMapping("/{userId}/close")
    public ResponseEntity<?> closeUserAccount(
            @PathVariable UUID userId,
            @RequestParam("reason") String reason) {
        assertAdminAccess();

        if (reason == null || reason.trim().isEmpty()) {
            throw new BusinessException("Closure reason is required");
        }

        try {
            userService.updateStatus(userId, AccountStatus.CLOSED);
            return ResponseEntity.ok(new ApiErrorResponseDto("SUCCESS",
                    "Account closed permanently. Reason: " + reason, null));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponseDto("ERROR", e.getMessage(), null));
        }
    }

    // ============================================================
    // KYC (ADMIN)
    // ============================================================

    /**
     * GET /v1/admin/users/kyc/pending
     *
     * Retourne les utilisateurs en attente de vérification KYC.
     *
     * @return Liste de UserProfileResponseDto
     */
    @GetMapping("/kyc/pending")
    public ResponseEntity<List<UserProfileResponseDto>> getPendingKycUsers() {
        assertAdminAccess();
        List<User> users = userService.findByKycStatus("PENDING");
        List<UserProfileResponseDto> response = users.stream()
                .map(userMapper::toProfileResponseDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }

    /**
     * POST /v1/admin/users/{userId}/kyc/approve
     *
     * Approuve la vérification KYC d'un utilisateur.
     *
     * @param userId ID de l'utilisateur
     * @return Confirmation
     */
    @PostMapping("/{userId}/kyc/approve")
    public ResponseEntity<?> approveKyc(@PathVariable UUID userId) {
        assertAdminAccess();
        try {
            userService.approveKyc(userId);
            return ResponseEntity.ok(new ApiErrorResponseDto("SUCCESS", "KYC approved", null));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponseDto("ERROR", e.getMessage(), null));
        }
    }

    /**
     * POST /v1/admin/users/{userId}/kyc/reject
     *
     * Rejette la vérification KYC d'un utilisateur.
     *
     * @param userId ID de l'utilisateur
     * @param reason Raison du rejet
     * @return Confirmation
     */
    @PostMapping("/{userId}/kyc/reject")
    public ResponseEntity<?> rejectKyc(
            @PathVariable UUID userId,
            @RequestParam("reason") String reason) {
        assertAdminAccess();

        if (reason == null || reason.trim().isEmpty()) {
            throw new BusinessException("Rejection reason is required");
        }

        try {
            userService.rejectKyc(userId, reason);
            return ResponseEntity.ok(new ApiErrorResponseDto("SUCCESS",
                    "KYC rejected. Reason: " + reason, null));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponseDto("ERROR", e.getMessage(), null));
        }
    }

    // ============================================================
    // SUPPRESSION (ADMIN)
    // ============================================================

    /**
     * DELETE /v1/admin/users/{userId}
     *
     * Supprime définitivement un utilisateur.
     * Action irréversible.
     *
     * @param userId ID de l'utilisateur
     * @return Confirmation
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<?> deleteUser(@PathVariable UUID userId) {
        assertAdminAccess();
        try {
            userService.deleteUser(userId);
            return ResponseEntity.ok(new ApiErrorResponseDto("SUCCESS",
                    "User permanently deleted", null));
        } catch (BusinessException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new ApiErrorResponseDto("ERROR", e.getMessage(), null));
        }
    }

    // ============================================================
    // MÉTHODE PRIVÉE
    // ============================================================

    private void assertAdminAccess() {
        if (!authenticationFacade.isCurrentUserAdmin()) {
            throw new BusinessException("Access denied: administrative privileges required");
        }
    }
}