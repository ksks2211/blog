package org.iptime.yoon.blog.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author rival
 * @since 2023-08-17
 */
public class ExpiredRefreshTokenException extends AuthenticationException {
    public ExpiredRefreshTokenException(String msg) {
        super(msg);
    }
}
