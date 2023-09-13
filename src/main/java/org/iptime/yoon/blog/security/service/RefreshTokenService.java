package org.iptime.yoon.blog.security.service;


import org.iptime.yoon.blog.security.dto.User;

/**
 * @author rival
 * @since 2023-08-17
 */
public interface RefreshTokenService {
    String createToken(Long id);
    User validateTokenAndGetUser(String token);

    void removeExpiredTokens();

}
