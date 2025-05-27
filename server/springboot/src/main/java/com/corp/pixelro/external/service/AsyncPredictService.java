package com.corp.pixelro.external.service;

import com.corp.pixelro.check.service.AredsDataService;
import com.corp.pixelro.check.service.CheckService;
import com.corp.pixelro.external.dto.AredsResultResponse;
import com.corp.pixelro.external.dto.ImagePredictionFrontResponse;
import com.corp.pixelro.external.dto.ImagePredictionResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class AsyncPredictService {

    private final FastApiService fastApiService;
    private final SseEmitterService sseEmitterService;
    private final S3Service s3Service;
    private final CheckService checkService;

    @Async("threadPoolTaskExecutor")
    public void asyncPredictAreds(Long userId, String emitterId) {
        log.info("[AREDS] asyncPredictAreds 시작 1초 대기 - userId: {}", userId);
        try {
            Thread.sleep(1000);
            log.info("[AREDS] 입력 변경 여부 확인 - userId: {}", userId);
            boolean changed = checkService.aredsInputHasChanged(userId);
            log.info("[AREDS] 입력 변경됨? {}", changed);

            if (!changed) {
                log.info("[AREDS] 입력 변경 없음 - 기존 결과 조회 시도");
                AredsResultResponse lastResult = fastApiService.getLatestAredsResultByUserId(userId);

                if (lastResult != null) {
                    log.info("[AREDS] 기존 예측 결과 존재 - SSE 전송 시작");
                    sseEmitterService.sendAredsResult(emitterId, lastResult);
                    return;
                } else {
                    log.warn("[AREDS] 입력 변경 없음 + 기존 예측 결과 없음 → 강제 예측 실행");
                }
            }

            log.info("[AREDS] FastAPI 예측 요청 시작 - userId: {}", userId);
            AredsResultResponse result = fastApiService.predictAreds(userId);

            if (result == null) {
                log.error("[AREDS] 예측 결과가 null - SSE 에러 전송");
                sseEmitterService.sendAredsError(emitterId, "예측 결과를 가져오지 못했습니다.");
                return;
            }

            log.info("[AREDS] FastAPI 예측 성공 - SSE 전송 시작");
            sseEmitterService.sendAredsResult(emitterId, result);
        } catch (Exception e) {
            log.error("[AREDS] 예측 중 예외 발생 - userId: {}, 메시지: {}", userId, e.getMessage(), e);
            sseEmitterService.sendAredsError(emitterId, e.getMessage());
        }
    }

    @Async("threadPoolTaskExecutor")
    public void asyncPredictImage(String key, Long userId, String emitterId) {
        try {
            log.info("📨 [asyncPredictImage 시작] userId={}, key={}, emitterId={}", userId, key, emitterId);

            String fileUrl = s3Service.generatePresignedGetUrl(key);
            log.info("🔗 [Presigned URL 생성 완료] fileUrl={}", fileUrl);
            // FastAPI 호출 (예측 후 Mongo 저장됨)
            ImagePredictionResponse result = fastApiService.predictImage(key, fileUrl, userId);
            log.info("📤 [FastAPI 예측 요청 완료] resultId={}, userId={}", result.id(), userId);

            Thread.sleep(1000); // 약간의 대기 후 조회 보장 (Mongo write 직후 read)
            // 결과 불러온 후
            ImagePredictionResponse finalResult = fastApiService.getImagePredictionById(result.id());
            log.info("📥 [FastAPI 결과 조회 완료] resultId={}, description={}", finalResult.id(), finalResult.description());
            // S3 URL로 가공 후 반환

            ImagePredictionFrontResponse response = fastApiService.getImagePredictionFrontResponse(finalResult);
            log.info("📡 [SSE 전송 준비 완료] emitterId={}, imageUrl={}", emitterId, response.imageUrl());

            sseEmitterService.sendImageResult(emitterId, response);
            log.info("✅ [SSE 전송 완료] emitterId={}", emitterId);
        } catch (Exception e) {
            log.error("❌ Failed to predict image - emitterId={}, error={}", emitterId, e.getMessage(), e);
            sseEmitterService.sendImageError(emitterId, e.getMessage());
        }
    }
}
