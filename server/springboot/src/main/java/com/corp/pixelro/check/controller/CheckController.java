package com.corp.pixelro.check.controller;

import com.corp.pixelro.check.dto.*;
import com.corp.pixelro.check.service.*;
import com.corp.pixelro.global.error.code.ErrorCode;
import com.corp.pixelro.global.error.dto.ErrorResponse;
import com.corp.pixelro.global.error.exception.EyeCheckSightException;
import com.corp.pixelro.global.response.GlobalResponse;
import com.corp.pixelro.global.vo.CustomUserDetails;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Slice;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.DateTimeException;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/v1/reports")
@RequiredArgsConstructor
@Tag(name = "검사 API", description = "검사 관련 API")
@Slf4j
public class CheckController {

    private final CheckService checkService;
    private final VisionService visionService;
    private final SightDataService sightDataService;
    private final PresbyopiaDataService presbyopiaDataService;
    private final AmslerDataService amslerDataService;
    private final MChartDataService mChartDataService;

    @Operation(summary = "검사 리포트 용 시력 & 안구 나이", description = "선택한 날짜 기준으로 가장 최신의 결과를 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = VisionResponse.class),
                            examples = @ExampleObject(value = """
                {
                  "status": 200,
                  "message": "Success",
                  "data": {
                    "name": "홍길동",
                    "age": 65,
                    "leftSight": 7,
                    "rightSight": 8,
                    "createdAt": "2025-05-09T00:00:00"
                  },
                  "timestamp": "2025-05-09T12:00:00"
                }
            """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "시력 검사 결과 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                {
                  "timestamp": "2025-05-09T12:00:00",
                  "name": "SIGHT_CHECK_NOT_FOUND",
                  "status": 404,
                  "message": "시력검사 결과가 존재하지 않습니다."
                }
            """)
                    )
            )
    })
    @GetMapping("/vision")
    public ResponseEntity<GlobalResponse<VisionResponse>> vision(
            @Parameter(
                    description = "기준 시각 (ISO-8601 형식)",
                    example = "2025-05-01T07:00:00"
            )
            @RequestParam
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
            LocalDateTime selectedDateTime,
            @AuthenticationPrincipal CustomUserDetails userDetail
    ) {
        VisionResponse response = visionService.selectVisionByDate(selectedDateTime, userDetail.getUserId());

        return ResponseEntity.ok()
                .body(GlobalResponse.success(response));
    }

    @Operation(summary = "검사 통합 리스트 조회", description = "사용자의 모든 검사 결과를 날짜 기준으로 통합하여 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CheckSummary.class),
                            examples = @ExampleObject(value = """
                {
                  "status": 200,
                  "message": "Success",
                  "data": [
                    {
                      "dateTime": "2025-05-09T00:00:00",
                      "hasSight": true,
                      "hasPresbyopia": false,
                      "hasAmsler": true,
                      "hasMChart": true
                    }
                  ],
                  "timestamp": "2025-05-09T12:00:00"
                }
            """)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                {
                  "timestamp": "2025-05-09T12:00:00",
                  "name": "INTERNAL_SERVER_ERROR",
                  "status": 500,
                  "message": "서버 오류가 발생했습니다"
                }
            """)
                    )
            )
    })
    @GetMapping("/total-list")
    public ResponseEntity<GlobalResponse<Slice<CheckSummary>>> selectTotalCheckList(
            @AuthenticationPrincipal CustomUserDetails userDetail,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Slice<CheckSummary> result = checkService.selectTotalCheckList(userDetail.getUserId(), page, size);
        return ResponseEntity.ok().body(GlobalResponse.success(result));
    }


    @Operation(summary = "근거리 시력 검사 상세", description = "해당 날짜의 시력 검사 기준으로 그래프 7개를 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = SightCheckResponse.class),
                            examples = @ExampleObject(value = """
                {
                  "status": 200,
                  "message": "Success",
                  "data": [
                    {
                      "id": 1,
                      "leftSight": 7,
                      "rightSight": 8,
                      "leftSightPrediction": 7,
                      "rightSightPrediction": 8,
                      "aiResult": "정상",
                      "status": "NORMAL",
                      "createdAt": "2025-05-09T10:00:00"
                    }
                  ],
                  "timestamp": "2025-05-09T12:00:00"
                }
            """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "결과 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                {
                  "timestamp": "2025-05-09T12:00:00",
                  "name": "SIGHT_CHECK_NOT_FOUND",
                  "status": 404,
                  "message": "시력검사 결과가 존재하지 않습니다."
                }
            """)
                    )
            )
    })
    @GetMapping("/myopia")
    public ResponseEntity<GlobalResponse<List<SightCheckResponse>>> selectSightDetail(
            @AuthenticationPrincipal CustomUserDetails userDetail,
            @Parameter(
                    description = "기준 시각 (ISO-8601 형식)",
                    example = "2025-05-01T07:00:00"
            )
            @RequestParam(required = false) String targetDateTime
    ) {
        LocalDateTime parsedDateTime = null;
        if (targetDateTime != null && !targetDateTime.isBlank()) {
            try {
                parsedDateTime = LocalDateTime.parse(targetDateTime);
            } catch (DateTimeException e) {
                throw new EyeCheckSightException(ErrorCode.INVALID_INPUT_VALUE);
            }
        }

        List<SightCheckResponse> result;
        if (parsedDateTime == null) {
            LocalDateTime latestDateTime = sightDataService.selectLatestSight(userDetail.getUserId());
            result = sightDataService.selectSightContextWithPrediction(userDetail.getUserId(), latestDateTime);
        } else {
            result = sightDataService.selectSightContextWithPrediction(userDetail.getUserId(), parsedDateTime);
        }
        return ResponseEntity.ok().body(GlobalResponse.success(result));
    }

    @Operation(summary = "노안 검사 상세", description = "해당 날짜의 노안 검사 기준으로 그래프 7개를 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = PresbyopiaCheckResponse.class),
                            examples = @ExampleObject(value = """
                {
                  "status": 200,
                  "message": "Success",
                  "data": [
                    {
                      "id": 1,
                      "age": 64,
                      "agePrediction": 68,
                      "aiResult": "노안 의심",
                      "status": "WARNING",
                      "createdAt": "2025-05-09T10:00:00"
                    }
                  ],
                  "timestamp": "2025-05-09T12:00:00"
                }
            """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "결과 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                {
                  "timestamp": "2025-05-09T12:00:00",
                  "name": "PRESBYOPIA_CHECK_NOT_FOUND",
                  "status": 404,
                  "message": "노안검사 결과가 존재하지 않습니다."
                }
            """)
                    )
            )
    })
    @GetMapping("/presbyopia")
    public ResponseEntity<GlobalResponse<List<PresbyopiaCheckResponse>>> selectPresbyopiaDetail(
            @AuthenticationPrincipal CustomUserDetails userDetail,
            @Parameter(
                    description = "기준 시각 (ISO-8601 형식)",
                    example = "2025-05-01T07:00:00"
            )
            @RequestParam(required = false) String targetDateTime
    ) {
        LocalDateTime parsedDateTime = null;
        if (targetDateTime != null) {
            try {
                parsedDateTime = LocalDateTime.parse(targetDateTime);
            } catch (DateTimeException e) {
                throw new EyeCheckSightException(ErrorCode.INVALID_INPUT_VALUE);
            }
        }

        List<PresbyopiaCheckResponse> result;

        if (parsedDateTime == null || parsedDateTime.toString().isBlank()) {
            LocalDateTime latestDateTime = presbyopiaDataService.selectLatestPresbyopia(userDetail.getUserId());
            result = presbyopiaDataService.selectPresbyopiaContextWithPrediction(userDetail.getUserId(), latestDateTime);
        } else {
            result = presbyopiaDataService.selectPresbyopiaContextWithPrediction(userDetail.getUserId(), parsedDateTime);
        }

        return ResponseEntity.ok().body(GlobalResponse.success(result));
    }

    @Operation(summary = "황반변성 검사 상세", description = "해당 날짜의 엠차트&암슬러 결과를 조회합니다.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = AmdCheckDetailResponse.class),
                            examples = @ExampleObject(value = """
                {
                  "status": 200,
                  "message": "Success",
                  "data": {
                    "amsler": {
                      "id": 1,
                      "rightMacularLoc": "n,n,n,w,n,n,d,b,n",
                      "leftMacularLoc": "n,n,n,w,n,n,d,b,n",
                      "aiResult": "황반변성 의심",
                      "createdAt": "2025-05-09T10:00:00"
                    },
                    "mchart": {
                      "id": 2,
                      "leftEyeVer": 2,
                      "rightEyeVer": 3,
                      "leftEyeHor": 1,
                      "rightEyeHor": 2,
                      "aiResult": "정상",
                      "createdAt": "2025-05-09T10:00:00"
                    }
                  },
                  "timestamp": "2025-05-09T12:00:00"
                }
            """)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "결과 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class),
                            examples = @ExampleObject(value = """
                {
                  "timestamp": "2025-05-09T12:00:00",
                  "name": "AMSLER_CHECK_NOT_FOUND",
                  "status": 404,
                  "message": "황반변성 검사 결과가 존재하지 않습니다."
                }
            """)
                    )
            )
    })
    @GetMapping("/macular")
    public ResponseEntity<GlobalResponse<AmdCheckDetailResponse>> selectAmdCheckDetail(
            @AuthenticationPrincipal CustomUserDetails userDetail,
            @Parameter(
                    description = "기준 시각 (ISO-8601 형식)",
                    example = "2025-05-01T07:00:00"
            )
            @RequestParam(required = false) String targetDateTime
    ) {
        LocalDateTime parsedDateTime = null;
        if (targetDateTime != null) {
            try {
                parsedDateTime = LocalDateTime.parse(targetDateTime);
            } catch (DateTimeException e) {
                throw new EyeCheckSightException(ErrorCode.INVALID_INPUT_VALUE);
            }
        }

        AmdCheckDetailResponse result;
        if (parsedDateTime == null || parsedDateTime.toString().isBlank()) {
            LocalDateTime amslerLatestDateTime = amslerDataService.selectLatestAmsler(userDetail.getUserId());
            LocalDateTime mchartLatestDateTime = mChartDataService.selectLatestMChart(userDetail.getUserId());

            result = checkService.selectAmdCheckDetailByDifferentDate(userDetail.getUserId(), amslerLatestDateTime, mchartLatestDateTime);
        } else {
            result = checkService.selectAmdCheckDetail(userDetail.getUserId(), parsedDateTime);
        }
        return ResponseEntity.ok().body(GlobalResponse.success(result));
    }

    @PostMapping("/qr")
    public ResponseEntity<GlobalResponse<String>> createData(
            @RequestBody QrRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetail
    ) throws JsonProcessingException {
        checkService.createData(request, userDetail.getUserId());
        return ResponseEntity.ok().body(GlobalResponse.success("정보가 정상적으로 저장되었습니다."));
    }

}
