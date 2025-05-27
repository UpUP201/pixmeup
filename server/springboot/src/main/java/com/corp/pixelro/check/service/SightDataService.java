package com.corp.pixelro.check.service;

import com.corp.pixelro.check.dto.SightCheckResponse;
import com.corp.pixelro.check.entity.SightCheck;
import com.corp.pixelro.check.repository.SightCheckRepository;
import com.corp.pixelro.check.util.SightCheckProcessor;
import com.corp.pixelro.external.dto.SightTest;
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
public class SightDataService {

    private final SightCheckRepository sightCheckRepository;

    // 해당 사용자의 모든 시력검사 결과 조회
    public List<SightCheck> selectAllSightChecks(Long userId) {
        List<SightCheck> result = sightCheckRepository.findAllByUser_IdOrderByCreatedAtDesc(userId);

        return result;
    }

    // 해당 사용자의 가장 최근 시력검사 결과 조회
    public LocalDateTime selectLatestSight(Long userId) {
        return sightCheckRepository
                .findTopByUser_IdOrderByCreatedAtDesc(userId)
                .map(SightCheck::getCreatedAt)
                .orElse(null);
    }

    // 해당 날짜의 가장 최근 시력검사 결과 조회
    public SightCheck selectSightCheckByDate(LocalDateTime dateTime, Long userId) {
        return sightCheckRepository.findTopByUser_IdAndCreatedAtLessThanEqualOrderByCreatedAtDesc(userId, dateTime)
                .orElse(null);
    }

    /**
     * FastAPI 사용을 위해 Map으로 변형
     */
    public List<SightTest> getSightTests(Long userId) {
        return selectAllSightChecks(userId).stream()
                .map(SightTest::from)
                .toList();
    }

    public List<SightCheckResponse> selectSightContextWithPrediction(Long userId, LocalDateTime targetDateTime) {
        List<SightCheck> all = selectAllSightChecks(userId); // 최신순 정렬

        int index = IntStream.range(0, all.size())
                .filter(i -> all.get(i).getCreatedAt().truncatedTo(ChronoUnit.SECONDS)
                        .isEqual(targetDateTime.truncatedTo(ChronoUnit.SECONDS)))
                .findFirst()
                .orElse(-1); // 못 찾으면 -1 반환

        if (index == -1) {
            return List.of(); // 또는 data: null 응답 위해 null 반환도 가능
        }

        boolean isLatest = (index == 0);
        List<SightCheck> result = new ArrayList<>();

        if (isLatest) {
            int end = Math.min(6, all.size());
            result.addAll(all.subList(0, end));
            result.add(SightCheckProcessor.buildPredictionRow(all.get(0))); // 예측값
        } else {
            int start = Math.max(0, index - 3);
            int end = Math.min(all.size(), start + 7);

            if (end - start < 7 && start > 0) {
                start = Math.max(0, end - 7);
            }

            result.addAll(all.subList(start, end));
        }

        // 전체 정렬
        result.sort(Comparator.comparing(SightCheck::getCreatedAt));

        return result.stream()
                .map(SightCheckResponse::of)
                .toList();
    }

}
