package com.corp.pixelro.survey.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Gender {
    M("남자"),
    W("여자");

    private final String label;

}
