package com.corp.pixelro.check.service;

import com.corp.pixelro.check.entity.AmslerCheck;
import com.corp.pixelro.check.repository.AmslerCheckRepository;
import com.corp.pixelro.external.dto.AmslerTest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AmslerDataService {
    private final AmslerCheckRepository amslerCheckRepository;

    // 해당 사용자의 모든 암슬러 결과 조회
    public List<AmslerCheck> selectAllAmslerChecks(Long userId) {

        return amslerCheckRepository.findAllByUser_Id(userId);
    }

    // 해당 사용자의 가장 최근 암슬러 결과 조회
    public AmslerCheck selectLatestAmslerCheck(Long userId) {
        return amslerCheckRepository
                .findTopByUser_IdOrderByCreatedAtDesc(userId)
                .orElse(null);
    }

    // 해당 사용자의 가장 최근 암슬러 결과 조회
    public LocalDateTime selectLatestAmsler(Long userId) {
        AmslerCheck result = selectLatestAmslerCheck(userId);
        return result == null ?
                null :
                result.getCreatedAt();
    }

    public List<AmslerTest> getAmslerTests(Long userId) {
        return selectAllAmslerChecks(userId).stream()
                .map(AmslerTest::from)
                .toList();
    }
}