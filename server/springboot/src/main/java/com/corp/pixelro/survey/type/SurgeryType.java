package com.corp.pixelro.survey.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SurgeryType {
    NORMAL("없음"),
    CORRECTION("라식/라섹"),
    CATARACT("백내장"),
    ETC("기타");

    private final String label;

}