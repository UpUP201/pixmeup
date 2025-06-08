package com.corp.pixmeup.webauthn.dto;

import com.corp.pixmeup.webauthn.entity.WebAuthnCredential;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.List;

/**
 * WebAuthn 인증정보 DTO 클래스
 * 클라이언트에 인증정보 목록을 반환하기 위한 DTO
 */
@Schema(description = "WebAuthn 인증정보")
public record WebAuthnCredentialDTO(
        @Schema(description = "인증정보 ID", example = "1")
        Long id,
        
        @Schema(description = "Credential ID", example = "credential-123")
        String credentialId,
        
        @Schema(description = "디바이스 이름", example = "iPhone 12")
        String deviceName,
        
        @Schema(description = "디바이스 타입", example = "platform")
        String deviceType,
        
        @Schema(description = "생성 일시", example = "2024-03-14T12:00:00")
        LocalDateTime createdAt,
        
        @Schema(description = "마지막 사용 일시", example = "2024-03-14T12:30:00")
        LocalDateTime lastUsedAt,
        
        @Schema(description = "인증기 AAGUID", example = "00000000-0000-0000-0000-000000000000")
        String aaguid,
        
        @Schema(description = "전송 방식 목록", example = "[\"internal\"]")
        List<String> transports,
        
        @Schema(description = "인증정보 상태", example = "REGISTERED", allowableValues = {"REGISTERED", "REVOKED"})
        String status
) {
    /**
     * WebAuthnCredential 엔티티를 DTO로 변환
     */
    public static WebAuthnCredentialDTO fromEntity(WebAuthnCredential credential) {
        return new WebAuthnCredentialDTO(
                credential.getId(),
                credential.getCredentialId(),
                credential.getDeviceName(),
                credential.getDeviceType(),
                credential.getRegisteredAt(),
                credential.getLastUsedAt(),
                credential.getAaguid(),
                credential.getTransportList(),
                credential.getStatus().name()
        );
    }
} 