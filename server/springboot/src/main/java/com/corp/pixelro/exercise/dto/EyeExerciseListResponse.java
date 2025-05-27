package com.corp.pixelro.exercise.dto;

import com.corp.pixelro.exercise.entity.EyeExercise;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record EyeExerciseListResponse(
	@Schema(description = "눈 운동 ID", example = "1")
	Long eyeExerciseId,

	@Schema(description = "눈 운동 이름", example = "눈 깜빡이기 운동")
	String eyeExerciseName,

	@Schema(description = "총 소요 시간(초)", example = "180")
	Integer totalDuration
) {
	public static EyeExerciseListResponse of(EyeExercise exercise) {
		return EyeExerciseListResponse.builder()
			.eyeExerciseId(exercise.getId())
			.eyeExerciseName(exercise.getName())
			.totalDuration(exercise.getTotalDuration())
			.build();
	}
}
