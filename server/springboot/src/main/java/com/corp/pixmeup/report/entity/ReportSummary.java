package com.corp.pixmeup.report.entity;

import com.corp.pixmeup.global.entity.BaseEntity;
import com.corp.pixmeup.user.entity.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

@Entity
@Table(name = "report_summaries")
@Getter
@NoArgsConstructor
@SQLRestriction("deleted = false")
public class ReportSummary extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Lob
    private String recommendNextCheckup;

    @Lob
    private String finalAnalysisText;

    @Builder
    public ReportSummary(User user, String recommendNextCheckup, String finalAnalysisText) {
        this.user = user;
        this.recommendNextCheckup = recommendNextCheckup;
        this.finalAnalysisText = finalAnalysisText;
    }
}
