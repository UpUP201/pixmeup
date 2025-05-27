package com.corp.pixelro.check.repository;

import com.corp.pixelro.check.dto.CheckSummary;
import com.corp.pixelro.check.repository.projection.RecentExaminationRawDto;

import java.time.LocalDateTime;
import java.util.List;

public interface CheckRepositoryCustom {
    LocalDateTime findLatestCheckDateByUserId(Long userId);

    List<RecentExaminationRawDto> findLatestExaminations(Long userId);

    List<CheckSummary> findTotalChecks(Long userId, int page, int size);

}
