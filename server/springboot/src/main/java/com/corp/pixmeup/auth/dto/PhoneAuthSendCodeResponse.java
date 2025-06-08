package com.corp.pixmeup.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "휴대폰 인증 코드 발송 응답")
public record PhoneAuthSendCodeResponse(
        @Schema(description = "발송 성공 여부", example = "true")
        Boolean success,

        @Schema(description = "인증 코드 유효 시간(초)", example = "180")
        Integer expiresInSeconds
) {
}
