package com.corp.pixmeup.survey.repository;

import com.corp.pixmeup.survey.entity.Survey;
import com.corp.pixmeup.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SurveyRepository extends JpaRepository<Survey, Long> {
    Optional<Survey> findTopByUser_IdOrderByCreatedAtDesc(Long userId);

    List<Survey> findAllByUser_Id(Long userId);

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE Survey s SET s.deleted = true, s.updatedAt = CURRENT_TIMESTAMP WHERE s.user = :user")
    int softDeleteAllByUser(@Param("user") User user);
}
