package com.corp.pixelro.exercise.service;

import java.util.List;

import com.corp.pixelro.exercise.entity.EyeExercise;
import com.corp.pixelro.exercise.entity.EyeExerciseStep;

public interface EyeExerciseStepService {

	List<EyeExerciseStep> selectAllExerciseStepByExercise(EyeExercise eyeExercise);
}
