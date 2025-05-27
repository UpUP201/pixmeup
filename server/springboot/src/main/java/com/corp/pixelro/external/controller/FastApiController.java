package com.corp.pixelro.external.controller;

import java.util.UUID;

import com.corp.pixelro.external.dto.AredsResultResponse;
import com.corp.pixelro.external.dto.EmitterIdResponse;
import com.corp.pixelro.external.dto.ImagePredictionFrontResponse;
import com.corp.pixelro.external.dto.ImagePredictionResponse;
import com.corp.pixelro.external.dto.ImageUrlPredictionRequest;
import com.corp.pixelro.external.service.AsyncPredictService;
import com.corp.pixelro.external.service.FastApiService;
import com.corp.pixelro.external.service.SseEmitterService;
import com.corp.pixelro.external.util.EmitterUtil;
import com.corp.pixelro.global.error.code.ErrorCode;
import com.corp.pixelro.global.error.dto.ErrorResponse;
import com.corp.pixelro.global.error.exception.UserException;
import com.corp.pixelro.global.response.GlobalResponse;
import com.corp.pixelro.global.vo.CustomUserDetails;
import com.corp.pixelro.user.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
@Tag(name = "FastAPI 연동 API", description = "AREDS 예측 및 이미지 기반 안질환 예측 결과를 FastAPI")
public class FastApiController {

    private final FastApiService fastApiService;
    private final SseEmitterService sseEmitterService;
    private final AsyncPredictService asyncPredictService;
    private final UserService userService;

    @Operation(
            summary = "AREDS 예측 요청",
            description = "문진 및 검사 결과를 기반으로 AREDS 위험도를 예측합니다. 비동기로 처리되며 SSE를 통해 결과를 수신합니다."
    )
    @ApiResponse(
        responseCode = "200",
        description = "조회 성공",
        content = @Content(
            mediaType = "application/json",
            schema = @Schema(implementation = EmitterIdResponse.class),
            examples = @ExampleObject(value = """
            {
              "status": 200,
              "message": "Success",
              "data": {
                "emitterId": "123_f9c6a810-403a-46e7-95cf-cba520b57ba6"
              },
              "timestamp": "2025-05-09T12:00:00"
            }
            """)
        )
    )
    @PostMapping("/diagnosis/request")
    public ResponseEntity<GlobalResponse<EmitterIdResponse>> predictAreds(@AuthenticationPrincipal CustomUserDetails userDetail) {
        Long userId = userDetail.getUserId();
        String emitterId = EmitterUtil.generateEmitterIdByUserId(userId);
        asyncPredictService.asyncPredictAreds(userId, emitterId);
        return ResponseEntity.ok().body(GlobalResponse.success(EmitterIdResponse.builder().emitterId(emitterId).build()));
    }

    @Operation(summary = "AREDS 예측 결과 조회", description = "예측 결과 ID를 통해 AREDS 예측 결과를 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = AredsResultResponse.class),
                    examples = @ExampleObject(value = """
            {
              "status": 200,
              "message": "Success",
              "data": {
                "id": "66410e85f6ef23cde8b40192",
                "userId": 42,
                "riskPercent": 65,
                "summary": "중간 위험군입니다.",
                "risk": "Medium",
                "createdAt": "2024-05-08T10:00:00",
                "mChartCheckId": 123,
                "amslerCheckId": 456,
                "surveyId": 789
              },
              "timestamp": "2025-05-09T12:00:00"
            }
            """)
            )
    )
    @GetMapping("/diagnosis/result/{resultId}")
    public ResponseEntity<GlobalResponse<AredsResultResponse>> getAredsResult(
            @Parameter(description = "AREDS 예측 결과 ID", example = "66410e85f6ef23cde8b40192")
            @PathVariable String resultId,
        @AuthenticationPrincipal CustomUserDetails userDetail
    ) {
        AredsResultResponse result = fastApiService.getAredsResultById(resultId);
        String userName = userService.getUser(userDetail.getUserId()).getName();
        AredsResultResponse response = new AredsResultResponse(
            result.id(), result.userId(), result.riskPercent(), result.summary(), result.risk(), result.createdAt(),
            result.mChartCheckId(), result.amslerCheckId(), result.surveyId(), userName
        );
        return ResponseEntity.ok().body(GlobalResponse.success(response));
    }

    @Operation(
            summary = "이미지 기반 예측 요청",
            description = "S3 Key를 FastAPI에 전달하여 예측을 요청합니다. 결과는 SSE로 전송됩니다."
    )
    @ApiResponses({
            @ApiResponse(
                responseCode = "200",
                description = "조회 성공",
                content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = EmitterIdResponse.class),
                    examples = @ExampleObject(value = """
            {
              "status": 200,
              "message": "Success",
              "data": {
                "emitterId": "123_f9c6a810-403a-46e7-95cf-cba520b57ba6",
                "userName": "김종명"
              },
              "timestamp": "2025-05-09T12:00:00"
            }
            """)
                )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "요청 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping(value = "/image/request")
    public ResponseEntity<GlobalResponse<EmitterIdResponse>> predictImage(
            @RequestBody ImageUrlPredictionRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetail
    ) {
        Long userId = userDetail.getUserId();
        String emitterId = EmitterUtil.generateEmitterIdByUserId(userId);
        asyncPredictService.asyncPredictImage(request.s3Key(), userId, emitterId);
        return ResponseEntity.ok(GlobalResponse.success(EmitterIdResponse.builder().emitterId(emitterId).build()));
    }

    @Operation(
            summary = "이미지 예측 결과 조회",
            description = "예측 결과 ID로 이미지 예측 결과를 조회합니다."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ImagePredictionFrontResponse.class),
                            examples = @ExampleObject(value = """
                {
                  "status": 200,
                  "message": "Success",
                  "data": {
                    "id": "66411018f6ef23cde8b401ae",
                    "userId": 42,
                    "summary": "이상 없음",
                    "description": "질병이 감지되지 않았습니다.",
                    "imageUrl": "https://s3-url/image.png",
                    "createdAt": "2024-05-08T10:00:00"
                  },
                  "timestamp": "2025-05-09T12:00:00"
                }
            """)
                    )
            )
    })
    @GetMapping("/image/result/{resultId}")
    public ResponseEntity<GlobalResponse<ImagePredictionFrontResponse>> getImageResult(
            @Parameter(description = "이미지 예측 결과 ID", example = "66411018f6ef23cde8b401ae")
            @PathVariable String resultId
    ) {
        ImagePredictionResponse result = fastApiService.getImagePredictionById(resultId);
        ImagePredictionFrontResponse response = fastApiService.getImagePredictionFrontResponse(result);
        return ResponseEntity.ok().body(GlobalResponse.success(response));
    }

    @Operation(summary = "전체 예측 결과 조회", description = "AREDS 및 이미지 기반 예측 결과를 통합하여 최신 순으로 조회합니다.")
    @ApiResponse(
            responseCode = "200",
            description = "조회 성공",
            content = @Content(
                    mediaType = "application/json",
                    examples = @ExampleObject(value = """
            {
              "status": 200,
              "message": "Success",
              "data": [
                {
                  "id": "66411018f6ef23cde8b401ae",
                  "type": "IMAGE",
                  "createdAt": "2025-05-09T10:00:00"
                }
              ],
              "timestamp": "2025-05-09T12:00:00"
            }
            """)
            )
    )
    @GetMapping("/total-diagnosis")
    public ResponseEntity<GlobalResponse<?>> getPredictionRecords(
            @AuthenticationPrincipal CustomUserDetails userDetail,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        return ResponseEntity.ok().body(GlobalResponse.success(fastApiService.getPredictionRecords(userDetail.getUserId(), page, size)));
    }

    @Operation(
            summary = "AREDS 예측 결과 실시간 구독",
            description = "SSE를 통해 AREDS 예측 결과를 실시간으로 수신합니다."
    )
//    @ApiResponse(
//            responseCode = "200",
//            description = "SSE 스트림 연결 성공",
//            content = @Content(mediaType = "text/event-stream")
//    )
//    @GetMapping(value = "/diagnosis/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public SseEmitter subscribeAreds(@AuthenticationPrincipal CustomUserDetails userDetail) {
//        return sseEmitterService.createEmitter(userDetail.getUserId());
//    }
    @GetMapping(value = "/diagnosis/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeAreds(
        @RequestParam String emitterId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new UserException(ErrorCode.UNAUTHORIZED); // 필요 시 커스텀 에러로
        }

        CustomUserDetails userDetail = (CustomUserDetails) authentication.getPrincipal();

        Long emitterUserId = EmitterUtil.getUserIdFromEmitterId(emitterId);
        Long currentUserId = userDetail.getUserId();

        if(!emitterUserId.equals(currentUserId)) {
            throw new UserException(ErrorCode.IVALID_SSE_USER);
        }

        return sseEmitterService.createEmitter(emitterId);
    }

    @Operation(
            summary = "이미지 예측 결과 실시간 구독",
            description = "SSE를 통해 이미지 예측 결과를 실시간으로 수신합니다."
    )
//    @ApiResponse(
//            responseCode = "200",
//            description = "SSE 스트림 연결 성공",
//            content = @Content(mediaType = "text/event-stream")
//    )
//    @GetMapping(value = "/image/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
//    public SseEmitter subscribeImage(@AuthenticationPrincipal CustomUserDetails userDetail) {
//        return sseEmitterService.createEmitter(userDetail.getUserId());
//    }
    @GetMapping(value = "/image/subscribe", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public SseEmitter subscribeImage(
        @RequestParam String emitterId
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !(authentication.getPrincipal() instanceof CustomUserDetails)) {
            throw new UserException(ErrorCode.UNAUTHORIZED); // 필요 시 커스텀 에러로
        }
        CustomUserDetails userDetail = (CustomUserDetails) authentication.getPrincipal();

        Long emitterUserId = EmitterUtil.getUserIdFromEmitterId(emitterId);
        Long currentUserId = userDetail.getUserId();

        if(!emitterUserId.equals(currentUserId)) {
            throw new UserException(ErrorCode.IVALID_SSE_USER);
        }

        return sseEmitterService.createEmitter(emitterId);
    }
}
