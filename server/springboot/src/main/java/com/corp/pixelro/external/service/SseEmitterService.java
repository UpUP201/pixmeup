package com.corp.pixelro.external.service;

import com.corp.pixelro.external.dto.AredsResultResponse;
import com.corp.pixelro.external.dto.ImagePredictionFrontResponse;
import com.corp.pixelro.external.dto.ImagePredictionResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Service
@Slf4j
public class SseEmitterService {
    private final Map<String, SseEmitter> emitters = new ConcurrentHashMap<>();

    public SseEmitter createEmitter(String emitterId) {
        log.info("[SSE] Create emitter for emitterId: {}", emitterId);

        // ❗ 기존 emitter가 있다면 안전하게 정리
        SseEmitter old = emitters.remove(emitterId);
        if (old != null) {
            log.info("[SSE] Existing emitter found for emitterId {}, closing it", emitterId);
            old.complete();
            try {
                Thread.sleep(100); // 짧은 시간 대기 (연결 안정화용)
            } catch (InterruptedException ignored) {}
        }

        SseEmitter emitter = new SseEmitter(60_000L);
        emitters.put(emitterId, emitter);

        emitter.onTimeout(() -> {
            log.warn("[SSE] Timeout for emitterId: {}", emitterId);
            emitters.remove(emitterId);
        });

        emitter.onCompletion(() -> {
            log.info("[SSE] Completion for emitterId: {}", emitterId);
            emitters.remove(emitterId);
        });

        emitter.onError(e -> {
            String msg = e.getMessage();
            if (msg != null && msg.contains("Broken pipe")) {
                log.warn("[SSE] Broken pipe - 정상 종료로 처리: emitterId={}", emitterId);
            } else {
                log.error("[SSE] Error for emitterId {}: {}", emitterId, e.toString());
            }
            emitters.remove(emitterId);
        });

        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(() -> {
            try {
                emitter.send(SseEmitter.event().name("ping").data("keep-alive"));
                log.info("[SSE] Ping sent to emitterId: {}", emitterId);
            } catch (IOException e) {
                log.error("[SSE] Ping send failed for emitterId {}: {}", emitterId, e.getMessage());
                emitter.completeWithError(e);
            } finally {
                executor.shutdown(); // 반드시 스레드 누수 방지
            }
        }, 0, 15, TimeUnit.SECONDS);

        try {
            emitter.send(SseEmitter.event().name("init").data("connected"));
        } catch (IOException e) {
            log.error("[SSE] Init send failed for emitterId {}: {}", emitterId, e.getMessage());
            emitter.completeWithError(e);
        }

        return emitter;
    }


    public void sendAredsResult(String emitterId, AredsResultResponse result) {
        SseEmitter emitter = emitters.get(emitterId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .name("areds-result")
                        .data(result));
                emitter.complete();
            } catch (IOException e) {
                emitter.completeWithError(e);
                emitters.remove(emitterId);
            }
        }
    }

    public void sendImageResult(String emitterId, ImagePredictionFrontResponse result) {
        SseEmitter emitter = emitters.get(emitterId);
        if (emitter != null) {
            try {
                log.info("[SSE] 이미지 예측 결과 전송 시작 - emitterId: {}, resultId: {}", emitterId, result.id());

                emitter.send(SseEmitter.event().name("image-result").data(result));
                emitter.complete();

                log.info("[SSE] 이미지 예측 결과 전송 완료 - emitterId: {}", emitterId);
            } catch (IOException e) {
                log.warn("SSE 전송 실패 - 연결 끊김 등: {}", e.getMessage());
                emitter.completeWithError(e);
                emitters.remove(emitterId);
            }
        } else {
            log.warn("[SSE] 전송 실패 - emitter 없음: emitterId {}", emitterId);
        }
    }

    public void sendAredsError(String emitterId, String message) {
        SseEmitter emitter = emitters.get(emitterId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("areds-error").data(message));
                emitter.complete();
            } catch (IOException e) {
                emitter.completeWithError(e);
                emitters.remove(emitterId);
            }
        }
    }

    public void sendImageError(String emitterId, String message) {
        SseEmitter emitter = emitters.get(emitterId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event().name("image-error").data(message));
                emitter.complete();
            } catch (IOException e) {
                emitter.completeWithError(e);
                emitters.remove(emitterId);
            }
        }
    }
}
