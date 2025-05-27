package com.corp.pixelro.user.controller;

import com.corp.pixelro.global.error.dto.ErrorResponse;
import com.corp.pixelro.global.response.GlobalResponse;
import com.corp.pixelro.global.vo.CustomUserDetails;
import com.corp.pixelro.user.dto.*;
import com.corp.pixelro.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "User API", description = "사용자 관련 API - 회원 프로필 관리, 비밀번호 변경, 검사 결과 조회 등")
public class UserController {

    private final UserService userService;

    @Operation(
            summary = "회원 탈퇴",
            description = "현재 로그인된 사용자의 계정을 삭제(탈퇴)합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "회원 탈퇴 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "DeleteUserSuccess",
                                    summary = "회원 탈퇴 성공 예시",
                                    value = """
                                            {
                                              "status": 200,
                                              "message": "Success",
                                              "data": null,
                                              "timestamp": "2025-05-08T09:51:02"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @DeleteMapping("/")
    public ResponseEntity<GlobalResponse<Void>> deleteUser(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        userService.deleteUser(userDetails.getUserId());

        return ResponseEntity.ok()
                .body(GlobalResponse.success(null));
    }

    @Operation(
            summary = "회원 프로필 조회",
            description = "현재 로그인된 사용자의 프로필을 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "프로필 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "GetProfileSuccess",
                                            summary = "프로필 조회 성공 예시 (문진 데이터 있음)",
                                            value = """
                                                    {
                                                      "status": 200,
                                                      "message": "Success",
                                                      "data": {
                                                        "name": "홍길동",
                                                        "daysSinceCheck": 3,
                                                        "phoneNumber": "010-1234-5678",
                                                        "gender": "남성",
                                                        "age": 25,
                                                        "glasses": true,
                                                        "surgery": "없음",
                                                        "smoking": false,
                                                        "diabetes": false
                                                      },
                                                      "timestamp": "2025-05-08T09:51:02"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "GetProfileSuccessNoSurvey",
                                            summary = "프로필 조회 성공 예시 (문진 데이터 없음)",
                                            value = """
                                                    {
                                                      "status": 200,
                                                      "message": "Success",
                                                      "data": {
                                                        "name": "홍길동",
                                                        "daysSinceCheck": -1,
                                                        "phoneNumber": "010-1234-5678",
                                                        "gender": null,
                                                        "age": null,
                                                        "glasses": null,
                                                        "surgery": null,
                                                        "smoking": null,
                                                        "diabetes": null
                                                      },
                                                      "timestamp": "2025-05-08T09:51:02"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "사용자를 찾을 수 없음",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/profile")
    public ResponseEntity<GlobalResponse<UserProfileResponse>> getUserProfile(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserProfileResponse response = userService.getUserProfile(userDetails.getUserId());

        return ResponseEntity.ok()
                .body(GlobalResponse.success(response));
    }

    @Operation(
            summary = "회원 프로필 수정",
            description = "현재 로그인된 사용자의 프로필을 수정합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "프로필 수정 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "UpdateProfileSuccess",
                                            summary = "프로필 수정 성공 예시 (문진 데이터 있음)",
                                            value = """
                                                    {
                                                      "status": 200,
                                                      "message": "Success",
                                                      "data": {
                                                        "name": "홍길동",
                                                        "daysSinceCheck": 3,
                                                        "phoneNumber": "010-1234-5678",
                                                        "gender": "남성",
                                                        "age": 25,
                                                        "glasses": true,
                                                        "surgery": "없음",
                                                        "smoking": false,
                                                        "diabetes": false
                                                      },
                                                      "timestamp": "2025-05-08T09:51:02"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "UpdateProfileSuccessNoSurvey",
                                            summary = "프로필 수정 성공 예시 (문진 데이터 없음)",
                                            value = """
                                                    {
                                                      "status": 200,
                                                      "message": "Success",
                                                      "data": {
                                                        "name": "홍길동",
                                                        "daysSinceCheck": -1,
                                                        "phoneNumber": "010-1234-5678",
                                                        "gender": null,
                                                        "age": null,
                                                        "glasses": null,
                                                        "surgery": null,
                                                        "smoking": null,
                                                        "diabetes": null
                                                      },
                                                      "timestamp": "2025-05-08T09:51:02"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "사용자를 찾을 수 없음",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PutMapping("/profile")
    public ResponseEntity<GlobalResponse<UserProfileResponse>> updateUserProfile(
            @Valid @RequestBody UserProfileUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserProfileResponse response = userService.updateUserProfile(userDetails.getUserId(), request);

        return ResponseEntity.ok()
                .body(GlobalResponse.success(response));
    }

    @Operation(
            summary = "비밀번호 변경",
            description = "현재 로그인된 사용자의 비밀번호를 변경합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "비밀번호 변경 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "UpdatePasswordSuccess",
                                            summary = "비밀번호 변경 성공 예시 (문진 데이터 있음)",
                                            value = """
                                                    {
                                                      "status": 200,
                                                      "message": "Success",
                                                      "data": {
                                                        "name": "홍길동",
                                                        "daysSinceCheck": 3,
                                                        "phoneNumber": "010-1234-5678",
                                                        "gender": "남성",
                                                        "age": 25,
                                                        "glasses": true,
                                                        "surgery": "없음",
                                                        "smoking": false,
                                                        "diabetes": false
                                                      },
                                                      "timestamp": "2025-05-08T09:51:02"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "UpdatePasswordSuccessNoSurvey",
                                            summary = "비밀번호 변경 성공 예시 (문진 데이터 없음)",
                                            value = """
                                                    {
                                                      "status": 200,
                                                      "message": "Success",
                                                      "data": {
                                                        "name": "홍길동",
                                                        "daysSinceCheck": -1,
                                                        "phoneNumber": "010-1234-5678",
                                                        "gender": null,
                                                        "age": null,
                                                        "glasses": null,
                                                        "surgery": null,
                                                        "smoking": null,
                                                        "diabetes": null
                                                      },
                                                      "timestamp": "2025-05-08T09:51:02"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 또는 현재 비밀번호가 일치하지 않음",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "사용자를 찾을 수 없음",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PatchMapping("/password")
    public ResponseEntity<GlobalResponse<UserProfileResponse>> updateUserPassword(
            @Valid @RequestBody UserPasswordUpdateRequest request,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        UserProfileResponse response = userService.updateUserPassword(userDetails.getUserId(), request);

        return ResponseEntity.ok()
                .body(GlobalResponse.success(response));
    }

    @Operation(
            summary = "최근 검사 결과 조회",
            description = "현재 로그인된 사용자의 최근 검사 결과를 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "검사 결과 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "GetRecentExaminationSuccess",
                                            summary = "최근 검사 결과 조회 성공 예시",
                                            value = """
                                                    {
                                                      "status": 200,
                                                      "message": "Success",
                                                      "data": {
                                                        "age": 25,
                                                        "leftSight": 10,
                                                        "rightSight": 10,
                                                        "amslerStatus": "정상",
                                                        "leftEyeVer": 5,
                                                        "rightEyeVer": 5,
                                                        "leftEyeHor": 5,
                                                        "rightEyeHor": 5
                                                      },
                                                      "timestamp": "2025-05-08T09:51:02"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "GetRecentExaminationNoResults",
                                            summary = "검사 결과가 없는 경우 예시",
                                            value = """
                                                    {
                                                      "status": 200,
                                                      "message": "Success",
                                                      "data": {
                                                        "age": null,
                                                        "leftSight": null,
                                                        "rightSight": null,
                                                        "amslerStatus": null,
                                                        "leftEyeVer": null,
                                                        "rightEyeVer": null,
                                                        "leftEyeHor": null,
                                                        "rightEyeHor": null
                                                      },
                                                      "timestamp": "2025-05-08T09:51:02"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "사용자를 찾을 수 없음",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/result")
    public ResponseEntity<GlobalResponse<RecentExaminationResponse>> getRecentExaminationResults(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        RecentExaminationResponse response = userService.getRecentExaminationResults(userDetails.getUserId());

        return ResponseEntity.ok()
                .body(GlobalResponse.success(response));
    }

    @Operation(
            summary = "최근 검사일 조회",
            description = "현재 로그인된 사용자의 최근 검사일로부터 경과된 일수를 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "최근 검사일 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = {
                                    @ExampleObject(
                                            name = "GetRecentDateSuccess",
                                            summary = "최근 검사일 조회 성공 예시",
                                            value = """
                                                    {
                                                      "status": 200,
                                                      "message": "Success",
                                                      "data": 3,
                                                      "timestamp": "2025-05-08T09:51:02"
                                                    }
                                                    """
                                    ),
                                    @ExampleObject(
                                            name = "GetRecentDateNoCheck",
                                            summary = "검사 기록이 없는 경우 예시",
                                            value = """
                                                    {
                                                      "status": 200,
                                                      "message": "Success",
                                                      "data": -1,
                                                      "timestamp": "2025-05-08T09:51:02"
                                                    }
                                                    """
                                    )
                            }
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 사용자",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "사용자를 찾을 수 없음",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/recent-date")
    public ResponseEntity<GlobalResponse<Integer>> getRecentDate(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Integer response = userService.getRecentDate(userDetails.getUserId());

        return ResponseEntity.ok()
                .body(GlobalResponse.success(response));
    }

    @GetMapping("/total-info")
    public ResponseEntity<GlobalResponse<TotalUserInfoResponse>> getTotalUserInfo(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        TotalUserInfoResponse response = userService.getTotalUserInfo(userDetails.getUserId());

        return ResponseEntity.ok()
                .body(GlobalResponse.success(response));
    }
}
