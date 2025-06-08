package com.corp.pixmeup.exercise.repository;

import java.util.List;

import com.corp.pixmeup.exercise.entity.EyeExerciseRecord;
import com.corp.pixmeup.user.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EyeExerciseRecordRepository extends JpaRepository<EyeExerciseRecord, Long> {
	List<EyeExerciseRecord> findByUser(User user);

	@Modifying(clearAutomatically = true, flushAutomatically = true)
	@Query("UPDATE EyeExerciseRecord eer SET eer.deleted = true, eer.updatedAt = CURRENT_TIMESTAMP WHERE eer.user = :user")
	int softDeleteAllByUser(@Param("user") User user);
}
