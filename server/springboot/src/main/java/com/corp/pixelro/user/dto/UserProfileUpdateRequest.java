package com.corp.pixelro.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "사용자 프로필 수정 요청")
public record UserProfileUpdateRequest(
        @NotBlank
        @Schema(description = "사용자 이름", example = "홍길동")
        String name,
        
        @NotBlank
        @Schema(description = "전화번호", example = "01012345678")
        String phoneNumber
) {
}
