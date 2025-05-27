package com.corp.pixelro.loadtest;

import com.corp.pixelro.global.response.GlobalResponse;
import com.corp.pixelro.user.dto.UserProfileResponse;
import com.corp.pixelro.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/loadtest")
@Profile("!prod")
@RequiredArgsConstructor
public class LoadTestController {

    private final UserService userService;

    @GetMapping("/ping")
    public String ping() {
        return "ok";
    }

    @GetMapping("/users/{userId}")
    public ResponseEntity<GlobalResponse<UserProfileResponse>> getSimpleUserForTest(
            @PathVariable("userId") Long userId) {
        UserProfileResponse response = UserProfileResponse.fromUserOnly(userService.getUser(userId), 0);
        return ResponseEntity.ok()
                .body(GlobalResponse.success(response));
    }
}
