package com.corp.pixelro.global.error.exception;

import com.corp.pixelro.global.error.code.ErrorCode;

import lombok.Getter;

@Getter
public class GripStrengthCheckException extends BusinessException {
	public GripStrengthCheckException(ErrorCode errorCode) {
		super(errorCode);
	}
}