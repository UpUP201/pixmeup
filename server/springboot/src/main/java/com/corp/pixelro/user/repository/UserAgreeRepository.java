package com.corp.pixelro.user.repository;

import com.corp.pixelro.user.entity.User;
import com.corp.pixelro.user.entity.UserAgree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserAgreeRepository extends JpaRepository<UserAgree, Long> {

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE UserAgree ua SET ua.deleted = true, ua.updatedAt = CURRENT_TIMESTAMP WHERE ua.user = :user")
    int softDeleteAllByUser(@Param("user") User user);

}
