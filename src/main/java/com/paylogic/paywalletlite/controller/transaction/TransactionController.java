package com.paylogic.paywalletlite.controller.transaction;

import com.paylogic.paywalletlite.dto.response.ApiErrorResponseDto;
import com.paylogic.paywalletlite.dto.response.TransactionResponseDto;
import com.paylogic.paywalletlite.exception.BusinessException;
import com.paylogic.paywalletlite.security.AuthenticationFacade;
import com.paylogic.paywalletlite.service.transaction.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

/**
 * Contrôleur REST pour les opérations utilisateur sur les transactions.
 *
 * Tous les endpoints utilisent l'identité de l'utilisateur connecté
 * via le JWT. L'utilisateur ne peut voir que ses propres transactions.
 *
 * Route de base : /v1/transactions
 */
@RestController
@RequestMapping("/v1/transactions")
public class TransactionController {

    private final TransactionService transactionService;
    private final AuthenticationFacade authenticationFacade;

    @Autowired
    public TransactionController(TransactionService transactionService,
                                 AuthenticationFacade authenticationFacade) {
        this.transactionService = transactionService;
        this.authenticationFacade = authenticationFacade;
    }

    // ============================================================
    // CONSULTATION DE SES PROPRES TRANSACTIONS
    // ============================================================

    /**
     * GET /v1/transactions
     *
     * Retourne toutes les transactions de l'utilisateur connecté
     * (envoyées ET reçues).
     *
     * @return Liste de TransactionResponseDto
     */
    @GetMapping
    public ResponseEntity<List<TransactionResponseDto>> getMyTransactions() {
        UUID currentUserId = authenticationFacade.getCurrentUserId();
        List<TransactionResponseDto> transactions = transactionService.getTransactionsByUserId(currentUserId);
        return ResponseEntity.ok(transactions);
    }

    /**
     * GET /v1/transactions/{id}
     *
     * Retourne les détails d'une transaction spécifique.
     * L'utilisateur doit être l'expéditeur ou le destinataire.
     *
     * @param transactionId ID de la transaction
     * @return TransactionResponseDto
     */
    @GetMapping("/{id}")
    public ResponseEntity<TransactionResponseDto> getTransactionById(@PathVariable("id") UUID transactionId) {
        UUID currentUserId = authenticationFacade.getCurrentUserId();
        TransactionResponseDto transaction = transactionService.getTransactionById(transactionId);

        // Vérifier que l'utilisateur est impliqué dans cette transaction
        if (!transaction.getSenderWalletId().equals(currentUserId)
                && !transaction.getReceiverWalletId().equals(currentUserId)) {
            throw new BusinessException("Access denied: you are not involved in this transaction");
        }

        return ResponseEntity.ok(transaction);
    }

    /**
     * GET /v1/transactions/sent
     *
     * Retourne les transactions où l'utilisateur est l'expéditeur.
     *
     * @return Liste de TransactionResponseDto
     */
    @GetMapping("/sent")
    public ResponseEntity<List<TransactionResponseDto>> getMySentTransactions() {
        UUID currentUserId = authenticationFacade.getCurrentUserId();
        List<TransactionResponseDto> transactions = transactionService.getTransactionsBySenderId(currentUserId);
        return ResponseEntity.ok(transactions);
    }

    /**
     * GET /v1/transactions/received
     *
     * Retourne les transactions où l'utilisateur est le destinataire.
     *
     * @return Liste de TransactionResponseDto
     */
    @GetMapping("/received")
    public ResponseEntity<List<TransactionResponseDto>> getMyReceivedTransactions() {
        UUID currentUserId = authenticationFacade.getCurrentUserId();
        List<TransactionResponseDto> transactions = transactionService.getTransactionsByReceiverId(currentUserId);
        return ResponseEntity.ok(transactions);
    }

    /**
     * GET /v1/transactions/offline
     *
     * Retourne les transactions offline de l'utilisateur connecté.
     *
     * @return Liste de TransactionResponseDto
     */
    @GetMapping("/offline")
    public ResponseEntity<List<TransactionResponseDto>> getMyOfflineTransactions() {
        UUID currentUserId = authenticationFacade.getCurrentUserId();
        List<TransactionResponseDto> transactions = transactionService.getOfflineTransactionsByUserId(currentUserId);
        return ResponseEntity.ok(transactions);
    }
}