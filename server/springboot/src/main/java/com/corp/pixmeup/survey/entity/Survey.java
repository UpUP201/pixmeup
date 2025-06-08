package com.corp.pixmeup.survey.entity;

import com.corp.pixmeup.global.entity.BaseEntity;
import com.corp.pixmeup.survey.type.Gender;
import com.corp.pixmeup.survey.type.SurgeryType;
import com.corp.pixmeup.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "surveys")
@Getter
@NoArgsConstructor
@SQLRestriction("deleted = false")
public class Survey extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private Integer age;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 1)
    private Gender gender;

    @Column(nullable = false)
    private boolean glasses;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private SurgeryType surgery;

    @Column(nullable = false)
    private boolean diabetes;

    @Column(nullable = false)
    private boolean currentSmoking;

    @Column(nullable = false)
    private boolean pastSmoking;

    @Builder
    public Survey(User user, Integer age, Gender gender, boolean glasses, SurgeryType surgery, boolean diabetes, boolean currentSmoking, boolean pastSmoking) {
        this.user = user;
        this.age = age;
        this.gender = gender;
        this.glasses = glasses;
        this.surgery = surgery;
        this.diabetes = diabetes;
        this.currentSmoking = currentSmoking;
        this.pastSmoking = pastSmoking;
    }
}
