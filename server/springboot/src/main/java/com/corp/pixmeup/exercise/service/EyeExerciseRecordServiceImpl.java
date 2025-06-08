package com.corp.pixmeup.exercise.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.corp.pixmeup.exercise.dto.EyeExerciseRecordCreateRequest;
import com.corp.pixmeup.exercise.dto.EyeExerciseRecordListResponse;
import com.corp.pixmeup.exercise.entity.EyeExercise;
import com.corp.pixmeup.exercise.entity.EyeExerciseRecord;
import com.corp.pixmeup.exercise.repository.EyeExerciseRecordRepository;
import com.corp.pixmeup.external.service.S3Service;
import com.corp.pixmeup.global.error.code.ErrorCode;
import com.corp.pixmeup.global.error.exception.EyeExerciseRecordException;
import com.corp.pixmeup.user.entity.User;
import com.corp.pixmeup.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EyeExerciseRecordServiceImpl implements EyeExerciseRecordService {

	private final EyeExerciseRecordRepository eyeExerciseRecordRepository;
	private final UserService userService;
	private final EyeExerciseService eyeExerciseService;
	private final S3Service s3Service;

	@Override
	public List<EyeExerciseRecord> selectAllRecordByUser(Long userId) {
		User user = userService.getUser(userId);
		return eyeExerciseRecordRepository.findByUser(user);
	}

	@Override
	public EyeExerciseRecord selectRecordById(Long id) {
		return eyeExerciseRecordRepository.findById(id)
			.orElseThrow(() -> new EyeExerciseRecordException(ErrorCode.EYE_EXERCISE_RECORD_NOT_FOUND));
	}

	@Override
	public EyeExerciseRecordListResponse selectRecordListByUser(Long userId) {
		User user = userService.getUser(userId);
		List<EyeExerciseRecord> eyeExerciseRecords = eyeExerciseRecordRepository.findByUser(user);

		List<EyeExerciseRecordListResponse.EyeExerciseRecordList> recordList = eyeExerciseRecords.stream()
			.map(record -> EyeExerciseRecordListResponse.EyeExerciseRecordList.of(record, s3Service.generatePresignedGetUrl(record.getEyeExercise().getThumbnailUrl())))
			.toList();

		return EyeExerciseRecordListResponse.of(user.getName(), recordList);
	}

	@Override
	@Transactional
	public Long createRecord(Long userId, EyeExerciseRecordCreateRequest request) {
		User user = userService.getUser(userId);
		EyeExercise eyeExercise = eyeExerciseService.selectExerciseById(request.eyeExerciseId());
		EyeExerciseRecord eyeExerciseRecord = EyeExerciseRecord.builder()
			.eyeExercise(eyeExercise)
			.user(user)
			.build();
		eyeExerciseRecordRepository.save(eyeExerciseRecord);
		return eyeExerciseRecord.getId();
	}
}
