package com.corp.pixmeup.webauthn.dto;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * WebAuthn 인증 요청 DTO
 * 클라이언트로부터 받은 인증 시작 요청 정보
 */
@Schema(description = "WebAuthn 인증 요청")
public record AuthenticationRequest(
        @Schema(description = "사용자 검증 방식", example = "preferred", allowableValues = {"required", "preferred", "discouraged"})
        String userVerification
) {
} 