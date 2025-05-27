package com.corp.pixelro.exercise.dto;

import java.util.List;

import com.corp.pixelro.exercise.entity.EyeExercise;
import com.corp.pixelro.exercise.entity.EyeExerciseRecord;
import com.corp.pixelro.exercise.entity.EyeExerciseStep;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

@Builder
public record EyeExerciseRecordListResponse(
	@Schema(description = "유저 이름", example = "김쫑명")
	String userName,

	@Schema(description = "눈 운동 기록의 정보를 담고 있는 리스트")
	List<EyeExerciseRecordList> eyeExerciseRecordList
) {
	public static EyeExerciseRecordListResponse of(String userName, List<EyeExerciseRecordList> recordList) {
		return EyeExerciseRecordListResponse.builder()
			.userName(userName)
			.eyeExerciseRecordList(recordList)
			.build();
	}

	@Builder
	public record EyeExerciseRecordList(
		@Schema(description = "눈 운동 기록 ID", example = "1")
		Long eyeExerciseRecordId,

		@Schema(description = "눈 운동 ID", example = "3")
		Long eyeExerciseId,

		@Schema(description = "눈 운동 이름", example = "눈 깜빡이기 운동")
		String eyeExerciseName,

		@Schema(description = "총 소요 시간(초)", example = "180")
		Integer totalDuration,

		@Schema(description = "썸네일 이미지 URL", example = "https://cdn.example.com/images/step1.jpg")
		String thumbnailUrl
	) {
		public static EyeExerciseRecordList of(EyeExerciseRecord record, String imgUrl) {
			return EyeExerciseRecordList.builder()
				.eyeExerciseRecordId(record.getId())
				.eyeExerciseId(record.getEyeExercise().getId())
				.eyeExerciseName(record.getEyeExercise().getName())
				.totalDuration(record.getEyeExercise().getTotalDuration())
				.thumbnailUrl(imgUrl)
				.build();
		}
	}
}
