package com.corp.pixmeup.exercise.entity;

import com.corp.pixmeup.global.entity.BaseEntity;
import com.corp.pixmeup.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "eye_exercise_records")
@Getter
@NoArgsConstructor
@SQLRestriction("deleted = false")
public class EyeExerciseRecord extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "eye_exercise_id", nullable = false)
    private EyeExercise eyeExercise;

    @Builder
    public EyeExerciseRecord(User user, EyeExercise eyeExercise) {
        this.user = user;
        this.eyeExercise = eyeExercise;
    }
}
