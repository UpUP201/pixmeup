package com.corp.pixelro.exercise.dto;

import java.util.List;

import com.corp.pixelro.exercise.entity.EyeExercise;
import com.corp.pixelro.exercise.entity.EyeExerciseStep;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record EyeExerciseStartResponse(
	@Schema(description = "눈 운동 ID", example = "1")
	Long eyeExerciseId,

	@Schema(description = "눈 운동 이름", example = "눈 깜빡이기")
	String eyeExerciseName,

	@Schema(description = "썸네일 이미지 URL", example = "https://cdn.example.com/images/thumb1.jpg")
	String thumbnailUrl,

	@Schema(description = "눈 운동 스텝의 총 개수", example = "2")
	Integer totalSteps,

	@Schema(description = "눈 운동 단계 리스트")
	List<EyeExerciseStepList> eyeExerciseStepList
) {
	public static EyeExerciseStartResponse of(EyeExercise eyeExercise, List<EyeExerciseStepList> eyeExerciseStepList, String imgUrl) {
		return EyeExerciseStartResponse.builder()
			.eyeExerciseId(eyeExercise.getId())
			.eyeExerciseName(eyeExercise.getName())
			.thumbnailUrl(imgUrl)
			.totalSteps(eyeExercise.getTotalSteps())
			.eyeExerciseStepList(eyeExerciseStepList)
			.build();
	}

	@Builder
	public record EyeExerciseStepList(
		@Schema(description = "눈 운동 스텝 번호", example = "1")
		Integer stepOrder,

		@Schema(description = "눈 운동 스텝 제목", example = "정면 응시 후 눈 감기")
		String title,

		@Schema(description = "눈 운동 스텝 설명", example = "정면을 바라본 후, 천천히 눈을 감아주세요.")
		String instruction,

		@Schema(description = "스텝 소요 시간(초)", example = "90")
		Integer stepDuration,

		@Schema(description = "스텝 이미지 URL", example = "https://cdn.example.com/images/step1.jpg")
		String stepImageUrl,

		@Schema(description = "스텝 TTS 오디오 URL", example = "https://cdn.example.com/tts/step1.mp3")
		String stepTtsUrl
	) {
		public static EyeExerciseStepList of(EyeExerciseStep entity, String imgUrl, String ttsUrl) {
			return EyeExerciseStepList.builder()
				.stepOrder(entity.getStepOrder())
				.title(entity.getTitle())
				.instruction(entity.getInstruction())
				.stepDuration(entity.getStepDuration())
				.stepImageUrl(imgUrl)
				.stepTtsUrl(ttsUrl)
				.build();
		}
	}
}