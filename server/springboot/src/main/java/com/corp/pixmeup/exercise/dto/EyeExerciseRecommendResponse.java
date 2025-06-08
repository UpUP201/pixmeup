package com.corp.pixmeup.exercise.dto;

import com.corp.pixmeup.exercise.entity.EyeExercise;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record EyeExerciseRecommendResponse(
	@Schema(description = "눈 운동 ID", example = "1")
	Long eyeExerciseId,

	@Schema(description = "눈 운동 이름", example = "눈 깜빡이기 운동")
	String eyeExerciseName,

	@Schema(description = "유저 이름", example = "김쫑명")
	String userName
) {
	public static EyeExerciseRecommendResponse of(String userName, EyeExercise exercise) {
		return EyeExerciseRecommendResponse.builder()
			.eyeExerciseId(exercise.getId())
			.eyeExerciseName(exercise.getName())
			.userName(userName)
			.build();
	}
}
