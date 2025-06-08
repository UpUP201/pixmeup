package com.corp.pixmeup.check.repository;

import com.corp.pixmeup.check.entity.AmslerCheck;
import com.corp.pixmeup.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface AmslerCheckRepository extends JpaRepository<AmslerCheck, Long> {

    Optional<AmslerCheck> findTopByUser_IdOrderByCreatedAtDesc(Long userId);

    List<AmslerCheck> findAllByUser_IdOrderByCreatedAtDesc(Long userId);

    List<AmslerCheck> findAllByUser_Id(Long userId);

    Optional<AmslerCheck> findByUser_IdAndCreatedAt(Long userId, LocalDateTime targetDateTime);

    Optional<AmslerCheck> findByUser_IdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE AmslerCheck ac SET ac.deleted = true, ac.updatedAt = CURRENT_TIMESTAMP WHERE ac.user = :user")
    int softDeleteAllByUser(@Param("user") User user);
}
