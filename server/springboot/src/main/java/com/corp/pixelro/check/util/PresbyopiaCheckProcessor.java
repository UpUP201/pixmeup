package com.corp.pixelro.check.util;

import com.corp.pixelro.check.entity.PresbyopiaCheck;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Map;

public class PresbyopiaCheckProcessor {
    public static Map<String, Object> process(PresbyopiaCheck check) {
        boolean abnormal = check.getAvgDistance() > 33.0;

        return Map.of(
                "presbyopia_distance_1_cm", check.getFirstDistance(),
                "presbyopia_distance_2_cm", check.getSecondDistance(),
                "presbyopia_distance_3_cm", check.getThirdDistance(),
                "presbyopia_average_cm", check.getAvgDistance(),
                "presbyopia_abnormal_flag", abnormal
        );
    }

    public static PresbyopiaCheck buildPredictionRow(PresbyopiaCheck source) {
        PresbyopiaCheck prediction = PresbyopiaCheck.builder()
                .user(source.getUser())
                .firstDistance(0.0)
                .secondDistance(0.0)
                .thirdDistance(0.0)
                .avgDistance(source.getAvgDistance())
                .age(0)
                .agePrediction(source.getAgePrediction())
                .aiResult("예측값입니다.")
                .build();

        Field createdAtField = ReflectionUtils.findField(PresbyopiaCheck.class, "createdAt");
        createdAtField.setAccessible(true);
        ReflectionUtils.setField(createdAtField, prediction, LocalDateTime.of(9999, 1, 1, 0, 0));

        return prediction;
    }
}
