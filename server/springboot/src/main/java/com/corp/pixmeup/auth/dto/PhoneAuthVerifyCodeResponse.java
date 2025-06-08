package com.corp.pixmeup.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "휴대폰 인증 코드 검증 응답")
public record PhoneAuthVerifyCodeResponse(
        @Schema(description = "검증 성공 여부", example = "true")
        Boolean success
) {
}
