package com.corp.pixmeup.user.entity;

import com.corp.pixmeup.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "user_agrees")
@Getter
@NoArgsConstructor
@SQLRestriction("deleted = false")
public class UserAgree extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private boolean service = true;

    @Column(nullable = false)
    private boolean privacy = true;

    @Column(nullable = false)
    private boolean sensitiveInfo = true;

    @Column(nullable = false)
    private boolean thirdParty = true;

    @Column(nullable = false)
    private boolean consignment = true;

    @Column(nullable = false)
    private boolean marketing = false;

    @Builder
    public UserAgree(User user, boolean service, boolean privacy, boolean sensitiveInfo, boolean thirdParty, boolean consignment, boolean marketing) {
        this.user = user;
        this.service = service;
        this.privacy = privacy;
        this.sensitiveInfo = sensitiveInfo;
        this.thirdParty = thirdParty;
        this.consignment = consignment;
        this.marketing = marketing;
    }
}
