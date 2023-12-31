package org.iptime.yoon.blog.user.dto;

import lombok.Data;

/**
 * @author rival
 * @since 2023-08-14
 */

@Data
public class BlogUserRegisterRequest {
    private String username;
    private String password;
}
