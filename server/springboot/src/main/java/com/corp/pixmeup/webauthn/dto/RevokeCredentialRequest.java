package com.corp.pixmeup.webauthn.dto;

public record RevokeCredentialRequest(
        String credentialId
) {
}
