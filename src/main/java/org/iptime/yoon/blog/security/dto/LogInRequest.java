package org.iptime.yoon.blog.security.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.iptime.yoon.blog.user.dto.BlogUserConstants;


/**
 * @author rival
 * @since 2023-08-14
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogInRequest {
    @Schema(description = "Username", example = BlogUserConstants.EXAMPLE_USERNAME)
    @NotBlank(message = "Username is mandatory")
    private String username;

    @Schema(description = "Password", example = BlogUserConstants.EXAMPLE_PASSWORD)
    @NotBlank(message = "Password is required")
    private String password;
}
