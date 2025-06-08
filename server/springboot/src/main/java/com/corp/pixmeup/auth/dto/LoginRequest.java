package com.corp.pixmeup.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "로그인 요청")
public record LoginRequest(
        @Schema(description = "휴대폰 번호", example = "01012345678")
        @NotBlank(message = "휴대폰 번호를 입력해주세요.")
        String phoneNumber,

        @Schema(description = "비밀번호", example = "password123")
        @NotBlank(message = "비밀번호를 입력해주세요.")
        String password
) {}