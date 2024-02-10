package org.iptime.yoon.blog.security.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author rival
 * @since 2023-08-14
 */

@Data
@NoArgsConstructor
public class LogInRequest {

    @NotBlank(message = "Username is mandatory")
    private String username;
    @NotBlank(message = "Password is required")
    private String password;
}
