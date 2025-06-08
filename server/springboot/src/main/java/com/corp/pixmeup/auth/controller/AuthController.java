package com.corp.pixmeup.auth.controller;

import com.corp.pixmeup.auth.dto.*;
import com.corp.pixmeup.auth.entity.RefreshToken;
import com.corp.pixmeup.auth.service.AuthService;
import com.corp.pixmeup.global.error.code.ErrorCode;
import com.corp.pixmeup.global.error.dto.ErrorResponse;
import com.corp.pixmeup.global.error.exception.BusinessException;
import com.corp.pixmeup.global.response.GlobalResponse;
import com.corp.pixmeup.global.response.TokenResponse;
import com.corp.pixmeup.global.util.JwtTokenProvider;
import com.corp.pixmeup.global.vo.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.headers.Header;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Tag(name = "인증", description = "인증 관련 API")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class AuthController {

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    @Operation(
            summary = "휴대폰 인증 코드 발송",
            description = "입력된 휴대폰 번호로 인증 코드를 발송합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "인증 코드 발송 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "SendCodeSuccess",
                                    summary = "인증 코드 발송 성공 예시",
                                    value = """
                                            {
                                              "status": 200,
                                              "message": "Success",
                                              "data": {
                                                "success": true,
                                                "expiresInSeconds": 180
                                              },
                                              "timestamp": "2025-05-08T09:51:02"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "429",
                    description = "너무 많은 요청",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ErrorResponse.class)
                    )
            )
    })
    @PostMapping("/send-code")
    public ResponseEntity<GlobalResponse<PhoneAuthSendCodeResponse>> sendVerificationCode(
            @RequestBody PhoneAuthSendCodeRequest request
    ) {
        PhoneAuthSendCodeResponse response = authService.sendVerificationCode(request);
        return ResponseEntity.ok()
                .body(GlobalResponse.success(response));
    }

    @Operation(
            summary = "휴대폰 인증 코드 확인",
            description = "입력된 인증 코드가 유효한지 확인합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "인증 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "VerifyCodeSuccess",
                                    summary = "인증 코드 확인 성공 예시",
                                    value = """
                                            {
                                              "status": 200,
                                              "message": "Success",
                                              "data": {
                                                "success": true
                                              },
                                              "timestamp": "2025-05-08T09:51:02"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 또는 인증 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "429",
                    description = "너무 많은 요청",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/verify-code")
    public ResponseEntity<GlobalResponse<PhoneAuthVerifyCodeResponse>> verifyCode(
            @RequestBody PhoneAuthVerifyCodeRequest request
    ) {
        PhoneAuthVerifyCodeResponse response = authService.verifyCode(request);
        return ResponseEntity.ok()
                .body(GlobalResponse.success(response));
    }

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(
            summary = "회원 가입",
            description = "사용자 정보를 입력받아 신규 회원으로 등록합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "회원가입 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "RegisterSuccess",
                                    summary = "회원가입 성공 예시",
                                    value = """
                                            {
                                              "status": 201,
                                              "message": "Created",
                                              "data": {
                                                "userId": 1,
                                                "name": "홍길동"
                                              },
                                              "timestamp": "2025-05-08T09:51:02"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "데이터 충돌",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/register")
    public ResponseEntity<GlobalResponse<RegisterResponse>> register(
            @Valid @RequestBody RegisterRequest request
    ) {
        RegisterResponse response = authService.registerUser(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(GlobalResponse.created(response));
    }

    @Operation(summary = "로그인", description = "휴대폰 번호와 비밀번호를 사용하여 로그인하고, 성공 시 토큰을 발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "로그인 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "LoginSuccess",
                                    summary = "로그인 성공 예시",
                                    value = """
                                            {
                                              "status": 200,
                                              "message": "Success",
                                              "data": {
                                                "userId": 1,
                                                "name": "홍길동",
                                                "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                                              },
                                              "timestamp": "2025-05-08T09:51:02"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/login")
    public ResponseEntity<GlobalResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request
    ) {
        TokenResponse<LoginResponse> response = authService.loginUser(request);
        RefreshToken refreshToken = response.refreshToken();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", refreshToken.getToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshToken.getTtl())
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(GlobalResponse.success(response.data()));
    }

    @Operation(
            summary = "로그아웃",
            description = "현재 로그인된 사용자를 로그아웃 처리하고, 관련된 토큰을 무효화합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "로그아웃 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "LogoutSuccess",
                                    summary = "로그아웃 성공 예시",
                                    value = """
                                            {
                                              "status": 200,
                                              "message": "로그아웃 되었습니다.",
                                              "data": null,
                                              "timestamp": "2025-05-08T09:51:02"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 사용자",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/logout")
    public ResponseEntity<GlobalResponse<Void>> logout(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request
    ) {
        String accessToken = jwtTokenProvider.resolveToken(request);
        authService.logout(userDetails.getUserId(), accessToken);

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", "")
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(0)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(GlobalResponse.success("로그아웃 되었습니다.", null));
    }

    @Operation(
            summary = "토큰 갱신",
            description = "Refresh Token을 사용하여 새로운 Access Token과 Refresh Token을 발급받습니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "토큰 갱신 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "TokenRefreshSuccess",
                                    summary = "토큰 갱신 성공 예시",
                                    value = """
                                            {
                                              "status": 200,
                                              "message": "Success",
                                              "data": {
                                                "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                                              },
                                              "timestamp": "2025-05-08T09:51:02"
                                            }
                                            """
                            )
                    ),
                    headers = @Header(
                            name = HttpHeaders.SET_COOKIE,
                            description = "새로운 Refresh Token이 HttpOnly, Secure 쿠키로 설정됩니다."
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Refresh Token 쿠키 누락",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "유효하지 않은 Refresh Token",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<GlobalResponse<TokenRefreshResponse>> refreshToken(
            @CookieValue(name = "refreshToken", required = false) String refreshToken
    ) {
        ValidateRefreshTokenResult result = authService.validateRefreshToken(refreshToken);

        if (!result.success()) {
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }

        String newAccessToken = result.accessToken();
        RefreshToken newRefreshToken = result.refreshToken();

        ResponseCookie refreshTokenCookie = ResponseCookie.from("refreshToken", newRefreshToken.getToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(newRefreshToken.getTtl())
                .sameSite("Strict")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, refreshTokenCookie.toString())
                .body(GlobalResponse.success(new TokenRefreshResponse(newAccessToken)));
    }
}
