package com.corp.pixelro.check.repository;

import com.corp.pixelro.check.entity.MChartCheck;
import com.corp.pixelro.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MChartCheckRepository extends JpaRepository<MChartCheck,Long> {

    Optional<MChartCheck> findTopByUser_IdOrderByCreatedAtDesc(Long userId);

    List<MChartCheck> findAllByUser_IdOrderByCreatedAtDesc(Long userId);

    List<MChartCheck> findAllByUser_Id(Long userId);

    Optional<MChartCheck> findByUser_IdAndCreatedAt(Long userId, LocalDateTime targetDateTime);

    Optional<MChartCheck> findByUser_IdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE MChartCheck mc SET mc.deleted = true, mc.updatedAt = CURRENT_TIMESTAMP WHERE mc.user = :user")
    int softDeleteAllByUser(@Param("user") User user);
}
