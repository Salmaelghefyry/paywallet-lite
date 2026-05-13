package com.paylogic.paywalletlite.domain.wallet;

import com.paylogic.paywalletlite.domain.wallet.enums.WalletConfigStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "WALLET_CONFIGS")
public class WalletConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
    @SequenceGenerator(name = "global_seq", sequenceName = "GLOBAL_SEQ", allocationSize = 100)
    private Long id;

    private Boolean useBiometrics;
    private Integer dailyLimitAmount;

    @Enumerated(EnumType.STRING)
    private WalletConfigStatus status;

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

    public Boolean getUseBiometrics() {
        return useBiometrics;
    }

    public void setUseBiometrics(Boolean useBiometrics) {
        this.useBiometrics = useBiometrics;
    }

    public Integer getDailyLimitAmount() {
        return dailyLimitAmount;
    }

    public void setDailyLimitAmount(Integer dailyLimitAmount) {
        this.dailyLimitAmount = dailyLimitAmount;
    }

    public WalletConfigStatus getStatus() {
        return status;
    }

    public void setStatus(WalletConfigStatus status) {
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