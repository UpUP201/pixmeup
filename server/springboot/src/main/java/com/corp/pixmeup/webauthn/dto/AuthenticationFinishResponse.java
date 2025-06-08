package com.corp.pixmeup.webauthn.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "WebAuthn 인증 완료 응답")
public record AuthenticationFinishResponse(
        @Schema(description = "사용자 ID", example = "1")
        Long userId,

        @Schema(description = "사용자 이름", example = "홍길동")
        String userName,

        @Schema(description = "등록된 credential의 ID (식별자 x, base64Url)", example = "8xbz8K0mOeC_-27fjisYaXe5itLXFzrGMZWjL_8XmJY")
        String credentialId,

        @Schema(description = "액세스 토큰", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
        String accessToken
) {}