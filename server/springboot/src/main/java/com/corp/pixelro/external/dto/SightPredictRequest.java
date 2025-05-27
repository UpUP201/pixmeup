package com.corp.pixelro.external.dto;

import java.util.List;

public record SightPredictRequest(
        Long user_id,
        List<SightHistoryItem> history
) {
}
