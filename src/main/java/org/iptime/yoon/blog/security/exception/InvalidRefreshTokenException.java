package org.iptime.yoon.blog.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author rival
 * @since 2023-08-17
 */
public class InvalidRefreshTokenException extends AuthenticationException {
    public InvalidRefreshTokenException(String msg) {
        super(msg);
    }
}
