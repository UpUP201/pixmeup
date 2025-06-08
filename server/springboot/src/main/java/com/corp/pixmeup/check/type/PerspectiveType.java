package com.corp.pixmeup.check.type;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public enum PerspectiveType {
    NORMAL("정상"),
    NEARSIGHTED("근시"),
    FARSIGHTED("원시"),
    ASTIGMATISM("난시");
    private final String name;

    PerspectiveType(String name) {
        this.name = name;
    }

    public static List<String> getNameByValue(String value) {
        List<String> response = new ArrayList<>();
        for (PerspectiveType type : PerspectiveType.values()) {
            if (type.name.contains(value)) {
                response.add(type.toString().toLowerCase());
            }
        }
        return response;
    }
}
