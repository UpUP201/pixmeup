package com.corp.pixmeup.survey.converter;

import com.corp.pixmeup.survey.entity.Survey;
import com.corp.pixmeup.survey.dto.SurveyRequest;
import com.corp.pixmeup.survey.dto.SurveyResponse;
import com.corp.pixmeup.survey.type.Gender;
import com.corp.pixmeup.survey.type.SurgeryType;
import com.corp.pixmeup.user.entity.User;
import org.springframework.stereotype.Component;

@Component
public class SurveyConverter {

    // Request DTO -> Domain
    public Survey toEntity(SurveyRequest dto, User user) {
        return Survey.builder()
                .user(user)
                .age(dto.age())
                .gender(Gender.valueOf(dto.gender()))
                .glasses(dto.glasses())
                .surgery(SurgeryType.valueOf(dto.surgery()))
                .diabetes(dto.diabetes())
                .currentSmoking(dto.currentSmoking())
                .pastSmoking(dto.pastSmoking())
                .build();
    }

    // Domain -> Response DTO
    public SurveyResponse toDto(Survey entity) {
        return new SurveyResponse(
                entity.getId(),
                entity.getUser().getId(),
                entity.getAge(),
                entity.getGender().toString(),
                entity.isGlasses(),
                entity.getSurgery().toString(),
                entity.isDiabetes(),
                entity.isCurrentSmoking(),
                entity.isPastSmoking()
        );
    }

}
