package com.corp.pixmeup.exercise.service;

import java.util.List;

import com.corp.pixmeup.exercise.dto.EyeExerciseRecordCreateRequest;
import com.corp.pixmeup.exercise.dto.EyeExerciseRecordListResponse;
import com.corp.pixmeup.exercise.entity.EyeExerciseRecord;

public interface EyeExerciseRecordService {

	List<EyeExerciseRecord> selectAllRecordByUser(Long userId);

	EyeExerciseRecord selectRecordById(Long id);

	EyeExerciseRecordListResponse selectRecordListByUser(Long userId);

	Long createRecord(Long userId, EyeExerciseRecordCreateRequest request);

}
