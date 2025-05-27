package com.corp.pixelro.exercise.service;

import java.util.List;

import com.corp.pixelro.exercise.dto.EyeExerciseDetailResponse;
import com.corp.pixelro.exercise.dto.EyeExerciseListResponse;
import com.corp.pixelro.exercise.dto.EyeExerciseRecommendResponse;
import com.corp.pixelro.exercise.dto.EyeExerciseStartResponse;
import com.corp.pixelro.exercise.entity.EyeExercise;

public interface EyeExerciseService {

	List<EyeExercise> selectAllExercises();

	EyeExercise selectExerciseById(Long id);

	List<EyeExerciseListResponse> selectAllExercisesList();

	EyeExerciseRecommendResponse recommendExercise(Long userId);

	EyeExerciseDetailResponse selectExerciseDetail(Long id);

	EyeExerciseStartResponse selectExerciseStart(Long id);

}
