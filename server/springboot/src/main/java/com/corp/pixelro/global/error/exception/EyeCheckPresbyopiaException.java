package com.corp.pixelro.global.error.exception;

import com.corp.pixelro.global.error.code.ErrorCode;

import lombok.Getter;

@Getter
public class EyeCheckPresbyopiaException extends BusinessException {
	public EyeCheckPresbyopiaException(ErrorCode errorCode) {
		super(errorCode);
	}
}