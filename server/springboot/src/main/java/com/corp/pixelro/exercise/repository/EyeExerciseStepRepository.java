package com.corp.pixelro.exercise.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.corp.pixelro.exercise.entity.EyeExercise;
import com.corp.pixelro.exercise.entity.EyeExerciseStep;

@Repository
public interface EyeExerciseStepRepository extends JpaRepository<EyeExerciseStep, Long> {

	List<EyeExerciseStep> findByEyeExercise(EyeExercise eyeExercise);

}
