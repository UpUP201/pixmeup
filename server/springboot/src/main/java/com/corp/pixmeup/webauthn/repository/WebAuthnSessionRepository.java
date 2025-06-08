package com.corp.pixmeup.webauthn.repository;

import com.corp.pixmeup.webauthn.entity.WebAuthnSession;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WebAuthnSessionRepository extends CrudRepository<WebAuthnSession, String> {
} 