package com.corp.pixelro.global.error.exception;

import com.corp.pixelro.global.error.code.ErrorCode;

import lombok.Getter;

@Getter
public class EyeCheckMchartException extends BusinessException {
	public EyeCheckMchartException(ErrorCode errorCode) {
		super(errorCode);
	}
}