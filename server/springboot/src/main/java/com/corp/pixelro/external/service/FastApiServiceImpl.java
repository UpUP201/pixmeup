package com.corp.pixelro.external.service;

import com.corp.pixelro.check.entity.AmslerCheck;
import com.corp.pixelro.check.entity.MChartCheck;
import com.corp.pixelro.check.entity.PresbyopiaCheck;
import com.corp.pixelro.check.entity.SightCheck;
import com.corp.pixelro.check.service.*;
import com.corp.pixelro.external.dto.*;
import com.corp.pixelro.external.util.AredsRequestBuilder;
import com.corp.pixelro.global.error.code.ErrorCode;
import com.corp.pixelro.global.error.exception.BusinessException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class FastApiServiceImpl implements FastApiService {

    private final WebClient fastApiWebClient;

    // 분리된 DataService
    private final AredsDataService aredsDataService;
    private final SurveyDataService surveyDataService;
    private final SightDataService sightDataService;
    private final PresbyopiaDataService presbyopiaDataService;
    private final AmslerDataService amslerDataService;
    private final MChartDataService mChartDataService;
    private final S3Service s3Service;
    private final ObjectMapper objectMapper;


    @Value("${fastapi.base-url}")
    private String baseUrl;

    @Override
    @Transactional
    public AredsResultResponse predictAreds(Long userId) {
        AredsPredictionInput input = aredsDataService.prepareAredsInput(userId);
        Map<String, Object> requestBody = AredsRequestBuilder.toRequestMap(input, userId);

        return fastApiWebClient.post()
                .uri(baseUrl + "/predict/areds")
                .bodyValue(requestBody)
                .retrieve()
                .bodyToMono(AredsResultResponse.class)
                .block();
    }


    @Override
    @Transactional
    public AredsResultResponse getAredsResultById(String resultId) {
        return fastApiWebClient.get()
                .uri(baseUrl + "/predict/areds/" + resultId)
                .retrieve()
                .bodyToMono(AredsResultResponse.class)
                .block();
    }

    @Override
    @Transactional
    public AredsResultResponse getLatestAredsResultByUserId(Long userId) {
        return fastApiWebClient.get()
                .uri(baseUrl + "/predict/areds/latest/" + userId)
                .retrieve()
                .bodyToMono(AredsResultResponse.class)
                .block();
    }

    @Override
    @Transactional
    public ImagePredictionResponse predictImage(String s3Key, String fileUrl, Long userId) {
        ImagePredictionRequest request = ImagePredictionRequest.builder()
                .userId(userId)
                .fileUrl(fileUrl)
                .s3Key(s3Key)
                .build();

        String fullUrl = baseUrl + "/predict/image";
        try {
            log.info("📤 FastAPI 호출 준비 - URL: {}", fullUrl);
            log.info("📤 FastAPI 호출 Body: userId={}, s3Key={}, fileUrl={}",
                    request.userId(), request.s3Key(), request.fileUrl());
        } catch (Exception e) {
            log.warn("⚠️ 로깅 중 오류 발생: {}", e.getMessage());
        }

        return fastApiWebClient.post()
                .uri(fullUrl)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(request)
                .retrieve()
                .bodyToMono(ImagePredictionResponse.class)
                .block();
    }

    @Override
    @Transactional
    public ImagePredictionResponse getImagePredictionById(String resultId) {
        return fastApiWebClient.get()
                .uri(baseUrl + "/predict/image/" + resultId)
                .retrieve()
                .bodyToMono(ImagePredictionResponse.class)
                .block();
    }

    @Override
    public ImagePredictionResponse getLatestImagePredictionByUserId(Long userId) {
        return fastApiWebClient.get()
                .uri(baseUrl + "/predict/image/latest/" + userId)
                .retrieve()
                .bodyToMono(ImagePredictionResponse.class)
                .block();
    }

    @Override
    @Transactional
    public Slice<PredictionRecord> getPredictionRecords(Long userId, int page, int size) {
        try {
            String uri = UriComponentsBuilder.fromHttpUrl(baseUrl + "/predict/records")
                    .queryParam("user_id", userId)
                    .queryParam("page", page)
                    .queryParam("size", size)
                    .toUriString();

            PredictionRecord[] resultArray = fastApiWebClient.get()
                    .uri(uri)
                    .retrieve()
                    .bodyToMono(PredictionRecord[].class)
                    .block();

            List<PredictionRecord> records = List.of(resultArray);
            boolean hasNext = records.size() == size;
            return new SliceImpl<>(records, PageRequest.of(page, size), hasNext);
        } catch (RuntimeException e) {
            log.error("전체 예측 결과 조회 에러 발생: {}", e.getMessage());
            throw new BusinessException(ErrorCode.FAST_API_ERROR, "전체 예측 결과 조회 에러가 발생했습니다.");
        }
    }

    @Override
    public SightPredictResponse predictSight(Long userId, List<SightHistoryItem> history) {
        return fastApiWebClient.post()
                .uri(baseUrl + "/predict/sight")
                .bodyValue(new SightPredictRequest(userId, history))
                .retrieve()
                .bodyToMono(SightPredictResponse.class)
                .block();
    }

    @Override
    public EyeAgePredictResponse predictEyeAge(Long userId, List<Integer> history) {
        return fastApiWebClient.post()
                .uri(baseUrl + "/predict/eye-age")
                .bodyValue(new EyeAgePredictRequest(userId, history))
                .retrieve()
                .bodyToMono(EyeAgePredictResponse.class)
                .block();
    }

    // 시력 검사 AI 판정 결과
    @Override
    public SightAiResponse getSightAiResponse(Long userId, List<SightCheck> lists, SurveyData surveyData) {
        List<SightTest> sightTests = lists.stream()
                .map(SightTest::from)
                .toList();

        return fastApiWebClient.post()
                .uri(baseUrl + "/llm/sight")
                .bodyValue(new SightRequest(surveyData, sightTests))
                .retrieve()
                .bodyToMono(SightAiResponse.class)
                .block();
    }

    // 노안 검사 AI 판정 결과
    @Override
    public PresbyopiaAiResponse getPresbyopiaAiResponse(Long userId, List<PresbyopiaCheck> lists, SurveyData surveyData) {
        List<PresbyopiaTest> tests = lists.stream()
                .map(PresbyopiaTest::from)
                .toList();

        return fastApiWebClient.post()
                .uri(baseUrl + "/llm/presbyopia")
                .bodyValue(new PresbyopiaRequest(surveyData, tests))
                .retrieve()
                .bodyToMono(PresbyopiaAiResponse.class)
                .block();
    }

    // 암슬러 검사 AI 판정 결과
    @Override
    public AmslerAiResponse getAmslerAiResponse(Long userId, List<AmslerCheck> lists, SurveyData surveyData) {
//        SurveyData surveyData = surveyDataService.getLatestSurveyData(userId);
        List<AmslerTest> tests = lists.stream()
                .map(AmslerTest::from)
                .toList();

        try {
            String json = objectMapper.writeValueAsString(new AmslerRequest(surveyData, tests));
            System.out.println("[보내는 JSON 확인] " + json);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

        return fastApiWebClient.post()
                .uri(baseUrl + "/llm/amsler")
                .bodyValue(new AmslerRequest(surveyData, tests))
                .retrieve()
                .bodyToMono(AmslerAiResponse.class)
                .block();
    }

    // MChart 검사 AI 판정 결과
    @Override
    public MChartAiResponse getMChartAiResponse(Long userId, List<MChartCheck> lists, SurveyData surveyData) {
//        SurveyData surveyData = surveyDataService.getLatestSurveyData(userId);
        List<MChartTest> tests = lists.stream()
                .map(MChartTest::from)
                .toList();

        return fastApiWebClient.post()
                .uri(baseUrl + "/llm/mchart")
                .bodyValue(new MChartRequest(surveyData, tests))
                .retrieve()
                .bodyToMono(MChartAiResponse.class)
                .block();
    }

    @Transactional
    @Override
    public ImagePredictionFrontResponse getImagePredictionFrontResponse(ImagePredictionResponse response) {
        return ImagePredictionFrontResponse.builder()
                .id(response.id())
                .userId(response.userId())
                .summary(response.summary())
                .description(response.description())
                .imageUrl(s3Service.generatePresignedGetUrl(response.s3Key()))
                .createdAt(response.createdAt())
                .build();
    }
}
