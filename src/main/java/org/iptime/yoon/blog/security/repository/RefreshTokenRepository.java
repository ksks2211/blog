package org.iptime.yoon.blog.security.repository;

import org.iptime.yoon.blog.security.entity.BlogUser;
import org.iptime.yoon.blog.security.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * @author rival
 * @since 2023-08-17
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,String> {

    Optional<RefreshToken> findByUser(BlogUser user);
}
