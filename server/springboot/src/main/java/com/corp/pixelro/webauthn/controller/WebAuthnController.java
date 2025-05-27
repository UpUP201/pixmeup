package com.corp.pixelro.webauthn.controller;

import com.corp.pixelro.auth.entity.RefreshToken;
import com.corp.pixelro.global.error.dto.ErrorResponse;
import com.corp.pixelro.global.response.GlobalResponse;
import com.corp.pixelro.global.response.TokenResponse;
import com.corp.pixelro.global.vo.CustomUserDetails;
import com.corp.pixelro.webauthn.dto.*;
import com.corp.pixelro.webauthn.service.WebAuthnService;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@Tag(name = "WebAuthn", description = "WebAuthn 인증 관련 API")
@RestController
@RequestMapping("/api/v1/webauthn")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearer-key")
public class WebAuthnController {

    private final WebAuthnService webAuthnService;

    @Value("${app.cookie.domain}")
    private String cookieDomain;

    @Operation(summary = "WebAuthn 등록 시작", description = "새로운 WebAuthn 인증정보 등록을 시작합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "등록 시작 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "RegistrationStartExample",
                                    summary = "WebAuthn 등록 시작 예시",
                                    value = """
                                            {
                                              "status": 200,
                                              "message": "Success",
                                              "data": {
                                                "data": {
                                                  "challenge": "UUSEUYTLEhzsvI67PXlMePPtuLKnvijkVir0pcOF_3A",
                                                  "rp": {
                                                    "id": "localhost",
                                                    "name": "upup"
                                                  },
                                                  "user": {
                                                    "id": "a4ayc_80_OGda4BO_1o_V0etpOqiLx1JwB5S3beHW0s",
                                                    "name": "01012345678",
                                                    "displayName": "qqq"
                                                  },
                                                  "pubKeyCredParams": [
                                                    {"type": "public-key", "alg": -7},
                                                    {"type": "public-key", "alg": -8}
                                                  ],
                                                  "timeout": 120000,
                                                  "excludeCredentials": [],
                                                  "authenticatorSelection": {
                                                    "authenticatorAttachment": "platform",
                                                    "userVerification": "preferred",
                                                    "residentKey": "preferred"
                                                  },
                                                  "attestation": "none"
                                                },
                                                "sessionId": "da4c5bfb-9f35-4666-984e-319645463f76"
                                              },
                                              "timestamp": "2025-05-08 09:51:02"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/register/start")
    public ResponseEntity<GlobalResponse<WebAuthnResponse<RegistrationStartResponse>>> startRegistration(
            @RequestBody RegistrationStartRequest request) {
        WebAuthnResponse<RegistrationStartResponse> response = webAuthnService.startRegistration(request.userId());
        return ResponseEntity.ok()
                .body(GlobalResponse.success(response));
    }

    @Operation(
            summary = "WebAuthn 등록 완료",
            description = "WebAuthn 인증정보 등록을 완료합니다."
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "등록 완료 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "RegistrationFinishExample",
                                    summary = "WebAuthn 등록 완료 예시",
                                    value = """
                                            {
                                              "status": 200,
                                              "message": "Success",
                                              "data": {
                                                "data": {
                                                  "credentialId": "credential-123",
                                                  "deviceType": "platform",
                                                  "createdAt": "2024-03-14T12:00:00"
                                                },
                                                "sessionId": "da4c5bfb-9f35-4666-984e-319645463f76"
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
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @PostMapping("/register/finish")
    public ResponseEntity<GlobalResponse<WebAuthnResponse<RegistrationFinishResponse>>> finishRegistration(
            @Valid @RequestBody RegistrationFinishRequest request) {
        WebAuthnResponse<RegistrationFinishResponse> response = webAuthnService.finishRegistration(request);
        return ResponseEntity.ok()
                .body(GlobalResponse.success(response));
    }

    @Operation(summary = "WebAuthn 인증 시작", description = "WebAuthn 인증을 시작합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "인증 시작 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    name = "AuthenticationStartExample",
                                    summary = "WebAuthn 인증 시작 예시",
                                    value = """
                                            {
                                              "status": 200,
                                              "message": "Success",
                                              "data": {
                                                  "data": {
                                                    "challenge": "challenge-123",
                                                    "timeout": 60000,
                                                    "rp": {
                                                      "id": "example.com",
                                                      "name": "Example RP"
                                                    },
                                                    "allowCredentials": [
                                                      {
                                                        "type": "public-key",
                                                        "id": "credentialId-abc123"
                                                      }
                                                    ],
                                                  }
                                                },
                                                "sessionId": "dddddd"
                                                "userVerification": "preferred"
                                              },
                                              "timestamp": "2025-05-08T09:51:02"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증되지 않은 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/authenticate/start")
    public ResponseEntity<GlobalResponse<WebAuthnResponse<AuthenticationStartResponse>>> startAuthentication(
            @Valid @RequestBody(required = false) AuthenticationRequest request) {
        WebAuthnResponse<AuthenticationStartResponse> response = webAuthnService.startAuthentication(request);
        return ResponseEntity.ok()
                .body(GlobalResponse.success(response));
    }

    @Operation(summary = "WebAuthn 인증 완료", description = "WebAuthn 인증을 완료하고 액세스 토큰을 발급합니다.")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "인증 완료 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalResponse.class),
                            examples = @ExampleObject(
                                    name = "AuthenticationFinishSuccess",
                                    summary = "WebAuthn 인증 완료 성공 예시",
                                    value = """
                                            {
                                              "status": 200,
                                              "message": "Success",
                                              "data": {
                                                "userId": 1,
                                                "userName": "홍길동",
                                                "credentialId": "8xbz8K0mOeC_-27fjisYaXe5itLXFzrGMZWjL_8XmJY",
                                                "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9..."
                                              },
                                              "timestamp": "2025-05-08T09:51:02"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(responseCode = "400", description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    @PostMapping("/authenticate/finish")
    public ResponseEntity<GlobalResponse<AuthenticationFinishResponse>> finishAuthentication(
            @Valid @RequestBody AuthenticationFinishRequest request) {
        TokenResponse<AuthenticationFinishResponse> response = webAuthnService.finishAuthentication(request);
        RefreshToken refreshToken = response.refreshToken();

        ResponseCookie authTokenCookie = ResponseCookie.from("refreshToken", refreshToken.getToken())
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(refreshToken.getTtl())
                .domain(cookieDomain)
                .sameSite("Lax")
                .build();

        return ResponseEntity.ok()
                .header("Set-Cookie", authTokenCookie.toString())
                .body(GlobalResponse.success(response.data()));
    }

    @Operation(
            summary = "WebAuthn 인증정보 목록 조회",
            description = "사용자의 등록된 WebAuthn 인증정보 목록을 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalResponse.class),
                            examples = @ExampleObject(
                                    name = "WebAuthnCredentialListSuccess",
                                    summary = "WebAuthn 인증정보 목록 조회 성공 예시",
                                    value = """
                                            {
                                              "status": 200,
                                              "message": "Success",
                                              "data": [
                                                {
                                                  "id": 1,
                                                  "credentialId": "credential-123",
                                                  "deviceName": "iPhone 12",
                                                  "deviceType": "platform",
                                                  "createdAt": "2024-03-14T12:00:00",
                                                  "lastUsedAt": "2024-03-14T12:30:00",
                                                  "aaguid": "00000000-0000-0000-0000-000000000000",
                                                  "transports": ["internal"],
                                                  "status": "REGISTERED"
                                                },
                                                {
                                                  "id": 2,
                                                  "credentialId": "credential-456",
                                                  "deviceName": "Galaxy S23",
                                                  "deviceType": "cross-platform",
                                                  "createdAt": "2024-03-15T09:00:00",
                                                  "lastUsedAt": "2024-03-15T10:00:00",
                                                  "aaguid": "11111111-2222-3333-4444-555555555555",
                                                  "transports": ["usb", "nfc"],
                                                  "status": "REGISTERED"
                                                }
                                              ],
                                              "timestamp": "2025-05-08T09:51:02"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @GetMapping("/credentials")
    public ResponseEntity<GlobalResponse<List<WebAuthnCredentialDTO>>> getCredentials(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        WebAuthnResponse<List<WebAuthnCredentialDTO>> response = webAuthnService.getCredentials(userDetails.getUserId());
        return ResponseEntity.ok()
                .body(GlobalResponse.success(response.getData()));
    }

    @Operation(
            summary = "WebAuthn 인증정보 삭제",
            description = "등록된 WebAuthn 인증정보를 삭제합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "삭제 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = GlobalResponse.class),
                            examples = @ExampleObject(
                                    name = "WebAuthnCredentialDeleteSuccess",
                                    summary = "WebAuthn 인증정보 삭제 성공 예시",
                                    value = """
                                            {
                                              "status": 200,
                                              "message": "Success",
                                              "data": "인증 정보 삭제 완료"
                                              "timestamp": "2025-05-08T09:51:02"
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "인증되지 않은 요청",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "인증정보를 찾을 수 없음",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            ),
            @ApiResponse(
                    responseCode = "500",
                    description = "서버 내부 오류",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
    })
    @DeleteMapping("/credentials")
    public ResponseEntity<GlobalResponse<String>> revokeCredential(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody RevokeCredentialRequest request) {
        webAuthnService.revokeCredential(userDetails.getUserId(), request.credentialId());
        return ResponseEntity.ok()
                .body(GlobalResponse.success("인증 정보 삭제 완료"));
    }
} 