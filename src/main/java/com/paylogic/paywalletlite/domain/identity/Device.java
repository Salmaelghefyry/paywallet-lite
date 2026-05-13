package com.paylogic.paywalletlite.domain.identity;

import com.paylogic.paywalletlite.domain.identity.enums.DevicePlatform;
import com.paylogic.paywalletlite.domain.identity.enums.DeviceStatus;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "DEVICES")
public class Device {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "global_seq")
    @SequenceGenerator(name = "global_seq", sequenceName = "GLOBAL_SEQ", allocationSize = 100)
    private Long id;

    @Column(unique = true, nullable = false)
    private String deviceId;

    @Enumerated(EnumType.STRING)
    private DevicePlatform platform;

    @Enumerated(EnumType.STRING)
    private DeviceStatus status;

    @Column(length = 50)
    private String pushToken;

    @Column(updatable = false)
    private LocalDateTime createdDate;

    private LocalDateTime updatedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @PrePersist
    protected void onCreate() { createdDate = LocalDateTime.now(); }
    @PreUpdate
    protected void onUpdate() { updatedDate = LocalDateTime.now(); }

    // Getters & Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDeviceId() { return deviceId; }
    public void setDeviceId(String deviceId) { this.deviceId = deviceId; }
    public DevicePlatform getPlatform() { return platform; }
    public void setPlatform(DevicePlatform platform) { this.platform = platform; }
    public DeviceStatus getStatus() { return status; }
    public void setStatus(DeviceStatus status) { this.status = status; }
    public String getPushToken() { return pushToken; }
    public void setPushToken(String pushToken) { this.pushToken = pushToken; }
    public LocalDateTime getCreatedDate() { return createdDate; }
    public void setCreatedDate(LocalDateTime createdDate) { this.createdDate = createdDate; }
    public LocalDateTime getUpdatedDate() { return updatedDate; }
    public void setUpdatedDate(LocalDateTime updatedDate) { this.updatedDate = updatedDate; }
    public User getUser() { return user; }
    public void setUser(User user) { this.user = user; }
}