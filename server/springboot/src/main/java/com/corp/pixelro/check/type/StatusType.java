package com.corp.pixelro.check.type;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum StatusType {
    BAD("위험"),
    NORMAL("정상"),
    GOOD("좋음");

    private final String name;

    StatusType(String name) {
        this.name = name;
    }

    public static List<String> getNameByValue(String value) {
        List<String> response = new ArrayList<>();
        for (StatusType type : StatusType.values()) {
            if (type.name.contains(value)) {
                response.add(type.toString().toLowerCase());
            }
        }
        return response;
    }

    public static StatusType from(String value) {
        if (value == null) return NORMAL;
        try {
            return StatusType.valueOf(value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return NORMAL;
        }
    }
}
