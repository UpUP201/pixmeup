package com.corp.pixelro.exercise.entity;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name = "eye_exercise")
@Getter
@NoArgsConstructor
public class EyeExercise {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String summary;

    @Column(nullable = false, name = "total_duration")
    private Integer totalDuration;

    @Column(nullable = false, name = "total_steps")
    private Integer totalSteps;

    @Column(nullable = false, name = "thumbnail_url")
    private String thumbnailUrl;

    @Column(nullable = false, name = "intro_tts_url")
    private String introTtsUrl;

    @Column(columnDefinition = "TEXT")
    private String precautions;

    @Column(columnDefinition = "TEXT")
    private String guidelines;

    @Builder
    public EyeExercise(String name, String description, String summary, Integer totalDuration,
        Integer totalSteps, String thumbnailUrl, String introTtsUrl, String precautions, String guidelines) {
        this.name = name;
        this.description = description;
        this.summary = summary;
        this.totalDuration = totalDuration;
        this.totalSteps = totalSteps;
        this.thumbnailUrl = thumbnailUrl;
        this.introTtsUrl = introTtsUrl;
        this.precautions = precautions;
        this.guidelines = guidelines;
    }
}
