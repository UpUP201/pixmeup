package com.corp.pixelro.webauthn.service;

import com.corp.pixelro.global.response.TokenResponse;
import com.corp.pixelro.webauthn.dto.*;

import java.util.List;

public interface WebAuthnService {

    WebAuthnResponse<RegistrationStartResponse> startRegistration(Long userId);

    WebAuthnResponse<RegistrationFinishResponse> finishRegistration(RegistrationFinishRequest request);

    WebAuthnResponse<AuthenticationStartResponse> startAuthentication(AuthenticationRequest request);

    TokenResponse<AuthenticationFinishResponse> finishAuthentication(AuthenticationFinishRequest request);

    WebAuthnResponse<List<WebAuthnCredentialDTO>> getCredentials(Long userId);

    void revokeCredential(Long userId, String credentialId);

}
