package com.corp.pixelro.report.repository;

import com.corp.pixelro.report.entity.ReportSummary;
import com.corp.pixelro.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportRepository extends JpaRepository<ReportSummary, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE ReportSummary rs SET rs.deleted = true, rs.updatedAt = CURRENT_TIMESTAMP WHERE rs.user = :user")
    int softDeleteAllByUser(@Param("user") User user);

}
