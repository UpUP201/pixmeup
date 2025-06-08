package com.corp.pixmeup.user.dto;

public record RecentExaminationResponse(
        String age,
        String leftSight,
        String rightSight,
        String amslerStatus,
        String leftEyeVer,
        String rightEyeVer,
        String leftEyeHor,
        String rightEyeHor
) {}
