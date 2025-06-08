package com.corp.pixmeup.global.error.exception;

import com.corp.pixmeup.global.error.code.ErrorCode;

import lombok.Getter;

@Getter
public class EyeExerciseException extends BusinessException {
	public EyeExerciseException(ErrorCode errorCode) {
		super(errorCode);
	}
}