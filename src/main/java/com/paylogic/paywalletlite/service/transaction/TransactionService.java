package com.paylogic.paywalletlite.service.transaction;

import com.paylogic.paywalletlite.domain.transaction.Transaction;
import com.paylogic.paywalletlite.domain.transaction.enums.TransactionStatus;
import com.paylogic.paywalletlite.domain.transaction.enums.TransactionType;
import com.paylogic.paywalletlite.dto.request.TransactionCreateRequestDto;
import com.paylogic.paywalletlite.dto.response.TransactionResponseDto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Service métier pour la gestion des transactions.
 * Couvre les 3 phases du workflow : préparation, paiement, reconciliation.
 */
public interface TransactionService {

    // ============================================================
    // CRÉATION
    // ============================================================

    /**
     * Phase 1/2 : Crée une nouvelle transaction (online ou offline).
     */
    TransactionResponseDto createTransaction(TransactionCreateRequestDto request);

    // ============================================================
    // CONSULTATION — UTILISATEUR
    // ============================================================

    /**
     * Récupère une transaction par son ID.
     */
    TransactionResponseDto getTransactionById(UUID transactionId);

    /**
     * Récupère toutes les transactions d'un wallet (envoyées + reçues).
     */
    List<TransactionResponseDto> getTransactionsByWalletId(UUID walletId);

    /**
     * Récupère les transactions d'un utilisateur (tous ses wallets).
     */
    List<TransactionResponseDto> getTransactionsByUserId(UUID userId);

    /**
     * Récupère les transactions où l'utilisateur est expéditeur.
     */
    List<TransactionResponseDto> getTransactionsBySenderId(UUID senderWalletId);

    /**
     * Récupère les transactions où l'utilisateur est destinataire.
     */
    List<TransactionResponseDto> getTransactionsByReceiverId(UUID receiverWalletId);

    /**
     * Récupère les transactions offline d'un utilisateur.
     */
    List<TransactionResponseDto> getOfflineTransactionsByUserId(UUID userId);

    // ============================================================
    // CONSULTATION — ADMINISTRATION
    // ============================================================

    /**
     * Récupère toutes les transactions du système.
     */
    List<TransactionResponseDto> getAllTransactions();

    /**
     * Récupère les transactions par statut.
     */
    List<TransactionResponseDto> getTransactionsByStatus(TransactionStatus status);

    /**
     * Récupère les transactions par type.
     */
    List<TransactionResponseDto> getTransactionsByType(TransactionType type);

    /**
     * Récupère les transactions dans une plage de dates.
     */
    List<TransactionResponseDto> getTransactionsByDateRange(LocalDateTime fromDate, LocalDateTime toDate);

    // ============================================================
    // GESTION DU CYCLE DE VIE
    // ============================================================

    /**
     * Met à jour le statut d'une transaction.
     */
    TransactionResponseDto updateTransactionStatus(UUID transactionId, TransactionStatus newStatus);

    /**
     * Finalise une transaction (après reconciliation).
     */
    TransactionResponseDto completeTransaction(UUID transactionId);

    /**
     * Annule une transaction en attente.
     */
    TransactionResponseDto cancelTransaction(UUID transactionId);

    /**
     * Associe une transaction à un lot de synchronisation.
     */
    void assignToSyncBatch(UUID transactionId, UUID syncBatchId);

    /**
     * Calcule le montant du surpaiement si transferredAmount > requestedAmount.
     */
    BigDecimal calculateOverpayment(UUID transactionId);

    // ============================================================
    // GESTION DES LITIGES (ADMIN)
    // ============================================================

    /**
     * Marque une transaction comme contestée.
     */
    TransactionResponseDto disputeTransaction(UUID transactionId, String reason);

    /**
     * Résout une transaction contestée.
     */
    TransactionResponseDto resolveDispute(UUID transactionId, String resolution);

    // ============================================================
    // ENTITÉ (POUR USAGE INTERNE)
    // ============================================================

    /**
     * Récupère l'entité Transaction (usage interne services).
     */
    Transaction findEntityById(UUID transactionId);
}