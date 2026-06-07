package com.paylogic.paywalletlite.dto.response;

import com.paylogic.paywalletlite.domain.crypto.enums.ServerKeyPurpose;
import com.paylogic.paywalletlite.domain.crypto.enums.ServerKeyStatus;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * DTO pour la réponse de consultation d'une clé serveur.
 *
 * N'expose JAMAIS la clé privée (privateKeyEncrypted).
 * La clé publique n'est exposée que via un endpoint dédié.
 */
public class ServerKeyResponseDto {

    private UUID serverKeyId;
    private ServerKeyPurpose keyPurpose;
    private String keyAlgorithm;
    private ServerKeyStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime expiresAt;
    private LocalDateTime rotatedAt;
    private String kmsReference;
    private UUID walletId;
    private boolean hasPublicKey;

    public ServerKeyResponseDto() {}

    // Getters et Setters
    public UUID getServerKeyId() { return serverKeyId; }
    public void setServerKeyId(UUID serverKeyId) { this.serverKeyId = serverKeyId; }

    public ServerKeyPurpose getKeyPurpose() { return keyPurpose; }
    public void setKeyPurpose(ServerKeyPurpose keyPurpose) { this.keyPurpose = keyPurpose; }

    public String getKeyAlgorithm() { return keyAlgorithm; }
    public void setKeyAlgorithm(String keyAlgorithm) { this.keyAlgorithm = keyAlgorithm; }

    public ServerKeyStatus getStatus() { return status; }
    public void setStatus(ServerKeyStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public LocalDateTime getExpiresAt() { return expiresAt; }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt = expiresAt; }

    public LocalDateTime getRotatedAt() { return rotatedAt; }
    public void setRotatedAt(LocalDateTime rotatedAt) { this.rotatedAt = rotatedAt; }

    public String getKmsReference() { return kmsReference; }
    public void setKmsReference(String kmsReference) { this.kmsReference = kmsReference; }

    public UUID getWalletId() { return walletId; }
    public void setWalletId(UUID walletId) { this.walletId = walletId; }

    public boolean isHasPublicKey() { return hasPublicKey; }
    public void setHasPublicKey(boolean hasPublicKey) { this.hasPublicKey = hasPublicKey; }
}