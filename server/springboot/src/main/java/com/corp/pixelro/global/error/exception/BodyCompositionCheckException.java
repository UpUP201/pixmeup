package com.corp.pixelro.global.error.exception;

import com.corp.pixelro.global.error.code.ErrorCode;

import lombok.Getter;

@Getter
public class BodyCompositionCheckException extends BusinessException {
	public BodyCompositionCheckException(ErrorCode errorCode) {
		super(errorCode);
	}
}