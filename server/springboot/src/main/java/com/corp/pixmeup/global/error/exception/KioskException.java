package com.corp.pixmeup.global.error.exception;

import com.corp.pixmeup.global.error.code.ErrorCode;

import lombok.Getter;

@Getter
public class KioskException extends BusinessException {
	public KioskException(ErrorCode errorCode) {
		super(errorCode);
	}
}