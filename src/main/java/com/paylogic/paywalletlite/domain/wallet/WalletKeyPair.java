package com.paylogic.paywalletlite.domain.wallet;

import com.paylogic.paywalletlite.domain.wallet.enums.KeyStatus;
import com.paylogic.paywalletlite.domain.wallet.enums.KeyStorageType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "WALLET_KEY_PAIRS")
public class WalletKeyPair {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
    @SequenceGenerator(name = "global_seq", sequenceName = "GLOBAL_SEQ", allocationSize = 100)
    private Long id;

    @Column(unique = true, nullable = false, length = 100)
    private String publicKey;

    @Column(length = 500)
    private String encryptedPrivateKey;

    @Enumerated(EnumType.STRING)
    private KeyStorageType keyStorageType;

    @Enumerated(EnumType.STRING)
    private KeyStatus status;

    @Column(updatable = false)
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id", unique = true, nullable = false)
    private Wallet wallet;

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

    public String getPublicKey() {
        return publicKey;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getEncryptedPrivateKey() {
        return encryptedPrivateKey;
    }

    public void setEncryptedPrivateKey(String encryptedPrivateKey) {
        this.encryptedPrivateKey = encryptedPrivateKey;
    }

    public KeyStorageType getKeyStorageType() {
        return keyStorageType;
    }

    public void setKeyStorageType(KeyStorageType keyStorageType) {
        this.keyStorageType = keyStorageType;
    }

    public KeyStatus getStatus() {
        return status;
    }

    public void setStatus(KeyStatus status) {
        this.status = status;
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

    public Wallet getWallet() {
        return wallet;
    }

    public void setWallet(Wallet wallet) {
        this.wallet = wallet;
    }
}