package com.corp.pixmeup.global.error.exception;

import com.corp.pixmeup.global.error.code.ErrorCode;

import lombok.Getter;

@Getter
public class EyeExerciseStepException extends BusinessException {
	public EyeExerciseStepException(ErrorCode errorCode) {
		super(errorCode);
	}
}