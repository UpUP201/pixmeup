package com.corp.pixelro.global.error.exception;

import com.corp.pixelro.global.error.code.ErrorCode;

import lombok.Getter;

@Getter
public class SurveyException extends BusinessException {
	public SurveyException(ErrorCode errorCode) {
		super(errorCode);
	}
}