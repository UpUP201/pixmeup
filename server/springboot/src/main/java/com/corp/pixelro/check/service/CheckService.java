package com.corp.pixelro.check.service;

import com.corp.pixelro.check.dto.*;
import com.corp.pixelro.check.entity.AmslerCheck;
import com.corp.pixelro.check.entity.MChartCheck;
import com.corp.pixelro.check.entity.PresbyopiaCheck;
import com.corp.pixelro.check.entity.SightCheck;
import com.corp.pixelro.external.dto.SurveyData;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.LocalDate;
import java.time.LocalDateTime;

public interface CheckService {

    // 전체 결과를 리스트로 통합
    Slice<CheckSummary> selectTotalCheckList(Long userId, int page, int size);

    void createSightCheck(SightCheckRequest sightCheck, Long userId, SurveyData surveyData);

    void createPresbyopiaCheck(PresbyopiaCheckRequest presbyopiaCheck, Long userId, SurveyData surveyData);

    void createAmslerCheck(AmslerCheckRequest amslerCheck, Long userId, SurveyData surveyData);

    void createMChartCheck(MChartCheckRequest mChartCheck, Long userId, SurveyData surveyData);

    AmdCheckDetailResponse selectAmdCheckDetail(Long userId, LocalDateTime targetDateTime);

    AmdCheckDetailResponse selectAmdCheckDetailByDifferentDate(Long userId, LocalDateTime amslerDateTime, LocalDateTime mchartDateTime);

    void createData(QrRequest request, Long userId);

    boolean aredsInputHasChanged(Long userId);

}
