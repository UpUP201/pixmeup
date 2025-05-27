package com.corp.pixelro.webauthn.repository;

import com.corp.pixelro.webauthn.entity.WebAuthnCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WebAuthnCredentialRepository extends JpaRepository<WebAuthnCredential, Long> {
    
    Optional<WebAuthnCredential> findByCredentialId(String credentialId);
    
    List<WebAuthnCredential> findAllByUserIdAndStatusNot(Long userId, WebAuthnCredential.CredentialStatus status);
    
    List<WebAuthnCredential> findAllByUserIdAndStatus(Long userId, WebAuthnCredential.CredentialStatus status);
    
    boolean existsByCredentialId(String credentialId);
    
    void deleteAllByUserId(Long userId);
    
    @Query("SELECT c FROM WebAuthnCredential c WHERE c.user.id = :userId AND c.status = 'ACTIVE'")
    List<WebAuthnCredential> findActiveCredentialsByUserId(@Param("userId") Long userId);
    
    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM WebAuthnCredential c WHERE c.user.id = :userId AND c.status = 'ACTIVE'")
    boolean hasActiveCredentials(@Param("userId") Long userId);
} 