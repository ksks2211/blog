package org.iptime.yoon.blog.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author rival
 * @since 2023-08-14
 */

@Data
public class LogInRequest {


    @NotBlank(message = "Username is mandatory")
    private String username;
    @NotBlank(message = "Password is required")
    private String password;
}
