package com.paylogic.paywalletlite.domain.wallet;

import com.paylogic.paywalletlite.domain.identity.User;
import com.paylogic.paywalletlite.domain.wallet.enums.WalletStatus;
import com.paylogic.paywalletlite.domain.wallet.enums.WalletType;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "WALLETS")
public class Wallet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
    @SequenceGenerator(name = "global_seq", sequenceName = "GLOBAL_SEQ", allocationSize = 100)
    private Long id;

    @Column(unique = true, nullable = false, length = 64)
    private String walletIdentifier;

    @Enumerated(EnumType.STRING)
    private WalletType type;

    @Enumerated(EnumType.STRING)
    private WalletStatus status;

    private BigDecimal balance;

    @Column(updatable = false)
    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @OneToOne(mappedBy = "wallet", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private WalletKeyPair keyPair;

    @OneToOne(mappedBy = "wallet", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = true)
    private WalletConfig walletConfig;

    @PrePersist
    protected void onCreate() { createdDate = LocalDateTime.now(); }
    @PreUpdate
    protected void onUpdate() { updatedDate = LocalDateTime.now(); }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getWalletIdentifier() {
        return walletIdentifier;
    }

    public void setWalletIdentifier(String walletIdentifier) {
        this.walletIdentifier = walletIdentifier;
    }

    public WalletType getType() {
        return type;
    }

    public void setType(WalletType type) {
        this.type = type;
    }

    public WalletStatus getStatus() {
        return status;
    }

    public void setStatus(WalletStatus status) {
        this.status = status;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public WalletKeyPair getKeyPair() {
        return keyPair;
    }

    public void setKeyPair(WalletKeyPair keyPair) {
        this.keyPair = keyPair;
    }

    public WalletConfig getWalletConfig() {
        return walletConfig;
    }

    public void setWalletConfig(WalletConfig walletConfig) {
        this.walletConfig = walletConfig;
    }
}