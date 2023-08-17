package org.iptime.yoon.blog.security.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author rival
 * @since 2023-08-14
 */
public class InvalidBlogUserDataException extends AuthenticationException {
    public InvalidBlogUserDataException(String msg) {
        super(msg);
    }
}
