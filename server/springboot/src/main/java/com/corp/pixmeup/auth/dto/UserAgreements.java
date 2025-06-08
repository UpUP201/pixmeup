package com.corp.pixmeup.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.AssertTrue;

@Schema(description = "이용약관 동의 정보")
public record UserAgreements(
        @Schema(description = "서비스 이용약관 동의 여부", example = "true")
        @AssertTrue(message = "서비스 이용약관에 동의해주세요.")
        boolean service,

        @Schema(description = "개인정보 처리방침 동의 여부", example = "true")
        @AssertTrue(message = "개인정보 처리방침에 동의해주세요.")
        boolean privacy,

        @Schema(description = "민감정보 수집 동의 여부", example = "true")
        @AssertTrue(message = "민감정보 수집에 동의해주세요.")
        boolean sensitiveInfo,

        @Schema(description = "제3자 제공 동의 여부", example = "true")
        @AssertTrue(message = "제3자 제공에 동의해주세요.")
        boolean thirdParty,

        @Schema(description = "위탁 동의 여부", example = "true")
        @AssertTrue(message = "위탁에 동의해주세요.")
        boolean consignment,

        @Schema(description = "마케팅 정보 수신 동의 여부 (선택)", example = "false")
        boolean marketing
) {
}
