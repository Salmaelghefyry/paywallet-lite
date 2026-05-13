package com.paylogic.paywalletlite.domain.identity;

import com.paylogic.paywalletlite.domain.identity.enums.KYCLevel;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "KYC_PROFILES")
public class KYCProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
    @SequenceGenerator(name = "global_seq", sequenceName = "GLOBAL_SEQ", allocationSize = 100)
    private Long id;

    @Column(length = 30)
    private String documentType;

    @Column(length = 50)
    private String documentNumber;

    private LocalDate dateOfBirth;

    private String nationality;

    @Enumerated(EnumType.STRING)
    private KYCLevel verifiedLevel;

    @Column(updatable = false)
    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true, nullable = false)
    private User user;

    @PrePersist
    protected void onCreate() { createdDate = LocalDateTime.now(); }
    @PreUpdate
    protected void onUpdate() { updatedDate = LocalDateTime.now(); }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDocumentType() { return documentType; }
    public void setDocumentType(String documentType) { this.documentType = documentType; }
    public String getDocumentNumber() { return documentNumber; }
    public void setDocumentNumber(String documentNumber) { this.documentNumber = documentNumber; }
    public LocalDate getDateOfBirth() { return dateOfBirth; }
    public void setDateOfBirth(LocalDate dateOfBirth) { this.dateOfBirth = dateOfBirth; }
    public String getNationality() { return nationality; }
    public void setNationality(String nationality) { this.nationality = nationality; }
    public KYCLevel getVerifiedLevel() { return verifiedLevel; }
    public void setVerifiedLevel(KYCLevel verifiedLevel) { this.verifiedLevel = verifiedLevel; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    public LocalDateTime getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(LocalDateTime updatedDate) { this.updatedDate = updatedDate; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}