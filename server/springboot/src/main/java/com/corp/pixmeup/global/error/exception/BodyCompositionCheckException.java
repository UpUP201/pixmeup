package com.corp.pixmeup.global.error.exception;

import com.corp.pixmeup.global.error.code.ErrorCode;

import lombok.Getter;

@Getter
public class BodyCompositionCheckException extends BusinessException {
	public BodyCompositionCheckException(ErrorCode errorCode) {
		super(errorCode);
	}
}