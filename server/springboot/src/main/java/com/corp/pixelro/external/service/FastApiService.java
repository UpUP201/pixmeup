package com.corp.pixelro.external.service;

import com.corp.pixelro.check.entity.AmslerCheck;
import com.corp.pixelro.check.entity.MChartCheck;
import com.corp.pixelro.check.entity.PresbyopiaCheck;
import com.corp.pixelro.check.entity.SightCheck;
import com.corp.pixelro.external.dto.AmslerAiResponse;
import com.corp.pixelro.external.dto.AredsResultResponse;
import com.corp.pixelro.external.dto.ImagePredictionResponse;
import com.corp.pixelro.external.dto.MChartAiResponse;
import com.corp.pixelro.external.dto.PredictionRecord;
import com.corp.pixelro.external.dto.PresbyopiaAiResponse;
import com.corp.pixelro.external.dto.SightAiResponse;

import com.corp.pixelro.external.dto.*;
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
