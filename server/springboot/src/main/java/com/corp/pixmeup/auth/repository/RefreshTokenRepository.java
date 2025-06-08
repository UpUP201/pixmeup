package com.corp.pixmeup.auth.repository;

import com.corp.pixmeup.auth.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    void deleteByUserId(Long userId);

}
