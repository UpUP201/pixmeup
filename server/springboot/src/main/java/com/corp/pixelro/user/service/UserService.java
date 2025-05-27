package com.corp.pixelro.user.service;

import com.corp.pixelro.user.dto.*;
import com.corp.pixelro.user.entity.User;

public interface UserService {

    User getUser(Long userId);

    void deleteUser(Long userId);

    UserProfileResponse getUserProfile(Long userId);

    UserProfileResponse updateUserProfile(Long userId, UserProfileUpdateRequest request);
    
    UserProfileResponse updateUserPassword(Long userId, UserPasswordUpdateRequest request);

    RecentExaminationResponse getRecentExaminationResults(Long userId);

    Integer getRecentDate(Long userId);

    TotalUserInfoResponse getTotalUserInfo(Long userId);
}
