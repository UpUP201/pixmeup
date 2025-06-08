package com.corp.pixmeup.global.error.exception;

import com.corp.pixmeup.global.error.code.ErrorCode;

import lombok.Getter;

@Getter
public class SurveyException extends BusinessException {
	public SurveyException(ErrorCode errorCode) {
		super(errorCode);
	}
}