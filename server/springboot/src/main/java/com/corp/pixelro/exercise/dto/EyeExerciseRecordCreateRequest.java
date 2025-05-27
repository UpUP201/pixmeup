package com.corp.pixelro.exercise.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record EyeExerciseRecordCreateRequest(

	@Schema(description = "눈 운동 ID", example = "10")
	@NotNull
	Long eyeExerciseId

) { }
