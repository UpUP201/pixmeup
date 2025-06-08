package com.corp.pixmeup.user.entity;

import com.corp.pixmeup.check.entity.AmslerCheck;
import com.corp.pixmeup.check.entity.MChartCheck;
import com.corp.pixmeup.check.entity.PresbyopiaCheck;
import com.corp.pixmeup.check.entity.SightCheck;
import com.corp.pixmeup.exercise.entity.EyeExerciseRecord;
import com.corp.pixmeup.global.entity.BaseEntity;
import com.corp.pixmeup.global.util.PhoneUtils;
import com.corp.pixmeup.report.entity.ReportSummary;
import com.corp.pixmeup.survey.entity.Survey;
import com.corp.pixmeup.webauthn.entity.WebAuthnCredential;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users")
@Getter
@NoArgsConstructor
@SQLRestriction("deleted = false")
public class User extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 20)
    private String name;

    @Column(nullable = false, length = 50, unique = true)
    private String phoneNumber;

    @Column(nullable = false)
    private String password;

    @Column(unique = true, length = 64)
    private String externalKey;

    @Column
    private LocalDateTime withdrawalAt;

    @OneToMany(mappedBy = "user")
    private List<AmslerCheck> amslerChecks = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<MChartCheck> mChartChecks = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<PresbyopiaCheck> presbyopiaChecks = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<SightCheck> sightChecks = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<Survey> surveys = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<UserAgree> userAgrees = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<EyeExerciseRecord> eyeExerciseRecords = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<ReportSummary> reportSummaries = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<WebAuthnCredential> webAuthnCredentials = new ArrayList<>();

    @Builder
    public User(String name, String phoneNumber, String password) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public void performWithdrawalActions(String externalKey) {
        super.delete();
        this.name = "탈퇴한 회원";
        this.phoneNumber = PhoneUtils.generateRandomWithdrawnPhoneNumber(this.id);
        this.password = "withdrawn";
        this.withdrawalAt = LocalDateTime.now();
        this.externalKey = externalKey;
    }

    public void updateProfile(String name, String phoneNumber) {
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
    
    public void updatePassword(String newPassword) {
        this.password = newPassword;
    }
}
