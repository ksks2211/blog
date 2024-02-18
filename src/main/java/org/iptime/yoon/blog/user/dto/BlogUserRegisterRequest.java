package org.iptime.yoon.blog.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author rival
 * @since 2023-08-14
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlogUserRegisterRequest {


    @Schema(description = "Username", example = BlogUserConstants.EXAMPLE_USERNAME)
    @NotBlank(message = "Username is mandatory")
    @Size(min = 5, max = 20, message = "Username must be between 5 and 20 characters")
    @Pattern(regexp = BlogUserConstants.USERNAME_PATTERN_REGEX, message = BlogUserConstants.USERNAME_PATTERN_MESSAGE)
    private String username;


    @Schema(description = "Password", example = BlogUserConstants.EXAMPLE_PASSWORD)
    @NotBlank(message = "Password is required")
    @Size(min = 5, max = 20, message = "Password must be between 5 and 20 characters")
    @Pattern(regexp = BlogUserConstants.PASSWORD_REGEX, message = BlogUserConstants.PASSWORD_MESSAGE)
    private String password;


    @Schema(description = "Email", example = "example123@example.com")
    @Email
    private String email;
}
