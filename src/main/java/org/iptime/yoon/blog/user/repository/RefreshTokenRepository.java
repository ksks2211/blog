package org.iptime.yoon.blog.user.repository;

import org.iptime.yoon.blog.user.entity.BlogUser;
import org.iptime.yoon.blog.user.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * @author rival
 * @since 2023-08-17
 */
public interface RefreshTokenRepository extends JpaRepository<RefreshToken,String> {

    Optional<RefreshToken> findByUser(BlogUser user);

    @Modifying
    @Query("delete from RefreshToken rt  where rt.expiryDate < CURRENT_TIMESTAMP ")
    void deleteAllExpiredTokens();
}
