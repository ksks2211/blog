package org.iptime.yoon.blog.security.dto;

import lombok.Data;

/**
 * @author rival
 * @since 2023-08-14
 */

@Data
public class LogInRequest {

    private String username;
    private String password;
}
