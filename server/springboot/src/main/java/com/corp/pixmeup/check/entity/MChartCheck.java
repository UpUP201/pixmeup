package com.corp.pixmeup.check.entity;

import com.corp.pixmeup.global.entity.BaseEntity;
import com.corp.pixmeup.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Entity
@Table(name = "mchart_checks")
@Getter
@NoArgsConstructor
@SQLRestriction("deleted = false")
public class MChartCheck extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false)
    private Integer leftEyeVer;

    @Column(nullable = false)
    private Integer rightEyeVer;

    @Column(nullable = false)
    private Integer leftEyeHor;

    @Column(nullable = false)
    private Integer rightEyeHor;

    @Column(nullable = true, length = 255)
    private String aiResult;

    @Builder
    public MChartCheck(User user, Integer leftEyeVer, Integer rightEyeVer, Integer leftEyeHor, Integer rightEyeHor, String aiResult, LocalDateTime createdAt) {
        this.user = user;
        this.leftEyeVer = leftEyeVer;
        this.rightEyeVer = rightEyeVer;
        this.leftEyeHor = leftEyeHor;
        this.rightEyeHor = rightEyeHor;
        this.aiResult = aiResult;
        this.createdAt = createdAt;
    }
}
