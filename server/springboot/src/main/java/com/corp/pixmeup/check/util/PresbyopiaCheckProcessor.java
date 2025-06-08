package com.corp.pixmeup.check.util;

import com.corp.pixmeup.check.entity.PresbyopiaCheck;

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

    // ** 리플렉션 제거 -> builder 패턴 변경 **
    public static PresbyopiaCheck buildPredictionRow(PresbyopiaCheck source) {
        return PresbyopiaCheck.builder()
                .user(source.getUser())
                .firstDistance(0.0)
                .secondDistance(0.0)
                .thirdDistance(0.0)
                .avgDistance(0.0)
                .age(0)
                .agePrediction(source.getAgePrediction())
                .aiResult("예측값입니다.")
                .createdAt(LocalDateTime.of(9999, 1, 1, 0, 0))
                .build();
    }
}
