package com.corp.pixelro.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "사용자 비밀번호 변경 요청")
public record UserPasswordUpdateRequest(
        @NotBlank
        @Schema(description = "현재 비밀번호", example = "current123")
        String currentPassword,
        
        @NotBlank
        @Schema(description = "새 비밀번호", example = "new123")
        String newPassword
) {
}
