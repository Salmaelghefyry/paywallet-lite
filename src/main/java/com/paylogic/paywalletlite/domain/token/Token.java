package com.paylogic.paywalletlite.domain.token;

import com.paylogic.paywalletlite.domain.token.enums.TokenStatus;
import com.paylogic.paywalletlite.domain.wallet.Wallet;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "TOKENS")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
    @SequenceGenerator(name = "global_seq", sequenceName = "GLOBAL_SEQ", allocationSize = 100)
    private Long id;

    @Column(unique = true, nullable = false, length = 128)
    private String tokenValue;

    private BigDecimal amount;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "denomination_id")
    private TokenDenomination denomination;

    @Enumerated(EnumType.STRING)
    private TokenStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "wallet_id")
    private Wallet currentOwner;

    @OneToMany(mappedBy = "token", cascade = CascadeType.ALL)
    private List<TokenSignature> signatures;

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

    public String getTokenValue() {
        return tokenValue;
    }

    public void setTokenValue(String tokenValue) {
        this.tokenValue = tokenValue;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public TokenDenomination getDenomination() {
        return denomination;
    }

    public void setDenomination(TokenDenomination denomination) {
        this.denomination = denomination;
    }

    public TokenStatus getStatus() {
        return status;
    }

    public void setStatus(TokenStatus status) {
        this.status = status;
    }

    public Wallet getCurrentOwner() {
        return currentOwner;
    }

    public void setCurrentOwner(Wallet currentOwner) {
        this.currentOwner = currentOwner;
    }

    public List<TokenSignature> getSignatures() {
        return signatures;
    }

    public void setSignatures(List<TokenSignature> signatures) {
        this.signatures = signatures;
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