package com.corp.pixelro.global.error.exception;

import com.corp.pixelro.global.error.code.ErrorCode;

import lombok.Getter;

@Getter
public class EyeExerciseStepException extends BusinessException {
	public EyeExerciseStepException(ErrorCode errorCode) {
		super(errorCode);
	}
}