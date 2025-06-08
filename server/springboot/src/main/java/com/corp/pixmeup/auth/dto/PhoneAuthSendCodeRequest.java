package com.corp.pixmeup.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "휴대폰 인증 코드 발송 요청")
public record PhoneAuthSendCodeRequest(
        @Schema(description = "휴대폰 번호", example = "01012345678")
        String phoneNumber
) {
}
