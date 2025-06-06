package com.corp.pixelro.check.service;

import com.corp.pixelro.check.dto.LatestRequest;
import com.corp.pixelro.check.dto.PresbyopiaCheckResponse;
import com.corp.pixelro.check.dto.SightCheckResponse;
import com.corp.pixelro.check.entity.PresbyopiaCheck;
import com.corp.pixelro.check.entity.SightCheck;
import com.corp.pixelro.check.repository.PresbyopiaCheckRepository;
import com.corp.pixelro.check.util.PresbyopiaCheckProcessor;
import com.corp.pixelro.external.dto.PresbyopiaTest;
import com.corp.pixelro.global.error.code.ErrorCode;
import com.corp.pixelro.global.error.exception.EyeCheckPresbyopiaException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PresbyopiaDataService {
    private final PresbyopiaCheckRepository presbyopiaCheckRepository;

    // 해당 사용자의 모든 노안검사 결과 조회
    public List<PresbyopiaCheck> selectAllPresbyopiaChecks(Long userId) {
        List<PresbyopiaCheck> result = presbyopiaCheckRepository.findAllByUser_IdOrderByCreatedAtDesc(userId);

        return result;
    }

    // 해당 사용자의 가장 최근 노안검사 결과 조회
    public LocalDateTime selectLatestPresbyopia(Long userId) {
        return presbyopiaCheckRepository
                .findTopByUser_IdOrderByCreatedAtDesc(userId)
                .map(PresbyopiaCheck::getCreatedAt)
                .orElse(null);
    }

    // 해당 날짜의 가장 최근 노안검사 결과 조회
    public PresbyopiaCheck selectPresbyopiaCheckByDate(LocalDateTime dateTime, Long userId) {
        return presbyopiaCheckRepository.findTopByUser_IdAndCreatedAtLessThanEqualOrderByCreatedAtDesc(userId, dateTime)
                .orElse(null);
    }

    /**
     * FastAPI 사용을 위해 Map으로 변형
     */
    public List<PresbyopiaTest> getPresbyopiaTests(Long userId) {
        return selectAllPresbyopiaChecks(userId).stream()
                .map(PresbyopiaTest::from)
                .toList();
    }

    public List<PresbyopiaCheckResponse> selectPresbyopiaContextWithPrediction(Long userId, LocalDateTime targetDateTime) {
        List<PresbyopiaCheck> all = selectAllPresbyopiaChecks(userId);

        if (all.isEmpty()) {
            return List.of(); // 빈 리스트 반환하면 200 OK
        }

        int index = IntStream.range(0, all.size())
                .filter(i -> all.get(i).getCreatedAt().truncatedTo(ChronoUnit.SECONDS)
                        .isEqual(targetDateTime.truncatedTo(ChronoUnit.SECONDS)))
                .findFirst()
                .orElse(-1); // 못 찾으면 -1

        if (index == -1) {
            return List.of(); // targetDateTime 일치값 없으면 빈 리스트
        }

        boolean isLatest = (index == 0);
        List<PresbyopiaCheck> result = new ArrayList<>();

        if (isLatest) {
            // 검사 결과 최대 6개, 그보다 적으면 그만큼만
            int end = Math.min(6, all.size());
            result.addAll(all.subList(0, end));
            // 가장 최근 검사 결과 기준으로 예측 행 생성
            result.add(PresbyopiaCheckProcessor.buildPredictionRow(all.get(0))); // 예측값 추가
        } else {
            // 검사 결과 기준 앞으로 3개 확보하려고 시도
            int start = Math.max(0, index - 3);
            // start로부터 7개 확보, 리스트 크기 넘어가지 않도록 all.size()로 제한
            int end = Math.min(all.size(), start + 7);

            // 7개보다 작고 start가 0보다 클 경우, 범위 조정
            if (end - start < 7 && start > 0) {
                start = Math.max(0, end - 7);
            }

            // 조정된 범위를 리스트에 추가
            result.addAll(all.subList(start, end)); // 예측값 없음
        }

        // 전체 정렬
        result.sort(Comparator.comparing(PresbyopiaCheck::getCreatedAt));

        return result.stream()
                .map(PresbyopiaCheckResponse::of)
                .toList();

    }
}