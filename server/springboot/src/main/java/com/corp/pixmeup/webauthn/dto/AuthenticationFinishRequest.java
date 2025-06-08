package com.corp.pixmeup.webauthn.dto;

import com.corp.pixmeup.webauthn.vo.AssertionCredentialInfo;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * WebAuthn 인증 완료 요청 DTO
 * 클라이언트로부터 받은 인증 완료 요청 정보
 */
@Schema(description = "WebAuthn 인증 완료 요청")
public record AuthenticationFinishRequest(
        @Schema(description = "세션 ID", example = "session-123")
        String sessionId,

        @Schema(description = "Credential 정보")
        AssertionCredentialInfo credential,

        @Schema(description = "디바이스 이름", example = "iPhone 12")
        String deviceName
) {
} 