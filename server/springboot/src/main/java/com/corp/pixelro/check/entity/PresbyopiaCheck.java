package com.corp.pixelro.check.entity;

import com.corp.pixelro.check.type.StatusType;
import com.corp.pixelro.global.entity.BaseEntity;
import com.corp.pixelro.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Table(name = "presbyopia_checks")
@Getter
@NoArgsConstructor
@SQLRestriction("deleted = false")
public class PresbyopiaCheck extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private Double firstDistance;

    @Column(nullable = false)
    private Double secondDistance;

    @Column(nullable = false)
    private Double thirdDistance;

    @Column(nullable = false)
    private Double avgDistance;

    @Column(nullable = false)
    private Integer age;

    @Column(nullable = true)
    private Integer agePrediction;

    @Column(nullable = true, length = 255)
    private String aiResult;

    @Enumerated(EnumType.STRING)
    @Column(nullable = true)
    private StatusType status;

    @Builder
    public PresbyopiaCheck(User user, Double firstDistance, Double secondDistance, Double thirdDistance, Double avgDistance, Integer age, Integer agePrediction, String aiResult, StatusType status, LocalDateTime createdAt) {
        this.user = user;
        this.firstDistance = firstDistance;
        this.secondDistance = secondDistance;
        this.thirdDistance = thirdDistance;
        this.avgDistance = avgDistance;
        this.age = age;
        this.agePrediction = agePrediction;
        this.aiResult = aiResult;
        this.status = status;
        this.createdAt = createdAt;
    }
}
