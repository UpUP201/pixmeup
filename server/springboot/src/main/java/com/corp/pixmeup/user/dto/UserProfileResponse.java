package com.corp.pixmeup.user.dto;

import com.corp.pixmeup.global.util.PhoneUtils;
import com.corp.pixmeup.survey.entity.Survey;
import com.corp.pixmeup.survey.util.SurveyProcessor;
import com.corp.pixmeup.user.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "사용자 프로필 응답")
public record UserProfileResponse(
        @Schema(description = "사용자 이름", example = "홍길동")
        String name,
        
        @Schema(description = "마지막 검사일로부터 경과 일수 (-1은 검사 기록 없음)", example = "3")
        Integer daysSinceCheck,
        
        @Schema(description = "전화번호", example = "010-1234-5678")
        String phoneNumber,
        
        @Schema(description = "성별", example = "남성", nullable = true)
        String gender,

        @Schema(description = "나이", example = "10대", nullable = true)
        String age,

        @Schema(description = "안경 착용 여부", example = "true", nullable = true)
        Boolean glasses,
        
        @Schema(description = "수술 이력", example = "없음", nullable = true)
        String surgery,
        
        @Schema(description = "흡연 여부", example = "false", nullable = true)
        Boolean smoking,
        
        @Schema(description = "당뇨 여부", example = "false", nullable = true)
        Boolean diabetes
) {
    /**
     * 사용자와 설문 정보를 모두 포함한 프로필 응답 생성
     */
    public static UserProfileResponse from(
            User user,
            Survey survey,
            int daysSinceCheck
    ) {
        return new UserProfileResponse(
                user.getName(),
                daysSinceCheck,
                PhoneUtils.formatWithHyphen(user.getPhoneNumber()),
                survey.getGender().getLabel(),
                SurveyProcessor.estimateFirstAgeFromGroup(survey.getAge()),
                survey.isGlasses(),
                survey.getSurgery().getLabel(),
                survey.isCurrentSmoking(),
                survey.isDiabetes()
        );
    }
    
    /**
     * 설문 정보 없이 사용자 기본 정보만 포함한 프로필 응답 생성
     */
    public static UserProfileResponse fromUserOnly(
            User user,
            int daysSinceCheck
    ) {
        return new UserProfileResponse(
                user.getName(),
                daysSinceCheck,
                PhoneUtils.formatWithHyphen(user.getPhoneNumber()),
                null,  // 성별 정보 없음
                null,  // 나이 정보 없음
                null,  // 안경 착용 여부 정보 없음
                null,  // 수술 이력 정보 없음
                null,  // 흡연 여부 정보 없음
                null   // 당뇨 여부 정보 없음
        );
    }
}
