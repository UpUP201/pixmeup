package com.corp.pixmeup.external.dto;

public record SightHistoryItem(
        String date,
        Integer left_sight,
        Integer right_sight
) {
}
