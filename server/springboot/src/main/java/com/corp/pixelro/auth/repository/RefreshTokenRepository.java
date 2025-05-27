package com.corp.pixelro.auth.repository;

import com.corp.pixelro.auth.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenRepository extends CrudRepository<RefreshToken, String> {

    void deleteByUserId(Long userId);

}
