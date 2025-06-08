package com.corp.pixmeup.check.service;

import com.corp.pixmeup.check.dto.VisionResponse;
import com.corp.pixmeup.check.entity.PresbyopiaCheck;
import com.corp.pixmeup.check.entity.SightCheck;
import com.corp.pixmeup.user.entity.User;
import com.corp.pixmeup.user.service.UserService;
import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VisionService {

    private final SightDataService sightDataService;
    private final PresbyopiaDataService presbyopiaDataService;
    private final UserService userService;

    public VisionResponse selectVisionByDate(LocalDateTime dateTime, Long userId) {
        User user = userService.getUser(userId);
        SightCheck sightCheck = sightDataService.selectSightCheckByDate(dateTime,userId);
        PresbyopiaCheck presbyopiaCheck = presbyopiaDataService.selectPresbyopiaCheckByDate(dateTime,userId);

        LocalDateTime createdAt = null;
        if (sightCheck != null && presbyopiaCheck != null) {
            createdAt = sightCheck.getCreatedAt().isAfter(presbyopiaCheck.getCreatedAt())
                ? sightCheck.getCreatedAt()
                : presbyopiaCheck.getCreatedAt();
        } else if (sightCheck != null) {
            createdAt = sightCheck.getCreatedAt();
        } else if (presbyopiaCheck != null) {
            createdAt = presbyopiaCheck.getCreatedAt();
        }

        return VisionResponse.builder()
            .name(user.getName())
            .age(presbyopiaCheck == null ? -1 : presbyopiaCheck.getAge())
            .leftSight(sightCheck == null ? -1 : sightCheck.getLeftSight())
            .rightSight(sightCheck == null ? -1 : sightCheck.getRightSight())
            .createdAt(createdAt)
            .build();
    }
}
