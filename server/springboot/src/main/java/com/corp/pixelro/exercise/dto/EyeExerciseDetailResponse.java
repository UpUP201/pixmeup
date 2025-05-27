package com.corp.pixelro.exercise.dto;

import java.util.List;

import com.corp.pixelro.exercise.entity.EyeExercise;
import com.corp.pixelro.exercise.entity.EyeExerciseStep;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record EyeExerciseDetailResponse(
	@Schema(description = "눈 운동 ID", example = "1")
	Long eyeExerciseId,

	@Schema(description = "눈 운동 이름", example = "눈 깜빡이기 운동")
	String eyeExerciseName,

	@Schema(description = "눈 운동 요약", example = "안구 건조 없이 촉촉하게")
	String eyeExerciseSummary,

	@Schema(description = "눈 운동 설명", example = "눈 주위를 감싸는 근육을... 더보기")
	String description,

	@Schema(description = "총 소요 시간(초)", example = "180")
	Integer totalDuration,

	@Schema(description = "주의사항", example = "해당 운동을 진행할 때 이러한 주의를 부탁드립니다.")
	String precautions,

	@Schema(description = "안내사항", example = "해당 운동을 진행할 때 눈을 꽉 감지 않으면 더 좋아요")
	String guidelines,

	@Schema(description = "썸네일 이미지 URL", example = "https://cdn.example.com/images/step1.jpg")
	String thumbnailUrl,

	@Schema(description = "눈 운동 스텝의 총 개수", example = "5")
	Integer totalSteps,

	@Schema(description = "눈 운동 스텝의 정보를 담고 있는 리스트")
	List<EyeExerciseStepList> eyeExerciseStepList
) {
	public static EyeExerciseDetailResponse of(EyeExercise eyeExercise, List<EyeExerciseStepList> stepList, String imgUrl) {
		return EyeExerciseDetailResponse.builder()
			.eyeExerciseId(eyeExercise.getId())
			.eyeExerciseName(eyeExercise.getName())
			.eyeExerciseSummary(eyeExercise.getSummary())
			.description(eyeExercise.getDescription())
			.totalDuration(eyeExercise.getTotalDuration())
			.precautions(eyeExercise.getPrecautions())
			.guidelines(eyeExercise.getGuidelines())
			.thumbnailUrl(imgUrl)
			.totalSteps(eyeExercise.getTotalSteps())
			.eyeExerciseStepList(stepList)
			.build();
	}

	@Builder
	public record EyeExerciseStepList(
		@Schema(description = "스텝 순서", example = "1")
		Integer stepOrder,

		@Schema(description = "운동 스텝 제목", example = "정면 응시 후 눈 감기")
		String title,

		@Schema(description = "운동 스텝 설명", example = "정면을 바라본 후, 천천히 눈을 감아주세요.")
		String instruction ,

		@Schema(description = "스텝 소요 시간(초)", example = "90")
		Integer stepDuration
	) {
		public static EyeExerciseStepList of(EyeExerciseStep eyeExerciseStep) {
			return EyeExerciseStepList.builder()
				.stepOrder(eyeExerciseStep.getStepOrder())
				.title(eyeExerciseStep.getTitle())
				.instruction(eyeExerciseStep.getInstruction())
				.stepDuration(eyeExerciseStep.getStepDuration())
				.build();
		}
	}
}
