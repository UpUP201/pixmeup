package com.corp.pixelro.webauthn.dto;

public record RevokeCredentialRequest(
        String credentialId
) {
}
