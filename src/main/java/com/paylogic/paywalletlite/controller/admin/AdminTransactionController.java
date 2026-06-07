package com.paylogic.paywalletlite.controller.admin;

import com.paylogic.paywalletlite.domain.transaction.enums.TransactionStatus;
import com.paylogic.paywalletlite.domain.transaction.enums.TransactionType;
import com.paylogic.paywalletlite.dto.response.ApiErrorResponseDto;
import com.paylogic.paywalletlite.dto.response.TransactionResponseDto;
import com.paylogic.paywalletlite.exception.BusinessException;
import com.paylogic.paywalletlite.security.AuthenticationFacade;
import com.paylogic.paywalletlite.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour les opérations d'administration des transactions.
 *
 * Tous les endpoints nécessitent le rôle ADMIN.
 * Permet la consultation de toutes les transactions, le filtrage,
 * et les opérations de gestion.
 *
 * Route de base : /v1/admin/transactions
 */
@RestController
@RequestMapping("/v1/admin/transactions")
@PreAuthorize("hasRole('ADMIN')")
public class AdminTransactionController {

    private final TransactionService transactionService;
    private final AuthenticationFacade authenticationFacade;

    @Autowired
    public AdminTransactionController(TransactionService transactionService,
                                      AuthenticationFacade authenticationFacade) {
        this.transactionService = transactionService;
        this.authenticationFacade = authenticationFacade;
    }

    // ============================================================
    // CONSULTATION GLOBALE
    // ============================================================

    /**
     * GET /v1/admin/transactions
     *
     * Retourne toutes les transactions avec filtres optionnels.
     *
     * @param status   Filtre par statut (optionnel)
     * @param type     Filtre par type (optionnel)
     * @param fromDate Filtre date début (optionnel)
     * @param toDate   Filtre date fin (optionnel)
     * @param limit    Nombre max de résultats (défaut 100)
     * @return Liste de TransactionResponseDto
     */
    @GetMapping
    public ResponseEntity<List<TransactionResponseDto>> getAllTransactions(
            @RequestParam(value = "status", required = false) TransactionStatus status,
            @RequestParam(value = "type", required = false) TransactionType type,
            @RequestParam(value = "fromDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fromDate,
            @RequestParam(value = "toDate", required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime toDate,
            @RequestParam(value = "limit", defaultValue = "100") int limit) {

        assertAdminAccess();

        List<TransactionResponseDto> transactions;

        if (status != null) {
            transactions = transactionService.getTransactionsByStatus(status);
        } else if (type != null) {
            transactions = transactionService.getTransactionsByType(type);
        } else if (fromDate != null && toDate != null) {
            transactions = transactionService.getTransactionsByDateRange(fromDate, toDate);
        } else {
            transactions = transactionService.getAllTransactions();
        }

        // Appliquer la limite
        if (transactions.size() > limit) {
            transactions = transactions.subList(0, limit);
        }

        return ResponseEntity.ok(transactions);
    }

    /**
     * GET /v1/admin/transactions/{id}
     *
     * Retourne les détails d'une transaction (admin : pas de restriction).
     *
     * @param transactionId ID de la transaction
     * @return TransactionResponseDto
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> getTransactionById(@PathVariable("id") UUID transactionId) {
        assertAdminAccess();
        TransactionResponseDto transaction = transactionService.getTransactionById(transactionId);
        return ResponseEntity.ok(transaction);
    }

    // ============================================================
    // CONSULTATION PAR UTILISATEUR
    // ============================================================

    /**
     * GET /v1/admin/transactions/user/{userId}
     *
     * Retourne toutes les transactions d'un utilisateur spécifique.
     *
     * @param userId ID de l'utilisateur
     * @return Liste de TransactionResponseDto
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByUser(
            @PathVariable("userId") UUID userId) {
        assertAdminAccess();
        List<TransactionResponseDto> transactions = transactionService.getTransactionsByUserId(userId);
        return ResponseEntity.ok(transactions);
    }

    /**
     * GET /v1/admin/transactions/wallet/{walletId}
     *
     * Retourne toutes les transactions d'un wallet spécifique.
     *
     * @param walletId ID du wallet
     * @return Liste de TransactionResponseDto
     */
    @GetMapping("/wallet/{walletId}")
    public ResponseEntity<List<TransactionResponseDto>> getTransactionsByWallet(
            @PathVariable("walletId") UUID walletId) {
        assertAdminAccess();
        List<TransactionResponseDto> transactions = transactionService.getTransactionsByWalletId(walletId);
        return ResponseEntity.ok(transactions);
    }

    // ============================================================
    // CONSULTATION PAR STATUT
    // ============================================================

    /**
     * GET /v1/admin/transactions/status/pending
     *
     * Retourne les transactions en attente (online pending).
     *
     * @return Liste de TransactionResponseDto
     */
    @GetMapping("/status/pending")
    public ResponseEntity<List<TransactionResponseDto>> getPendingTransactions() {
        assertAdminAccess();
        List<TransactionResponseDto> transactions = transactionService
                .getTransactionsByStatus(TransactionStatus.PENDING);
        return ResponseEntity.ok(transactions);
    }

    /**
     * GET /v1/admin/transactions/status/offline-pending
     *
     * Retourne les transactions offline en attente de synchronisation.
     *
     * @return Liste de TransactionResponseDto
     */
    @GetMapping("/status/offline-pending")
    public ResponseEntity<List<TransactionResponseDto>> getOfflinePendingTransactions() {
        assertAdminAccess();
        List<TransactionResponseDto> transactions = transactionService
                .getTransactionsByStatus(TransactionStatus.OFFLINE_PENDING);
        return ResponseEntity.ok(transactions);
    }

    /**
     * GET /v1/admin/transactions/status/failed
     *
     * Retourne les transactions échouées.
     *
     * @return Liste de TransactionResponseDto
     */
    @GetMapping("/status/failed")
    public ResponseEntity<List<TransactionResponseDto>> getFailedTransactions() {
        assertAdminAccess();
        List<TransactionResponseDto> transactions = transactionService
                .getTransactionsByStatus(TransactionStatus.FAILED);
        return ResponseEntity.ok(transactions);
    }

    /**
     * GET /v1/admin/transactions/status/disputed
     *
     * Retourne les transactions contestées.
     *
     * @return Liste de TransactionResponseDto
     */
    @GetMapping("/status/disputed")
    public ResponseEntity<List<TransactionResponseDto>> getDisputedTransactions() {
        assertAdminAccess();
        List<TransactionResponseDto> transactions = transactionService
                .getTransactionsByStatus(TransactionStatus.DISPUTED);
        return ResponseEntity.ok(transactions);
    }

    // ============================================================
    // GESTION DES TRANSACTIONS
    // ============================================================

    /**
     * POST /v1/admin/transactions/{id}/cancel
     *
     * Annule une transaction en attente.
     *
     * @param transactionId ID de la transaction à annuler
     * @return TransactionResponseDto mis à jour
     */
    @PostMapping("/{id}/cancel")
    public ResponseEntity<TransactionResponseDto> cancelTransaction(@PathVariable("id") UUID transactionId) {
        assertAdminAccess();
        TransactionResponseDto transaction = transactionService.cancelTransaction(transactionId);
        return ResponseEntity.ok(transaction);
    }

    /**
     * POST /v1/admin/transactions/{id}/dispute
     *
     * Marque une transaction comme contestée.
     *
     * @param transactionId ID de la transaction
     * @param reason        Raison de la contestation
     * @return TransactionResponseDto mis à jour
     */
    @PostMapping("/{id}/dispute")
    public ResponseEntity<TransactionResponseDto> disputeTransaction(
            @PathVariable("id") UUID transactionId,
            @RequestParam("reason") String reason) {
        assertAdminAccess();

        if (reason == null || reason.trim().isEmpty()) {
            throw new BusinessException("Dispute reason is required");
        }

        TransactionResponseDto transaction = transactionService.disputeTransaction(transactionId, reason);
        return ResponseEntity.ok(transaction);
    }

    /**
     * POST /v1/admin/transactions/{id}/resolve
     *
     * Résout une transaction contestée.
     *
     * @param transactionId ID de la transaction
     * @param resolution    Description de la résolution
     * @return TransactionResponseDto mis à jour
     */
    @PostMapping("/{id}/resolve")
    public ResponseEntity<TransactionResponseDto> resolveDispute(
            @PathVariable("id") UUID transactionId,
            @RequestParam("resolution") String resolution) {
        assertAdminAccess();

        if (resolution == null || resolution.trim().isEmpty()) {
            throw new BusinessException("Resolution description is required");
        }

        TransactionResponseDto transaction = transactionService.resolveDispute(transactionId, resolution);
        return ResponseEntity.ok(transaction);
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