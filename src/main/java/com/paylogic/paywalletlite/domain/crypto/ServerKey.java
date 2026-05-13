package com.paylogic.paywalletlite.domain.crypto;

import com.paylogic.paywalletlite.domain.crypto.enums.ServerKeyPurpose;
import com.paylogic.paywalletlite.domain.crypto.enums.ServerKeyStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "SERVER_KEYS")
public class ServerKey {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
    @SequenceGenerator(name = "global_seq", sequenceName = "GLOBAL_SEQ", allocationSize = 100)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ServerKeyPurpose purpose;

    @Column(unique = true, nullable = false)
    private String keyId;

    @Lob
    private byte[] encryptedPrivateKey;

    @Column(nullable = false)
    private byte[] publicKey;

    @Enumerated(EnumType.STRING)
    private ServerKeyStatus status;

    private LocalDateTime rotationDate;

    @Column(updatable = false)
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @PrePersist
    protected void onCreate() { createdDate = LocalDateTime.now(); }
    @PreUpdate
    protected void onUpdate() { updatedDate = LocalDateTime.now(); }

    // getters & setters...

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ServerKeyPurpose getPurpose() {
        return purpose;
    }

    public void setPurpose(ServerKeyPurpose purpose) {
        this.purpose = purpose;
    }

    public String getKeyId() {
        return keyId;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public byte[] getEncryptedPrivateKey() {
        return encryptedPrivateKey;
    }

    public void setEncryptedPrivateKey(byte[] encryptedPrivateKey) {
        this.encryptedPrivateKey = encryptedPrivateKey;
    }

    public byte[] getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(byte[] publicKey) {
        this.publicKey = publicKey;
    }

    public ServerKeyStatus getStatus() {
        return status;
    }

    public void setStatus(ServerKeyStatus status) {
        this.status = status;
    }

    public LocalDateTime getRotationDate() {
        return rotationDate;
    }

    public void setRotationDate(LocalDateTime rotationDate) {
        this.rotationDate = rotationDate;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public LocalDateTime getUpdatedDate() {
        return updatedDate;
    }

    public void setUpdatedDate(LocalDateTime updatedDate) {
        this.updatedDate = updatedDate;
    }
}