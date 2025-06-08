package com.corp.pixmeup.exercise.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "eye_exercise_steps")
@Getter
@NoArgsConstructor
public class EyeExerciseStep {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eye_exercise_id", nullable = false)
    private EyeExercise eyeExercise;

    @Column(nullable = false, name = "step_order")
    private Integer stepOrder;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String instruction;

    @Column(nullable = false, name = "step_duration")
    private Integer stepDuration;

    @Column(nullable = false, name = "step_image_url")
    private String stepImageUrl;

    @Column(nullable = false, name = "step_tts_url")
    private String stepTtsUrl;

    @Builder
    public EyeExerciseStep(EyeExercise eyeExercise, Integer stepOrder, String title, String instruction,
        Integer stepDuration, String stepImageUrl, String stepTtsUrl) {
        this.eyeExercise = eyeExercise;
        this.stepOrder = stepOrder;
        this.title = title;
        this.instruction = instruction;
        this.stepDuration = stepDuration;
        this.stepImageUrl = stepImageUrl;
        this.stepTtsUrl = stepTtsUrl;
    }
}
