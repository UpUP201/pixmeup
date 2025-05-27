package com.corp.pixelro.check.repository.projection;

import java.time.LocalDateTime;

public record RecentExaminationRawDto(
        String type,
        Long id,
        String status,
        LocalDateTime createdAt,
        String aiResult,

        // 시력 검사
        Integer leftSight,
        Integer rightSight,
        Integer leftSightPrediction,
        Integer rightSightPrediction,

        // 암슬러 검사
        String leftMacularLoc,
        String rightMacularLoc,

        // 엠차트 검사
        Integer leftEyeVer,
        Integer rightEyeVer,
        Integer leftEyeHor,
        Integer rightEyeHor,

        // 노안 검사
        Integer age,
        Integer agePrediction
) {
}
