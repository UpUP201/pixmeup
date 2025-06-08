package com.corp.pixmeup.webauthn.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "WebAuthn 등록 시작 요청")
public record RegistrationStartRequest(
        @Schema(description = "사용자 ID", example = "1")
        Long userId
) {
}
