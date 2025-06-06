package com.corp.pixelro.check.util;

import com.corp.pixelro.check.entity.SightCheck;
import com.corp.pixelro.check.type.PerspectiveType;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Map;

public class SightCheckProcessor {
    public static Map<String, Object> process(SightCheck check) {
        double leftSight = check.getLeftSight() / 10.0;
        double rightSight = check.getRightSight() / 10.0;

        boolean abnormal = leftSight < 0.5 || rightSight < 0.5;

        return Map.of(
                "left_eye_vision", leftSight,
                "right_eye_vision", rightSight,
                "sight_abnormal_flag", abnormal
        );
    }

    // ** 리플렉션 제거 -> builder 패턴 추가 **
    public static SightCheck buildPredictionRow(SightCheck source) {
        return SightCheck.builder()
                .user(source.getUser())
                .leftSight(source.getLeftSightPrediction())
                .rightSight(source.getRightSightPrediction())
                .aiResult("예측값입니다.")
                .createdAt(LocalDateTime.of(9999, 12, 31, 23, 59))
                .build();

    }
}
