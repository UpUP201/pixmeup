package com.corp.pixelro.webauthn.entity;

import com.corp.pixelro.user.entity.User;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@Entity
@Table(name = "webauthn_credentials")
@Getter
@NoArgsConstructor
public class WebAuthnCredential {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String credentialId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 64)
    private String name;

    @Column(columnDefinition = "BLOB", nullable = false)
    private byte[] publicKeyCose;

    @Column(nullable = false)
    private Long signatureCount;

    @Column(nullable = false)
    private String attestationType;

    @Column(length = 36)
    private String aaguid;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private CredentialStatus status;

    @Column(name = "transports", length = 255)
    private String transports;

    @CreationTimestamp
    @Column(nullable = false, updatable = false)
    private LocalDateTime registeredAt;

    @Column(name = "last_used_at")
    private LocalDateTime lastUsedAt;

    @Column(name = "device_type", length = 50)
    private String deviceType;

    @Column(name = "device_name", length = 255)
    private String deviceName;

    public enum CredentialStatus {
        ACTIVE,
        INACTIVE,
        REVOKED
    }

    public List<String> getTransportList() {
        if (transports == null || transports.isEmpty()) {
            return List.of();
        }
        return Arrays.asList(transports.split(","));
    }

    public void setTransportList(List<String> transportList) {
        if (transportList == null || transportList.isEmpty()) {
            this.transports = null;
        } else {
            this.transports = String.join(",", transportList);
        }
    }

    public void updateLastUsed() {
        this.lastUsedAt = LocalDateTime.now();
    }

    public void updateSignatureCount(long count) {
        if (count > this.signatureCount) {
            this.signatureCount = count;
        }
    }

    public void revoke() {
        this.status = CredentialStatus.REVOKED;
    }

    public void activate() {
        this.status = CredentialStatus.ACTIVE;
    }

    public void deactivate() {
        this.status = CredentialStatus.INACTIVE;
    }

    @Builder
    public WebAuthnCredential(String credentialId, User user, String name, byte[] publicKeyCose, Long signatureCount, String attestationType, String aaguid, CredentialStatus status, String transports, LocalDateTime lastUsedAt, String deviceType, String deviceName) {
        this.credentialId = credentialId;
        this.user = user;
        this.name = name;
        this.publicKeyCose = publicKeyCose;
        this.signatureCount = signatureCount;
        this.attestationType = attestationType;
        this.aaguid = aaguid;
        this.status = status;
        this.transports = transports;
        this.lastUsedAt = lastUsedAt;
        this.deviceType = deviceType;
        this.deviceName = deviceName;
    }
}
