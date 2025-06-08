package com.corp.pixmeup.check.service;

import com.corp.pixmeup.check.entity.MChartCheck;
import com.corp.pixmeup.check.repository.MChartCheckRepository;
import com.corp.pixmeup.external.dto.MChartTest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MChartDataService {
    private final MChartCheckRepository mChartCheckRepository;

    // 해당 사용자의 모든 엠차트 결과 조회
    public List<MChartCheck> selectAllMChartChecks(Long userId) {

        return mChartCheckRepository.findAllByUser_Id(userId);
    }

    // 해당 사용자의 가장 최근 엠차트 결과 조회
    public MChartCheck selectLatestMChartCheck(Long userId) {
        return mChartCheckRepository
                .findTopByUser_IdOrderByCreatedAtDesc(userId)
                .orElse(null);
    }

    public LocalDateTime selectLatestMChart(Long userId) {
        MChartCheck result = selectLatestMChartCheck(userId);
        return result == null ?
                null :
                result.getCreatedAt();
    }

    public List<MChartTest> getMChartTests(Long userId) {
        return selectAllMChartChecks(userId).stream()
                .map(MChartTest::from)
                .toList();
    }
}