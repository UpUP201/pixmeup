package com.corp.pixmeup.webauthn.dto;

import com.corp.pixmeup.webauthn.vo.CredentialInfo;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * WebAuthn 등록 완료 요청 DTO
 * 클라이언트로부터 받은 등록 완료 요청 정보
 */
@Schema(description = "WebAuthn 등록 완료 요청")
public record RegistrationFinishRequest(
        @Schema(description = "사용자 ID", example = "1")
        Long userId,
        
        @Schema(description = "세션 ID", example = "session-123")
        String sessionId,               // 세션 ID

        @Schema(description = "Credential 정보")
        CredentialInfo credential,
        
        @Schema(description = "디바이스 이름", example = "iPhone 12")
        String deviceName
) {
} 