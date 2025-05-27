package com.corp.pixelro.global.error.exception;

import com.corp.pixelro.global.error.code.ErrorCode;

import lombok.Getter;

@Getter
public class EyeCheckAmslerException extends BusinessException {
	public EyeCheckAmslerException(ErrorCode errorCode) {
		super(errorCode);
	}
}