package com.corp.pixelro.webauthn.service;

import com.corp.pixelro.auth.entity.RefreshToken;
import com.corp.pixelro.auth.repository.RefreshTokenRepository;
import com.corp.pixelro.global.error.code.ErrorCode;
import com.corp.pixelro.global.error.exception.BusinessException;
import com.corp.pixelro.global.response.TokenResponse;
import com.corp.pixelro.global.util.JwtTokenProvider;
import com.corp.pixelro.global.util.WebAuthnUtils;
import com.corp.pixelro.user.entity.User;
import com.corp.pixelro.user.repository.UserRepository;
import com.corp.pixelro.webauthn.dto.*;
import com.corp.pixelro.webauthn.entity.WebAuthnCredential;
import com.corp.pixelro.webauthn.entity.WebAuthnCredential.CredentialStatus;
import com.corp.pixelro.webauthn.entity.WebAuthnSession;
import com.corp.pixelro.webauthn.repository.WebAuthnCredentialRepository;
import com.corp.pixelro.webauthn.repository.WebAuthnSessionRepository;
import com.corp.pixelro.webauthn.vo.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.yubico.webauthn.*;
import com.yubico.webauthn.data.*;
import com.yubico.webauthn.exception.AssertionFailedException;
import com.yubico.webauthn.exception.RegistrationFailedException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class WebAuthnServiceImpl implements WebAuthnService {

    private final WebAuthnCredentialRepository credentialRepository;
    private final WebAuthnSessionRepository sessionRepository;
    private final UserRepository userRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final ObjectMapper objectMapper;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${webauthn.rp.id}")
    private String rpId;

    @Value("${webauthn.rp.name}")
    private String rpName;

    @Value("${webauthn.rp.origin}")
    private String rpOrigin;

    /**
     * <p>WebAuthn 등록 시작</p>
     * <p>1. 사용자 정보 가져오기</p>
     * <p>2. 기존 인증 키 찾기</p>
     * <p>3. 등록 옵션 생성</p>
     * <p>4. 세션에 등록 옵션 저장</p>
     * <p>5. 클라이언트에 등록 옵션 반환</p>
     */
    public WebAuthnResponse<RegistrationStartResponse> startRegistration(Long userId) {
        try {
            log.info("WebAuthn 등록 시작: userId={}", userId);

            // 1. 사용자 정보 가져오기
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

            // 2. 기존 인증 키 찾기 (삭제된 것 제외)
            List<WebAuthnCredential> existingCredentials = credentialRepository.findAllByUserIdAndStatusNot(
                    user.getId(), CredentialStatus.REVOKED);

            // 3. 실행 컨텍스트 생성 (RelyingParty)
            RelyingParty relyingParty = getRelyingParty();

            // 4. 사용자 인증 정보 생성
            ByteArray userHandle = WebAuthnUtils.createUserHandle(user.getId().toString());

            UserIdentity userIdentity = UserIdentity.builder()
                    .name(user.getPhoneNumber())
                    .displayName(user.getName())
                    .id(userHandle)
                    .build();

            // 6. 인증기기 선택 옵션 설정
            AuthenticatorSelectionCriteria selectionCriteria = AuthenticatorSelectionCriteria.builder()
                    .authenticatorAttachment(AuthenticatorAttachment.PLATFORM)
                    .residentKey(ResidentKeyRequirement.PREFERRED)
                    .userVerification(UserVerificationRequirement.PREFERRED)
                    .build();

            // 7. 등록 옵션 생성
            StartRegistrationOptions registrationOptions = StartRegistrationOptions.builder()
                    .user(userIdentity)
                    .timeout(120000L) // 2분
                    .authenticatorSelection(selectionCriteria)
                    .build();

            PublicKeyCredentialCreationOptions options = relyingParty.startRegistration(registrationOptions);

            // 8. 세션에 등록 옵션 저장
            String sessionId = UUID.randomUUID().toString();
            WebAuthnSession session = WebAuthnSession.builder()
                    .id(sessionId)
                    .userId(user.getId())
                    .challenge(WebAuthnUtils.base64UrlEncode(options.getChallenge()))
                    .operation("REGISTRATION")
                    .requestJson(objectMapper.writeValueAsString(options))
                    .clientIp(getClientIp())
                    .userAgent(getUserAgent())
                    .createdAt(System.currentTimeMillis())
                    .build();

            sessionRepository.save(session);
            log.info("등록 세션 생성: sessionId={}", sessionId);

            // 9. 클라이언트 응답 생성
            String challenge = WebAuthnUtils.base64UrlEncode(options.getChallenge());

            RpInfo rp = new RpInfo(
                    options.getRp().getId(),
                    options.getRp().getName()
            );

            UserInfo userInfo = new UserInfo(
                    WebAuthnUtils.base64UrlEncode(options.getUser().getId()),
                    options.getUser().getName(),
                    options.getUser().getDisplayName()
            );

            List<PubKeyCredParam> pubKeyCredParams = options.getPubKeyCredParams().stream()
                    .map(param -> new PubKeyCredParam(
                            param.getType().getId(),
                            param.getAlg().getId()
                    ))
                    .toList();

            long timeout = options.getTimeout().get();

            List<CredentialDescriptor> excludeCredentials = existingCredentials.stream()
                    .map(cred -> new CredentialDescriptor(
                            PublicKeyCredentialType.PUBLIC_KEY.getId(),
                            cred.getCredentialId(),
                            cred.getTransportList()
                    ))
                    .toList();

            String authenticatorAttachment = "";
            if (selectionCriteria.getAuthenticatorAttachment().isPresent()) {
                authenticatorAttachment = selectionCriteria.getAuthenticatorAttachment().get().getValue();
            }

            AuthenticatorSelection authenticatorSelection = new AuthenticatorSelection(
                    authenticatorAttachment,
                    selectionCriteria.getUserVerification().get().getValue(),
                    selectionCriteria.getResidentKey().get().getValue()
            );

            String attestation = options.getAttestation().getValue();

            RegistrationStartResponse response = new RegistrationStartResponse(
                    challenge,
                    rp,
                    userInfo,
                    pubKeyCredParams,
                    timeout,
                    excludeCredentials,
                    authenticatorSelection,
                    attestation
            );

            return WebAuthnResponse.success(response, sessionId);
        } catch (Exception e) {
            log.error("WebAuthn 등록 시작 오류", e);
            throw new BusinessException(ErrorCode.INVALID_AUTH_TYPE);
        }
    }

    /**
     * <p>WebAuthn 등록 완료</p>
     * <p>1. 세션 정보 가져오기</p>
     * <p>2. 클라이언트 응답 데이터 파싱</p>
     * <p>3. 등록 완료 요청 생성</p>
     * <p>4. 등록 완료 처리</p>
     * <p>5. 인증정보 저장</p>
     * <p>6. 세션 삭제</p>
     * <p>7. 결과 반환</p>
     */
    @Transactional
    public WebAuthnResponse<RegistrationFinishResponse> finishRegistration(RegistrationFinishRequest request) {
        try {
            log.info("WebAuthn 등록 완료 시작: sessionId={}", request.sessionId());

            // 1. 세션 정보 가져오기
            WebAuthnSession session = sessionRepository.findById(request.sessionId())
                    .orElseThrow(() -> new IllegalArgumentException("등록 세션을 찾을 수 없습니다: " + request.sessionId()));

            if (!"REGISTRATION".equals(session.getOperation())) {
                throw new IllegalStateException("잘못된 세션 작업: " + session.getOperation());
            }

            // 2. 사용자 정보 가져오기
            User user = userRepository.findById(session.getUserId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND));

            // 3. 실행 컨텍스트 생성
            RelyingParty relyingParty = getRelyingParty();

            // 4. 클라이언트 응답 데이터 파싱
            PublicKeyCredentialCreationOptions requestOptions = objectMapper.readValue(
                    session.getRequestJson(), PublicKeyCredentialCreationOptions.class);

            // 표준 구조만 추출해서 JSON 생성
            CredentialInfo cred = request.credential();
            ObjectNode node = objectMapper.createObjectNode();
            node.put("id", cred.id());
            node.put("rawId", cred.rawId());
            node.put("type", cred.type());
            node.set("response", objectMapper.valueToTree(cred.response()));
            node.set("clientExtensionResults", cred.clientExtensionResults());
            String clientDataJson = objectMapper.writeValueAsString(node);

            PublicKeyCredential<AuthenticatorAttestationResponse, ClientRegistrationExtensionOutputs> pkc =
                    PublicKeyCredential.parseRegistrationResponseJson(clientDataJson);

            // 5. 등록 완료 요청 생성
            FinishRegistrationOptions finishOptions = FinishRegistrationOptions.builder()
                    .request(requestOptions)
                    .response(pkc)
                    .build();

            // 6. 등록 완료 처리
            RegistrationResult result = relyingParty.finishRegistration(finishOptions);

            // 7. 인증정보 엔티티 생성
            String credentialId = WebAuthnUtils.base64UrlEncode(result.getKeyId().getId());

            // 중복 체크
            if (credentialRepository.existsByCredentialId(credentialId)) {
                throw new IllegalStateException("이미 등록된 인증정보입니다: " + credentialId);
            }

            AuthenticatorAttestationResponse response = pkc.getResponse();
            String deviceName = request.deviceName();
            if (deviceName == null || deviceName.isEmpty()) {
                deviceName = "등록된 인증키 #" + (credentialRepository.findAllByUserIdAndStatusNot(
                        user.getId(), CredentialStatus.REVOKED).size() + 1);
            }

            // 기기 유형 추정
            String deviceType = "unknown";
            if (result.getKeyId().getTransports().isPresent()) {
                List<String> transports = result.getKeyId().getTransports().get().stream()
                        .map(AuthenticatorTransport::getId)
                        .toList();

                if (transports.contains("internal")) {
                    deviceType = "platform";
                } else if (transports.contains("usb") || transports.contains("nfc") || transports.contains("ble")) {
                    deviceType = "cross-platform";
                }
            }

            // AAGUID 추출
            String aaguid = response.getAttestation().getAuthenticatorData().getAttestedCredentialData()
                    .map(data -> data.getAaguid().getBase64())
                    .orElse(null);

            WebAuthnCredential credential = WebAuthnCredential.builder()
                    .credentialId(credentialId)
                    .user(user)
                    .name(user.getPhoneNumber())
                    .publicKeyCose(result.getPublicKeyCose().getBytes())
                    .signatureCount(result.getSignatureCount())
                    .attestationType(result.getAttestationType().name())
                    .lastUsedAt(LocalDateTime.now())
                    .aaguid(aaguid)
                    .deviceName(deviceName)
                    .deviceType(deviceType)
                    .status(CredentialStatus.ACTIVE)
                    .build();

            // transports 설정
            if (result.getKeyId().getTransports().isPresent()) {
                List<String> transportList = result.getKeyId().getTransports().get().stream()
                        .map(AuthenticatorTransport::getId)
                        .toList();
                credential.setTransportList(transportList);
            }

            credential = credentialRepository.save(credential);
            log.info("새 인증정보 저장: credentialId={}, userId={}", credential.getCredentialId(), user.getId());

            // 8. 세션 삭제
            sessionRepository.deleteById(session.getId());

            // 9. 결과 반환
            return WebAuthnResponse.success(new RegistrationFinishResponse(
                    credentialId,
                    deviceType,
                    LocalDateTime.now()
            ));

        } catch (RegistrationFailedException e) {
            log.error("WebAuthn 등록 검증 실패", e);
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        } catch (IOException e) {
            log.error("WebAuthn 등록 완료 JSON 처리 오류", e);
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        } catch (Exception e) {
            log.error("WebAuthn 등록 완료 오류", e);
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        }
    }

    /**
     * <p>WebAuthn 인증 시작</p>
     * <p>1. 사용자 정보 가져오기</p>
     * <p>2. 사용자의 인증정보 가져오기</p>
     * <p>3. 인증 옵션 생성</p>
     * <p>4. 세션에 인증 옵션 저장</p>
     * <p>5. 클라이언트에 인증 옵션 반환</p>
     */
    public WebAuthnResponse<AuthenticationStartResponse> startAuthentication(AuthenticationRequest request) {
        try {
            log.info("WebAuthn 간편 로그인 인증 시작");

            // 간편 로그인 시에는 특정 사용자를 미리 조회하지 않음

            // 1. 실행 컨텍스트 생성 (RelyingParty)
            RelyingParty relyingParty = getRelyingParty();

            // 2. 사용자 검증 요구사항 설정
            UserVerificationRequirement uvr = UserVerificationRequirement.PREFERRED;
            if (request.userVerification() != null && !request.userVerification().isEmpty()) {
                try {
                    uvr = UserVerificationRequirement.valueOf(request.userVerification().toUpperCase());
                } catch (IllegalArgumentException e) {
                    log.warn("잘못된 userVerification 값: {}", request.userVerification());
                }
            }

            // 3. 인증 옵션 생성 (username, userHandle 없이 호출하여 Discoverable Credential 모드 사용)
            StartAssertionOptions assertionOptions = StartAssertionOptions.builder()
                    .userVerification(uvr)
                    .timeout(60000L) // 1분
                    .build();

            AssertionRequest assertionRequest = relyingParty.startAssertion(assertionOptions);

            // 4. 세션에 인증 옵션 저장
            String sessionId = UUID.randomUUID().toString();
            WebAuthnSession session = WebAuthnSession.builder()
                    .id(sessionId)
                    // userId는 이 단계에서는 알 수 없으므로 null 또는 별도 표시
                    .userId(null)
                    .challenge(WebAuthnUtils.base64UrlEncode(assertionRequest.getPublicKeyCredentialRequestOptions().getChallenge()))
                    .operation("AUTHENTICATION_DISCOVERABLE") // 새로운 operation type
                    .requestJson(objectMapper.writeValueAsString(assertionRequest))
                    .clientIp(getClientIp())
                    .userAgent(getUserAgent())
                    .createdAt(System.currentTimeMillis())
                    .build();

            sessionRepository.save(session);
            log.info("간편 로그인 인증 세션 생성: sessionId={}", sessionId);

            // 5. 클라이언트 응답 생성
            // Discoverable Credential 사용 시 allowCredentials는 비워둠
            List<CredentialDescriptor> allowCredentials = Collections.emptyList();

            String userVerificationValue = assertionRequest.getPublicKeyCredentialRequestOptions().getUserVerification()
                    .map(UserVerificationRequirement::getValue)
                    .orElse(UserVerificationRequirement.PREFERRED.getValue());

            AuthenticationStartResponse response = new AuthenticationStartResponse(
                    WebAuthnUtils.base64UrlEncode(assertionRequest.getPublicKeyCredentialRequestOptions().getChallenge()),
                    assertionRequest.getPublicKeyCredentialRequestOptions().getTimeout().orElse(60000L),
                    allowCredentials,
                    userVerificationValue
            );

            return WebAuthnResponse.success(response, sessionId);

        } catch (Exception e) {
            log.error("WebAuthn 간편 로그인 인증 시작 오류", e);
            throw new BusinessException(ErrorCode.UNAUTHORIZED); // 적절한 에러 코드로 변경 가능
        }
    }

    /**
     * <p>WebAuthn 인증 완료</p>
     * <p>1. 세션 정보 가져오기</p>
     * <p>2. 클라이언트 응답 데이터 파싱</p>
     * <p>3. 인증 완료 요청 생성</p>
     * <p>4. 인증 완료 처리</p>
     * <p>5. 인증정보 업데이트</p>
     * <p>6. 세션 삭제</p>
     * <p>7. 결과 반환</p>
     */
    @Transactional
    public TokenResponse<AuthenticationFinishResponse> finishAuthentication(AuthenticationFinishRequest request) {
        try {
            log.info("WebAuthn 인증 완료 시작: sessionId={}", request.sessionId());

            // 1. 세션 정보 가져오기
            WebAuthnSession session = sessionRepository.findById(request.sessionId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.INVALID_SESSION, "인증 세션을 찾을 수 없습니다: " + request.sessionId()));

            // 세션 작업 유형 확인 (일반 인증 / 간편 로그인 인증 구분)
            boolean isDiscoverable = "AUTHENTICATION_DISCOVERABLE".equals(session.getOperation());
            if (!isDiscoverable && !"AUTHENTICATION".equals(session.getOperation())) {
                throw new BusinessException(ErrorCode.INVALID_SESSION_OPERATION, "잘못된 세션 작업: " + session.getOperation());
            }

            // 2. 저장된 요청(AssertionRequest) 복원
            AssertionRequest assertionRequest = objectMapper.readValue(session.getRequestJson(), AssertionRequest.class);

            // 3. 클라이언트 응답 데이터 파싱 (AuthenticationFinishRequest DTO 구조에 맞게)
            PublicKeyCredential<AuthenticatorAssertionResponse, ClientAssertionExtensionOutputs> pkc;
            try {
                AssertionCredentialInfo cred = request.credential();
                // AuthenticationFinishRequest의 response 필드가 AssertionResponse DTO의 JSON 문자열이라고 가정
                // 실제 클라이언트 구현에 따라 request.rawId(), request.response().clientDataJSON() 등을 사용해야 할 수 있음
                String clientResponseJson = objectMapper.writeValueAsString(cred); // request.credential()이 AssertionResponse 객체라고 가정
                pkc = PublicKeyCredential.parseAssertionResponseJson(clientResponseJson);
            } catch (JsonProcessingException e) {
                log.error("클라이언트 WebAuthn 응답 JSON 파싱 오류", e);
                throw new BusinessException(ErrorCode.BAD_REQUEST, "잘못된 클라이언트 응답입니다.");
            }


            // 4. RelyingParty 컨텍스트 생성 및 인증 완료 처리
            RelyingParty relyingParty = getRelyingParty();
            FinishAssertionOptions finishOptions = FinishAssertionOptions.builder()
                    .request(assertionRequest)
                    .response(pkc)
                    .build();

            AssertionResult result = relyingParty.finishAssertion(finishOptions);

            if (!result.isSuccess()) {
                log.warn("WebAuthn 인증 검증 실패: credentialId={}, userHandle={}",
                        WebAuthnUtils.base64UrlEncode(pkc.getId()),
                        pkc.getResponse().getUserHandle().map(WebAuthnUtils::base64UrlEncode).orElse("N/A"));
                throw new BusinessException(ErrorCode.UNAUTHORIZED, "WebAuthn 인증에 실패했습니다.");
            }

            // 5. 사용자 정보 조회 및 로그인 처리
            User user;
            ByteArray userHandleFromAssertion = result.getUserHandle(); // Yubico 라이브러리가 반환하는 userHandle

            // ✅ userHandle이 없으면 credentialId 기반으로 fallback
            if (userHandleFromAssertion.isEmpty()) {
                // Non-resident key or platform authenticator (userHandle 없음)
                if (!isDiscoverable || session.getUserId() == null) {
                    log.error("Discoverable credential 인증 성공했으나 userHandle이 없거나 세션에 userId가 없습니다.");
                    throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "인증 처리 중 오류가 발생했습니다.");
                }
                user = userRepository.findById(session.getUserId())
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, "세션의 사용자를 찾을 수 없습니다."));
            } else {
                // ✅ userHandle 있으면 원래 방식대로 CredentialRepository 통해 username 조회
                String username = ((CredentialRepositoryAdapter) relyingParty.getCredentialRepository())
                    .getUsernameForUserHandle(userHandleFromAssertion)
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, "UserHandle에 해당하는 사용자를 찾을 수 없습니다."));
                user = userRepository.findByPhoneNumber(username)
                    .orElseThrow(() -> new BusinessException(ErrorCode.USER_NOT_FOUND, "사용자를 찾을 수 없습니다: " + username));
            }


            log.info("WebAuthn 인증 성공: userId={}, username={}", user.getId(), user.getPhoneNumber());

            // 6. 인증정보 업데이트 (서명 카운트, 마지막 사용 시간 등)
            String credentialId = WebAuthnUtils.base64UrlEncode(result.getCredentialId());
            WebAuthnCredential credential = credentialRepository.findByCredentialId(credentialId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.CREDENTIAL_NOT_FOUND, "인증정보를 찾을 수 없습니다: " + credentialId));

            // 사용자 일치 확인 (추가 보안 계층)
            if (!credential.getUser().getId().equals(user.getId())) {
                log.error("인증된 사용자와 Credential의 사용자가 일치하지 않습니다. credentialUserId={}, authenticatedUserId={}",
                        credential.getUser().getId(), user.getId());
                throw new BusinessException(ErrorCode.FORBIDDEN, "인증 정보가 현재 사용자의 것이 아닙니다.");
            }

            credential.updateSignatureCount(result.getSignatureCount());
            credential.updateLastUsed();
            credentialRepository.save(credential);
            log.info("인증정보 업데이트: credentialId={}, signatureCount={}", credentialId, result.getSignatureCount());

            // 7. 세션 삭제
            sessionRepository.delete(session);
            log.info("인증 세션 삭제: sessionId={}", session.getId());

            // 8. 토큰 발급
            String accessToken = jwtTokenProvider.generateAccessToken(user.getId());
            RefreshToken refreshToken = RefreshToken.builder()
                    .token(jwtTokenProvider.generateRefreshToken(user.getId()))
                    .userId(user.getId())
                    .ttl(jwtTokenProvider.getRefreshTokenExpiresIn())
                    .build();

            // 9. 리프레시 토큰 Redis 저장
            refreshTokenRepository.save(refreshToken);

            // 10. 로그인 성공 응답 생성
            return new TokenResponse<>(
                    refreshToken,
                    new AuthenticationFinishResponse(
                            user.getId(),
                            user.getPhoneNumber(),
                            credentialId,
                            accessToken
                    )
            );

        } catch (AssertionFailedException e) {
            log.warn("WebAuthn Assertion 실패", e);
            throw new BusinessException(ErrorCode.UNAUTHORIZED, "WebAuthn 인증 검증에 실패했습니다: " + e.getMessage());
        } catch (JsonProcessingException e) {
            log.error("WebAuthn 인증 완료 JSON 처리 오류", e);
            throw new BusinessException(ErrorCode.BAD_REQUEST, "요청/응답 처리 중 오류가 발생했습니다.");
        } catch (BusinessException e) {
            log.warn("WebAuthn 인증 완료 처리 중 비즈니스 예외 발생: {}", e.getMessage());
            throw e;
        } catch (Exception e) {
            log.error("WebAuthn 인증 완료 중 예상치 못한 오류 발생", e);
            throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "인증 처리 중 서버 내부 오류가 발생했습니다.");
        }
    }

    /**
     * 사용자의 등록된 WebAuthn 인증정보 목록 조회
     */
    public WebAuthnResponse<List<WebAuthnCredentialDTO>> getCredentials(Long userId) {
        try {
            List<WebAuthnCredential> credentials = credentialRepository.findAllByUserIdAndStatusNot(
                    userId, CredentialStatus.REVOKED);

            List<WebAuthnCredentialDTO> credentialDTOs = credentials.stream()
                    .map(WebAuthnCredentialDTO::fromEntity)
                    .toList();

            return WebAuthnResponse.success(credentialDTOs);
        } catch (Exception e) {
            log.error("WebAuthn 인증정보 조회 오류", e);
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        }
    }

    /**
     * WebAuthn 인증정보 삭제
     */
    @Transactional
    public void revokeCredential(Long userId, String credentialId) {
        try {
            WebAuthnCredential credential = credentialRepository.findByCredentialId(credentialId)
                    .orElseThrow(() -> new IllegalArgumentException("인증정보를 찾을 수 없습니다: " + credentialId));

            // 사용자 확인
            if (!credential.getUser().getId().equals(userId)) {
                throw new IllegalArgumentException("권한이 없습니다");
            }

            credentialRepository.delete(credential);
        } catch (Exception e) {
            log.error("WebAuthn 인증정보 삭제 오류", e);
            throw new BusinessException(ErrorCode.BAD_REQUEST);
        }
    }

    /**
     * RelyingParty 객체 생성
     */
    private RelyingParty getRelyingParty() {
        RelyingPartyIdentity rpIdentity = RelyingPartyIdentity.builder()
                .id(rpId)
                .name(rpName)
                .build();

        return RelyingParty.builder()
                .identity(rpIdentity)
                .credentialRepository(new CredentialRepositoryAdapter(credentialRepository, userRepository))
                .origins(Set.of(rpOrigin))
                .build();
    }

    /**
     * 전송 방식 문자열을 AuthenticatorTransport로 변환
     */
    private Optional<AuthenticatorTransport> parseTransport(String transport) {
        try {
            return Optional.of(AuthenticatorTransport.of(transport));
        } catch (IllegalArgumentException e) {
            log.warn("인식할 수 없는 전송 방식: {}", transport);
            return Optional.empty();
        }
    }

    /**
     * 클라이언트 IP 가져오기
     */
    private String getClientIp() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        String ip = request.getHeader("X-Forwarded-For");

        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_CLIENT_IP");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("HTTP_X_FORWARDED_FOR");
        }
        if (ip == null || ip.isEmpty() || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return ip;
    }

    /**
     * 사용자 에이전트 가져오기
     */
    private String getUserAgent() {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
        return request.getHeader("User-Agent");
    }

    /**
     * Yubico WebAuthn 라이브러리를 위한 CredentialRepository 어댑터
     */
    private static class CredentialRepositoryAdapter implements CredentialRepository {

        private final WebAuthnCredentialRepository credentialRepository;
        private final UserRepository userRepository;

        public CredentialRepositoryAdapter(WebAuthnCredentialRepository credentialRepository, UserRepository userRepository) {
            this.credentialRepository = credentialRepository;
            this.userRepository = userRepository;
        }

        @Override
        public Set<PublicKeyCredentialDescriptor> getCredentialIdsForUsername(String phoneNumber) {
            return userRepository.findByPhoneNumber(phoneNumber)
                    .map(user -> {
                        List<WebAuthnCredential> credentials = credentialRepository.findAllByUserIdAndStatus(
                                user.getId(), CredentialStatus.ACTIVE);

                        return credentials.stream()
                                .map(cred -> PublicKeyCredentialDescriptor.builder()
                                        .id(new ByteArray(WebAuthnUtils.base64UrlDecode(cred.getCredentialId())))
                                        .type(PublicKeyCredentialType.PUBLIC_KEY)
                                        .build())
                                .collect(Collectors.toSet());
                    })
                    .orElse(Collections.emptySet());
        }

        @Override
        public Optional<ByteArray> getUserHandleForUsername(String phoneNumber) {
            return userRepository.findByPhoneNumber(phoneNumber)
                    .map(user -> WebAuthnUtils.createUserHandle(user.getId().toString()));
        }

        @Override
        public Optional<String> getUsernameForUserHandle(ByteArray userHandle) {
            try {
                // SHA-256 해시를 사용하는 경우 직접 ID로 변환할 수 없으므로
                // 모든 사용자에 대해 핸들을 생성하여 비교
                return userRepository.findAll().stream()
                        .filter(user -> {
                            ByteArray computedHandle = WebAuthnUtils.createUserHandle(user.getId().toString());
                            return computedHandle.equals(userHandle);
                        })
                        .findFirst()
                        .map(User::getPhoneNumber);
            } catch (Exception e) {
                log.error("사용자 핸들 매핑 오류", e);
                return Optional.empty();
            }
        }

        @Override
        public Optional<RegisteredCredential> lookup(ByteArray credentialId, ByteArray userHandle) {
            String credentialIdString = WebAuthnUtils.base64UrlEncode(credentialId);

            return credentialRepository.findByCredentialId(credentialIdString)
                    .filter(cred -> cred.getStatus() == CredentialStatus.ACTIVE)
                    .map(cred -> RegisteredCredential.builder()
                            .credentialId(credentialId)
                            .userHandle(WebAuthnUtils.createUserHandle(cred.getUser().getId().toString()))
                            .publicKeyCose(new ByteArray(cred.getPublicKeyCose()))
                            .signatureCount(cred.getSignatureCount())
                            .build());
        }

        @Override
        public Set<RegisteredCredential> lookupAll(ByteArray credentialId) {
            String credentialIdString = WebAuthnUtils.base64UrlEncode(credentialId);

            return credentialRepository.findByCredentialId(credentialIdString)
                    .filter(cred -> cred.getStatus() == CredentialStatus.ACTIVE)
                    .map(cred -> RegisteredCredential.builder()
                            .credentialId(credentialId)
                            .userHandle(WebAuthnUtils.createUserHandle(cred.getUser().getId().toString()))
                            .publicKeyCose(new ByteArray(cred.getPublicKeyCose()))
                            .signatureCount(cred.getSignatureCount())
                            .build())
                    .map(Set::of)
                    .orElse(Collections.emptySet());
        }
    }
} 