package com.corp.pixelro.exercise.controller;

import java.util.List;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.corp.pixelro.exercise.dto.EyeExerciseDetailResponse;
import com.corp.pixelro.exercise.dto.EyeExerciseListResponse;
import com.corp.pixelro.exercise.dto.EyeExerciseRecommendResponse;
import com.corp.pixelro.exercise.dto.EyeExerciseRecordCreateRequest;
import com.corp.pixelro.exercise.dto.EyeExerciseRecordListResponse;
import com.corp.pixelro.exercise.dto.EyeExerciseStartResponse;
import com.corp.pixelro.exercise.service.EyeExerciseRecordService;
import com.corp.pixelro.exercise.service.EyeExerciseService;
import com.corp.pixelro.global.error.dto.ErrorResponse;
import com.corp.pixelro.global.response.GlobalResponse;
import com.corp.pixelro.global.vo.CustomUserDetails;

@Tag(name = "눈 운동", description = "눈 운동 관련 API")
@RestController
@RequestMapping("/api/v1/exercises")
@RequiredArgsConstructor
public class EyeExerciseController {

	private final EyeExerciseService eyeExerciseService;
	private final EyeExerciseRecordService eyeExerciseRecordService;

	@Operation(summary = "눈 운동 전체 조회", description = "모든 눈 운동 목록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "눈 운동 전체 조회 성공",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					name = "FindSuccess",
					summary = "눈 운동 전체 조회 성공 예시",
					value = """
						{
						  "status": 200,
						  "message": "Success",
						  "data":
						    [
							    {
								    "eyeExerciseId": 1,
								    "eyeExerciseName": "눈 깜빡이기",
							      	"totalDuration": 90
							    },
							     {
								    "eyeExerciseId": 2,
								    "eyeExerciseName": "상하좌우 운동",
							      	"totalDuration": 120
							    },
							    {
								    "eyeExerciseId": 3,
								    "eyeExerciseName": "눈으로 그림 그리기",
							      	"totalDuration": 150
							    },
							    {
								    "eyeExerciseId": 4,
								    "eyeExerciseName": "원근법 운동",
							      	"totalDuration": 180
							    }
						    ],
						  "timestamp": "2025-05-07T12:10:00Z"
						}
					"""
				)
			)
		),
		@ApiResponse(responseCode = "404", description = "요청한 리소스를 찾을 수 없습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
	})
	@GetMapping("")
	public ResponseEntity<GlobalResponse<List<EyeExerciseListResponse>>> selectAllExercises() {
		List<EyeExerciseListResponse> response = eyeExerciseService.selectAllExercisesList();

		return ResponseEntity.ok().body(GlobalResponse.success(response));
	}

	@Operation(summary = "운동 추천", description = "사용자에게 맞는 눈 운동을 추천합니다.")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "눈 운동 추천 성공",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					name = "RecommendSuccess",
					summary = "눈 운동 추천 성공 예시",
					value = """
						{
						  "status": 200,
						  "message": "Success",
						  "data": {
							  "userName": "쫑쫑스",
						    "eyeExerciseId": 1,
							  "eyeExerciseName": "눈 깜빡이기"
						  },
						  "timestamp": "2025-05-07T12:10:00Z"
						}
					"""
				)
			)
		),
		@ApiResponse(responseCode = "404", description = "요청한 리소스를 찾을 수 없습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
	})
	@GetMapping("/recommend")
	public ResponseEntity<GlobalResponse<EyeExerciseRecommendResponse>> recommendExercise(
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		Long userId = userDetails.getUserId();
		EyeExerciseRecommendResponse response = eyeExerciseService.recommendExercise(userId);

		return ResponseEntity.ok().body(GlobalResponse.success(response));
	}

	@Operation(summary = "눈 운동 상세 조회", description = "해당 ID에 대한 눈 운동의 상세 정보를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "눈 운동 상세 조회 성공",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					name = "DetailSuccess",
					summary = "눈 운동 상세 조회 성공 예시",
					value = """
						{
						   "status": 200,
						   "message": "Success",
						   "data": {
						     "eyeExerciseId": 1,
						 	  "eyeExerciseName": "눈 깜빡이기",
						 	  "eyeExerciseSummary": "안구 건조 없이 촉촉하게",
						 	  "description": "눈 주위를 감싸는 근육을 ...더보기",
						 	  "totalDuration": 180,
						 	  "precautions": "저는 주의사항이에오",
						 	  "guidelines": "저는 안내사항이에오",
						 	  "thumbnailUrl": "https://cdn.example.com/images/step1.jpg",
						 	  "totalSteps" : 2,
						 	  "eyeExerciseStepList": [
						 		  {
						 			  "stepOrder": "1",
						 			  "title": "정면 응시 후 눈 감기",
						 			  "insturction": "정면을 바라본 후, 천천히 눈을 감아주세요.",
						 			  "stepDuration" : 90
						 		  },
						 		  {
						 			  "stepOrder": "2",
						 			  "title": "2초간 눈 감고 유지하기",
						 			  "insturction": "눈을 감은 상태에서 2초간 힘을 주어 유지합니다.",
						 			  "stepDuration" : 90
						 		  }
						 	  ]
						   },
						   "timestamp": "2025-05-07T12:10:00Z"
						 }
					"""
				)
			)
		),
		@ApiResponse(responseCode = "404", description = "요청한 리소스를 찾을 수 없습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
	})
	@GetMapping("/{id}")
	public ResponseEntity<GlobalResponse<EyeExerciseDetailResponse>> selectExerciseDetail(
		@PathVariable Long id
	) {
		EyeExerciseDetailResponse response = eyeExerciseService.selectExerciseDetail(id);

		return ResponseEntity.ok().body(GlobalResponse.success(response));
	}

	@Operation(summary = "운동 시작 단계 정보 조회", description = "눈 운동 시작 시 필요한 단계 정보를 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "운동 시작 단계 정보 성공",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					name = "StartSuccess",
					summary = "운동 시작 단계 정보 성공 예시",
					value = """
						{
						  "status": 200,
						  "message": "Success",
						  "data": {
						    "eyeExerciseId": 1,
							  "eyeExerciseName": "눈 깜빡이기",
							  "thumbnailUrl": "이거 뭐로갈까 로컬 or 외부저장 암튼 사진주소",
							  "totalSteps" : 2,
							  "eyeExerciseStepList": [
								  {
									  "stepOrder": 1,
									  "title": "정면 응시 후 눈 감기",
									  "instruction": "정면을 바라본 후, 천천히 눈을 감아주세요.",
									  "stepDuration" : 90,
									  "stepImageUrl": "https://cdn.example.com/images/step1.jpg",
									  "stepTtsUrl": "https://cdn.example.com/tts/step1.mp3"
								  },
								  {
									  "stepOrder": 2,
									  "title": "2초간 눈 감고 유지하기",
									  "instruction": "눈을 감은 상태에서 2초간 힘을 주어 유지합니다.",
									  "stepDuration" : 90,
									  "stepImageUrl": "https://cdn.example.com/images/step1.jpg",
									  "stepTtsUrl": "https://cdn.example.com/tts/step1.mp3"
								  }
							  ]
						  },
						  "timestamp": "2025-05-07T12:10:00Z"
						}
						"""
				)
			)
		),
		@ApiResponse(responseCode = "404", description = "요청한 리소스를 찾을 수 없습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.corp.pixelro.global.error.dto.ErrorResponse.class)))
	})
	@GetMapping("/{id}/start")
	public ResponseEntity<GlobalResponse<EyeExerciseStartResponse>> selectExerciseStart(
		@PathVariable Long id
	) {
		EyeExerciseStartResponse response = eyeExerciseService.selectExerciseStart(id);

		return ResponseEntity.ok().body(GlobalResponse.success(response));
	}

	@Operation(summary = "운동 완료 기록 저장", description = "운동을 완료한 기록을 저장합니다.")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "201",
			description = "운동 완료 기록 저장 성공",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					name = "SaveSuccess",
					summary = "운동 완료 기록 저장 성공 예시",
					value = """
						{
						   "status": 201,
						   "message": "Success",
						   "data": {
						     "eyeExerciseRecordId": 1
						   },
						   "timestamp": "2025-05-07T12:10:00Z"
						 }
					"""
				)
			)
		),
		@ApiResponse(responseCode = "404", description = "요청한 리소스를 찾을 수 없습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.corp.pixelro.global.error.dto.ErrorResponse.class)))
	})
	@PostMapping("/complete")
	public ResponseEntity<GlobalResponse<Long>> createRecord(
		@Valid @RequestBody EyeExerciseRecordCreateRequest request,
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		Long userId = userDetails.getUserId();
		Long recordId = eyeExerciseRecordService.createRecord(userId, request);

		return ResponseEntity.status(HttpStatus.CREATED).body(GlobalResponse.success(recordId));
	}

	@Operation(summary = "운동 기록 조회", description = "사용자의 눈 운동 기록을 조회합니다.")
	@ApiResponses(value = {
		@ApiResponse(
			responseCode = "200",
			description = "운동 기록 조회 성공",
			content = @Content(
				mediaType = "application/json",
				examples = @ExampleObject(
					name = "FindSuccess",
					summary = "운동 기록 조회 성공 예시",
					value = """
						{
						 "status": 200,
						 "message": "Success",
						 "data": {
						   "userName": "쫑쫑스",
						  "eyeExerciseRecordList": [
							  {
								  "eyeExerciseRecordId": 100,
								  "eyeExerciseId": 1,
								  "eyeExerciseName": "눈 깜빡이기",
								  "totalDuration": 90,
								  "thumbnailUrl": "이거 뭐로갈까 로컬 or 외부저장 암튼 사진주소"
							  },
							  {
								  "eyeExerciseRecordId": 101,
								  "eyeExerciseId": 2,
								  "eyeExerciseName": "상하좌우 운동",
								  "totalDuration": 120,
								  "thumbnailUrl": "이거 뭐로갈까 로컬 or 외부저장 암튼 사진주소"
							  }
						  ]
						 },
						 "timestamp": "2025-05-07T12:10:00Z"
						}
					"""
				)
			)
		),
		@ApiResponse(responseCode = "404", description = "요청한 리소스를 찾을 수 없습니다.", content = @Content(mediaType = "application/json", schema = @Schema(implementation = com.corp.pixelro.global.error.dto.ErrorResponse.class)))
	})
	@GetMapping("/record")
	public ResponseEntity<GlobalResponse<EyeExerciseRecordListResponse>> selectRecordListByUser(
		@AuthenticationPrincipal CustomUserDetails userDetails
	) {
		Long userId = userDetails.getUserId();
		EyeExerciseRecordListResponse response = eyeExerciseRecordService.selectRecordListByUser(userId);

		return ResponseEntity.ok().body(GlobalResponse.success(response));
	}

}
