package com.paylogic.paywalletlite.domain.token;

import com.paylogic.paywalletlite.domain.token.enums.AllocationMode;
import com.paylogic.paywalletlite.domain.token.enums.TokenAllocationConfigStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "TOKEN_ALLOCATION_CONFIGS")
public class TokenAllocationConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
    @SequenceGenerator(name = "global_seq", sequenceName = "GLOBAL_SEQ", allocationSize = 100)
    private Long id;

    @Enumerated(EnumType.STRING)
    private AllocationMode mode;

    private Integer maxTokensPerWallet;
    private Integer priorityFactor;

    @Enumerated(EnumType.STRING)
    private TokenAllocationConfigStatus status;

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

    public AllocationMode getMode() {
        return mode;
    }

    public void setMode(AllocationMode mode) {
        this.mode = mode;
    }

    public Integer getMaxTokensPerWallet() {
        return maxTokensPerWallet;
    }

    public void setMaxTokensPerWallet(Integer maxTokensPerWallet) {
        this.maxTokensPerWallet = maxTokensPerWallet;
    }

    public Integer getPriorityFactor() {
        return priorityFactor;
    }

    public void setPriorityFactor(Integer priorityFactor) {
        this.priorityFactor = priorityFactor;
    }

    public TokenAllocationConfigStatus getStatus() {
        return status;
    }

    public void setStatus(TokenAllocationConfigStatus status) {
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
}