package org.iptime.yoon.blog.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author rival
 * @since 2023-08-14
 */
public class UsernameAlreadyTakenException extends AuthenticationException {
    public UsernameAlreadyTakenException(String msg) {
        super(msg);
    }
}
