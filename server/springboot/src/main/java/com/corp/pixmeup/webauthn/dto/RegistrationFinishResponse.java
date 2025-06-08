package com.corp.pixmeup.webauthn.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;

@Schema(description = "WebAuthn 등록 완료 응답")
public record RegistrationFinishResponse(
        @Schema(description = "Credential ID", example = "credential-123")
        String credentialId,
        
        @Schema(description = "디바이스 타입", example = "platform")
        String deviceType,
        
        @Schema(description = "생성 일시", example = "2024-03-14T12:00:00")
        LocalDateTime createdAt
) {
}
