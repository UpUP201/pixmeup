package com.corp.pixelro.exercise.repository;

import com.corp.pixelro.exercise.entity.EyeExercise;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EyeExerciseRepository extends JpaRepository<EyeExercise, Long> {
}
