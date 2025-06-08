package com.corp.pixmeup.webauthn.vo;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import com.fasterxml.jackson.databind.JsonNode;
import io.swagger.v3.oas.annotations.media.Schema;

public record AssertionCredentialInfo(
        @Schema(description = "Credential ID (base64url 인코딩된 값)", example = "credential-123")
        String id,

        @Schema(description = "Credential ID의 바이너리 값 (base64url 인코딩)", example = "raw-id-123")
        String rawId,

        @Schema(description = "고정 값 'public-key'", example = "public-key")
        String type,

        @Schema(description = "인증자 응답 (서명 결과)")
        AssertionResponse response,

        @Schema(description = "WebAuthn 확장 기능의 실행 결과")
        @JsonSetter(nulls = Nulls.AS_EMPTY)
        JsonNode clientExtensionResults
) {
}
