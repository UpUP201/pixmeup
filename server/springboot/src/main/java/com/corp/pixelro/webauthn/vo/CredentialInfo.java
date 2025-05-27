package com.corp.pixelro.webauthn.vo;

import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;

public record CredentialInfo(
        @Schema(description = "Credential ID (base64url 인코딩된 값)", example = "credential-123")
        String id,

        @Schema(description = "Credential ID의 바이너리 값 (base64url 인코딩)", example = "raw-id-123")
        String rawId,

        @Schema(description = "고정 값 'public-key'", example = "public-key")
        String type,

        @Schema(description = "인증자가 서명해서 브라우저에 반환한 등록 결과 데이터")
        AttestationResponse response,

        @Schema(description = "WebAuthn 확장 기능의 실행 결과")
        JsonNode clientExtensionResults
) {
}
