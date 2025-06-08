package com.corp.pixmeup.exercise.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.corp.pixmeup.exercise.entity.EyeExercise;
import com.corp.pixmeup.exercise.entity.EyeExerciseStep;

@Repository
public interface EyeExerciseStepRepository extends JpaRepository<EyeExerciseStep, Long> {

	List<EyeExerciseStep> findByEyeExercise(EyeExercise eyeExercise);

}
