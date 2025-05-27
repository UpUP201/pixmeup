package com.corp.pixelro.exercise.service;

import java.util.List;

import com.corp.pixelro.exercise.dto.EyeExerciseRecordCreateRequest;
import com.corp.pixelro.exercise.dto.EyeExerciseRecordListResponse;
import com.corp.pixelro.exercise.entity.EyeExerciseRecord;

public interface EyeExerciseRecordService {

	List<EyeExerciseRecord> selectAllRecordByUser(Long userId);

	EyeExerciseRecord selectRecordById(Long id);

	EyeExerciseRecordListResponse selectRecordListByUser(Long userId);

	Long createRecord(Long userId, EyeExerciseRecordCreateRequest request);

}
