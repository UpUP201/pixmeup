package com.corp.pixmeup.check.repository;

import com.corp.pixmeup.check.entity.PresbyopiaCheck;
import com.corp.pixmeup.user.entity.User;
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
public interface PresbyopiaCheckRepository extends JpaRepository<PresbyopiaCheck, Long> {

    List<PresbyopiaCheck> findAllByUser_Id(Long userId);

    Optional<PresbyopiaCheck> findTopByUser_IdOrderByCreatedAtDesc(Long userId);

    Optional<PresbyopiaCheck> findTopByUser_IdAndCreatedAtLessThanEqualOrderByCreatedAtDesc(Long userId, LocalDateTime beforeDateTime);

    List<PresbyopiaCheck> findAllByUser_IdOrderByCreatedAtDesc(Long userId);

    List<PresbyopiaCheck> findTop5ByUser_IdOrderByCreatedAtDesc(Long userId);

    Slice<PresbyopiaCheck> findByUser_IdAndCreatedAtLessThanOrderByCreatedAtDesc(Long userId, LocalDateTime lastCreatedAt, Pageable pageable);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE PresbyopiaCheck pc SET pc.deleted = true, pc.updatedAt = CURRENT_TIMESTAMP WHERE pc.user = :user")
    int softDeleteAllByUser(@Param("user") User user);
}
