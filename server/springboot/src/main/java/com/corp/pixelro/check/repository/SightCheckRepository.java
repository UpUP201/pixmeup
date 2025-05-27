package com.corp.pixelro.check.repository;

import com.corp.pixelro.check.entity.SightCheck;
import com.corp.pixelro.user.entity.User;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SightCheckRepository extends JpaRepository<SightCheck, Long> {

    List<SightCheck> findAllByUser_Id(Long userId);

    Optional<SightCheck> findTopByUser_IdOrderByCreatedAtDesc(Long userId);

    Optional<SightCheck> findTopByUser_IdAndCreatedAtLessThanEqualOrderByCreatedAtDesc(Long userId, LocalDateTime beforeDateTime);

    List<SightCheck> findAllByUser_IdOrderByCreatedAtDesc(Long userId);

    List<SightCheck> findTop5ByUser_IdOrderByCreatedAtDesc(Long userId);

    Slice<SightCheck> findByUser_IdAndCreatedAtLessThanOrderByCreatedAtDesc(Long userId, LocalDateTime lastCreatedAt, Pageable pageable);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE SightCheck sc SET sc.deleted = true, sc.updatedAt = CURRENT_TIMESTAMP WHERE sc.user = :user")
    int softDeleteAllByUser(@Param("user") User user);
}
