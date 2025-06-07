package com.corp.pixelro.survey.controller;

import com.corp.pixelro.global.response.GlobalResponse;
import com.corp.pixelro.global.vo.CustomUserDetails;
import com.corp.pixelro.global.vo.UserDetail;
import com.corp.pixelro.survey.dto.SurveyRequest;
import com.corp.pixelro.survey.dto.SurveyResponse;
import com.corp.pixelro.survey.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/surveys")
@RequiredArgsConstructor
@Tag(name = "문진 API", description = "문진(Survey) 관련 API")
public class SurveyController {

    private final SurveyService surveyService;

    @Operation(
            summary = "문진 결과 저장",
            description = "다른 검사 결과와 별개로 문진 결과만 따로 저장."
    )
    @ApiResponse(
            responseCode = "200",
            description = "문진 결과 저장 성공",
            content = @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = SurveyResponse.class),
                    examples = @ExampleObject(value = """
                    {
                      "status": 200,
                      "message": "Success",
                      "data": {
                        "resultId": 101,
                        "userId": 42,
                        "age": 65,
                        "gender": "M",
                        "glasses": true,
                        "surgery": "CATARACT",
                        "diabetes": false,
                        "currentSmoking": true,
                        "pastSmoking": false
                      },
                      "timestamp": "2025-05-09T12:00:00"
                    }
                    """)
            )
    )
    @PostMapping("")
    public ResponseEntity<GlobalResponse<SurveyResponse>> sendSurveyData(
            @RequestBody
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "사용자의 문진 요청 데이터",
                    required = true,
                    content = @Content(schema = @Schema(implementation = SurveyRequest.class))
            )
            SurveyRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetail
    ) {

        SurveyResponse response = surveyService.sendSurveyData(request, userDetail.getUserId());

        return ResponseEntity.ok()
                .body(GlobalResponse.success(response));
    }

}
