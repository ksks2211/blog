package org.iptime.yoon.blog.security.service;

import org.springframework.security.core.userdetails.User;

/**
 * @author rival
 * @since 2023-08-17
 */
public interface RefreshTokenService {
    String createToken(String username);
    User validateTokenAndGetUser(String token);

    void removeToken(String token);

}
