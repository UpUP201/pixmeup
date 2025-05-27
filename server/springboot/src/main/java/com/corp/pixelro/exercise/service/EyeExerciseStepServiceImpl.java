package com.corp.pixelro.exercise.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.corp.pixelro.exercise.dto.EyeExerciseListResponse;
import com.corp.pixelro.exercise.dto.EyeExerciseRecommendResponse;
import com.corp.pixelro.exercise.entity.EyeExercise;
import com.corp.pixelro.exercise.entity.EyeExerciseStep;
import com.corp.pixelro.exercise.repository.EyeExerciseStepRepository;
import com.corp.pixelro.global.error.code.ErrorCode;
import com.corp.pixelro.global.error.exception.EyeExerciseException;
import com.corp.pixelro.global.error.exception.EyeExerciseStepException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EyeExerciseStepServiceImpl implements EyeExerciseStepService {

	private final EyeExerciseStepRepository eyeExerciseStepRepository;

	@Override
	public List<EyeExerciseStep> selectAllExerciseStepByExercise(EyeExercise eyeExercise) {

		if(eyeExercise == null) {
			throw new EyeExerciseStepException(ErrorCode.EYE_EXERCISE_NOT_FOUND);
		}

		List<EyeExerciseStep> eyeExerciseSteps = eyeExerciseStepRepository.findByEyeExercise(eyeExercise);
		if (eyeExerciseSteps.isEmpty()) {
			throw new EyeExerciseStepException(ErrorCode.EYE_EXERCISE_STEP_NOT_FOUND);
		}

		return eyeExerciseSteps;
	}
}
