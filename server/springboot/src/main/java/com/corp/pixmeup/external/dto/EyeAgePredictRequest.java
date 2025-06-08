package com.corp.pixmeup.external.dto;

import java.util.List;

public record EyeAgePredictRequest(
        Long user_id,
        List<Integer> history
) {
}
