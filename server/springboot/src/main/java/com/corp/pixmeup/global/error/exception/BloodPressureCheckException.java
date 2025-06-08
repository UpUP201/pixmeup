package com.corp.pixmeup.global.error.exception;

import com.corp.pixmeup.global.error.code.ErrorCode;

import lombok.Getter;

@Getter
public class BloodPressureCheckException extends BusinessException {
	public BloodPressureCheckException(ErrorCode errorCode) {
		super(errorCode);
	}
}