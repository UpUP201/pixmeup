package com.corp.pixmeup.exercise.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.corp.pixmeup.exercise.dto.EyeExerciseDetailResponse;
import com.corp.pixmeup.exercise.dto.EyeExerciseListResponse;
import com.corp.pixmeup.exercise.dto.EyeExerciseRecommendResponse;
import com.corp.pixmeup.exercise.dto.EyeExerciseStartResponse;
import com.corp.pixmeup.exercise.entity.EyeExercise;
import com.corp.pixmeup.exercise.entity.EyeExerciseStep;
import com.corp.pixmeup.exercise.repository.EyeExerciseRepository;
import com.corp.pixmeup.external.service.S3Service;
import com.corp.pixmeup.global.error.code.ErrorCode;
import com.corp.pixmeup.global.error.exception.EyeExerciseException;
import com.corp.pixmeup.user.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class EyeExerciseServiceImpl implements EyeExerciseService {

	private final EyeExerciseRepository eyeExerciseRepository;
	private final UserService userService;
	private final EyeExerciseStepService eyeExerciseStepService;
	private final S3Service s3Service;

	@Override
	public List<EyeExercise> selectAllExercises() {
		List<EyeExercise> exercises = eyeExerciseRepository.findAll();
		if(exercises.isEmpty()) {
			throw new EyeExerciseException(ErrorCode.EYE_EXERCISE_NOT_FOUND);
		}
		return exercises;
	}

	@Override
	public EyeExercise selectExerciseById(Long id) {
		return eyeExerciseRepository.findById(id)
			.orElseThrow(() -> new EyeExerciseException(ErrorCode.EYE_EXERCISE_NOT_FOUND));
	}

	@Override
	public List<EyeExerciseListResponse> selectAllExercisesList() {
		return selectAllExercises()
			.stream()
			.map(EyeExerciseListResponse::of)
			.toList();
	}

	@Override
	public EyeExerciseRecommendResponse recommendExercise(Long userId) {
		String userName = userService.getUser(userId).getName();
		List<EyeExercise> lists = selectAllExercises();
		int size = lists.size();
		int day = LocalDate.now().getDayOfMonth();
		return EyeExerciseRecommendResponse.of(userName, lists.get(day%size));
	}

	@Override
	public EyeExerciseDetailResponse selectExerciseDetail(Long id) {
		EyeExercise eyeExercise = selectExerciseById(id);
		List<EyeExerciseStep> eyeExerciseSteps = eyeExerciseStepService.selectAllExerciseStepByExercise(eyeExercise);

		List<EyeExerciseDetailResponse.EyeExerciseStepList> stepList = eyeExerciseSteps.stream()
			.map(EyeExerciseDetailResponse.EyeExerciseStepList::of)
			.toList();

		String imgUrl = s3Service.generatePresignedGetUrl(eyeExercise.getThumbnailUrl());

		return EyeExerciseDetailResponse.of(eyeExercise, stepList, imgUrl);
	}

	@Override
	public EyeExerciseStartResponse selectExerciseStart(Long id) {
		EyeExercise eyeExercise = selectExerciseById(id);
		List<EyeExerciseStep> eyeExerciseSteps = eyeExerciseStepService.selectAllExerciseStepByExercise(eyeExercise);
		String imgUrl = s3Service.generatePresignedGetUrl(eyeExercise.getThumbnailUrl());

		List<EyeExerciseStartResponse.EyeExerciseStepList> stepList = eyeExerciseSteps.stream()
			.map(step -> EyeExerciseStartResponse.EyeExerciseStepList.of(
				step,
				s3Service.generatePresignedGetUrl(step.getStepImageUrl()),
				s3Service.generatePresignedGetUrl(step.getStepTtsUrl())
			))
			.toList();

		return EyeExerciseStartResponse.of(eyeExercise, stepList, imgUrl);
	}
}
