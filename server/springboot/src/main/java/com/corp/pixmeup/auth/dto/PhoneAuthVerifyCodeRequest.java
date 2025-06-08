package com.corp.pixmeup.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "휴대폰 인증 코드 검증 요청")
public record PhoneAuthVerifyCodeRequest(
        @Schema(description = "휴대폰 번호", example = "01012345678")
        String phoneNumber,

        @Schema(description = "인증 코드", example = "123456")
        String verificationCode
) {
}
