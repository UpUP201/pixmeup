package com.corp.pixmeup.external.service;

import com.corp.pixmeup.check.entity.AmslerCheck;
import com.corp.pixmeup.check.entity.MChartCheck;
import com.corp.pixmeup.check.entity.PresbyopiaCheck;
import com.corp.pixmeup.check.entity.SightCheck;
import com.corp.pixmeup.external.dto.AmslerAiResponse;
import com.corp.pixmeup.external.dto.AredsResultResponse;
import com.corp.pixmeup.external.dto.ImagePredictionResponse;
import com.corp.pixmeup.external.dto.MChartAiResponse;
import com.corp.pixmeup.external.dto.PredictionRecord;
import com.corp.pixmeup.external.dto.PresbyopiaAiResponse;
import com.corp.pixmeup.external.dto.SightAiResponse;

import com.corp.pixmeup.external.dto.*;
import org.springframework.data.domain.Slice;

import java.io.IOException;
import java.util.List;


public interface FastApiService {
    AredsResultResponse predictAreds(Long userId);
    AredsResultResponse getAredsResultById(String resultId);
    AredsResultResponse getLatestAredsResultByUserId(Long userId);
    ImagePredictionResponse predictImage(String s3Key, String fileUrl, Long userId) throws IOException;
    ImagePredictionResponse getImagePredictionById(String resultId);
    ImagePredictionResponse getLatestImagePredictionByUserId(Long userId);
    Slice<PredictionRecord> getPredictionRecords(Long userId, int page, int size);
    SightPredictResponse predictSight(Long userId, List<SightHistoryItem> history);
    EyeAgePredictResponse predictEyeAge(Long userId, List<Integer> history);
    SightAiResponse getSightAiResponse(Long userId, List<SightCheck> lists, SurveyData surveyData);
    PresbyopiaAiResponse getPresbyopiaAiResponse(Long userId, List<PresbyopiaCheck> lists, SurveyData surveyData);
    AmslerAiResponse getAmslerAiResponse(Long userId, List<AmslerCheck> lists, SurveyData surveyData);
    MChartAiResponse getMChartAiResponse(Long userId, List<MChartCheck> lists, SurveyData surveyData);
    ImagePredictionFrontResponse getImagePredictionFrontResponse(ImagePredictionResponse response);
}
