package com.corp.pixelro.survey.controller;

import com.corp.pixelro.global.response.GlobalResponse;
import com.corp.pixelro.global.vo.CustomUserDetails;
import com.corp.pixelro.global.vo.UserDetail;
import com.corp.pixelro.survey.dto.SurveyRequest;
import com.corp.pixelro.survey.dto.SurveyResponse;
import com.corp.pixelro.survey.service.SurveyService;
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
public class SurveyController {

    private final SurveyService surveyService;

    @PostMapping("")
    public ResponseEntity<GlobalResponse<SurveyResponse>> sendSurveyData(
            @RequestBody SurveyRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetail
    ) {

        SurveyResponse response = surveyService.sendSurveyData(request, userDetail.getUserId());

        return ResponseEntity.ok()
                .body(GlobalResponse.success(response));
    }

}
