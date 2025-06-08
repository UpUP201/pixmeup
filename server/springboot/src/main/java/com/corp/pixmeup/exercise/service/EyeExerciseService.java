package com.corp.pixmeup.exercise.service;

import java.util.List;

import com.corp.pixmeup.exercise.dto.EyeExerciseDetailResponse;
import com.corp.pixmeup.exercise.dto.EyeExerciseListResponse;
import com.corp.pixmeup.exercise.dto.EyeExerciseRecommendResponse;
import com.corp.pixmeup.exercise.dto.EyeExerciseStartResponse;
import com.corp.pixmeup.exercise.entity.EyeExercise;

public interface EyeExerciseService {

	List<EyeExercise> selectAllExercises();

	EyeExercise selectExerciseById(Long id);

	List<EyeExerciseListResponse> selectAllExercisesList();

	EyeExerciseRecommendResponse recommendExercise(Long userId);

	EyeExerciseDetailResponse selectExerciseDetail(Long id);

	EyeExerciseStartResponse selectExerciseStart(Long id);

}
