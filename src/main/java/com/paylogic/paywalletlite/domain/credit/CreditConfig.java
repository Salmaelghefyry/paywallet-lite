package com.paylogic.paywalletlite.domain.credit;

import com.paylogic.paywalletlite.domain.credit.enums.CreditConfigStatus;
import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "CREDIT_CONFIGS")
public class CreditConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
    @SequenceGenerator(name = "global_seq", sequenceName = "GLOBAL_SEQ", allocationSize = 100)
    private Long id;

    private BigDecimal maxCreditAmount;
    private Double interestRate;
    private Integer repaymentPeriodDays;

    @Enumerated(EnumType.STRING)
    private CreditConfigStatus status;

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

    public BigDecimal getMaxCreditAmount() {
        return maxCreditAmount;
    }

    public void setMaxCreditAmount(BigDecimal maxCreditAmount) {
        this.maxCreditAmount = maxCreditAmount;
    }

    public Double getInterestRate() {
        return interestRate;
    }

    public void setInterestRate(Double interestRate) {
        this.interestRate = interestRate;
    }

    public Integer getRepaymentPeriodDays() {
        return repaymentPeriodDays;
    }

    public void setRepaymentPeriodDays(Integer repaymentPeriodDays) {
        this.repaymentPeriodDays = repaymentPeriodDays;
    }

    public CreditConfigStatus getStatus() {
        return status;
    }

    public void setStatus(CreditConfigStatus status) {
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