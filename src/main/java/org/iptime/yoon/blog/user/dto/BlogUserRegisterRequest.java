package org.iptime.yoon.blog.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

/**
 * @author rival
 * @since 2023-08-14
 */

@Data
public class BlogUserRegisterRequest {

    @NotBlank(message = "Username is mandatory")
    @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters")
    @Pattern(regexp = "^[a-zA-Z0-9]*$", message = "Username must be alphanumeric")
    private String username;

    @NotBlank(message = "Password is required")
    @Size(min = 5, max = 20, message = "Password must be between 5 and 20 characters")
    @Pattern(regexp = "^\\S*$", message = "Password must not contain whitespace")
    private String password;

    @Email
    private String email;
}
