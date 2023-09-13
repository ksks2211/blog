package org.iptime.yoon.blog.security.dto.req;

import lombok.Data;

/**
 * @author rival
 * @since 2023-08-14
 */

@Data
public class BlogUserRegisterReqDto {
    private String username;
    private String password;
    private String email;
}
