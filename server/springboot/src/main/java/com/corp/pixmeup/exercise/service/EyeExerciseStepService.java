package com.corp.pixmeup.exercise.service;

import java.util.List;

import com.corp.pixmeup.exercise.entity.EyeExercise;
import com.corp.pixmeup.exercise.entity.EyeExerciseStep;

public interface EyeExerciseStepService {

	List<EyeExerciseStep> selectAllExerciseStepByExercise(EyeExercise eyeExercise);
}
