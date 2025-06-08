package com.corp.pixmeup.check.entity;

import com.corp.pixmeup.check.type.StatusType;
import com.corp.pixmeup.global.entity.BaseEntity;
import com.corp.pixmeup.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "sight_checks")
@Getter
@NoArgsConstructor
@SQLRestriction("deleted = false")
public class SightCheck extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private Integer leftSight;

    @Column(nullable = false)
    private Integer rightSight;

    @Column(nullable = true)
    private Integer leftSightPrediction;

    @Column(nullable = true)
    private Integer rightSightPrediction;

    @Column(nullable = true, length = 255)
    private String aiResult;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private StatusType status;

    @Builder
    public SightCheck(User user, Integer leftSight, Integer rightSight, Integer leftSightPrediction, Integer rightSightPrediction, String aiResult, StatusType status, LocalDateTime createdAt) {
        this.user = user;
        this.leftSight = leftSight;
        this.rightSight = rightSight;
        this.leftSightPrediction = leftSightPrediction;
        this.rightSightPrediction = rightSightPrediction;
        this.aiResult = aiResult;
        this.status = status;
        this.createdAt = createdAt;
    }
}
