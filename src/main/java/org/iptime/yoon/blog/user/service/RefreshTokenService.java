package org.iptime.yoon.blog.user.service;


import org.iptime.yoon.blog.security.auth.JwtUser;

/**
 * @author rival
 * @since 2023-08-17
 */
public interface RefreshTokenService {
    String createToken(Long id);
    JwtUser validateTokenAndGetJwtUser(String id);

    void removeExpiredTokens();

    void removeTokenByUserId(Long id);
}
