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
@Table(name = "amsler_checks")
@Getter
@NoArgsConstructor
@SQLRestriction("deleted = false")
public class AmslerCheck extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(nullable = false, length = 20)
    private String rightMacularLoc;

    @Column(nullable = false, length = 20)
    private String leftMacularLoc;

    @Column(nullable = true, length = 255)
    private String aiResult;

    @Builder
    public AmslerCheck(User user, String leftMacularLoc, String rightMacularLoc, String aiResult, LocalDateTime createdAt) {
        this.user = user;
        this.leftMacularLoc = leftMacularLoc;
        this.rightMacularLoc = rightMacularLoc;
        this.aiResult = aiResult;
        this.createdAt = createdAt;
    }
}
