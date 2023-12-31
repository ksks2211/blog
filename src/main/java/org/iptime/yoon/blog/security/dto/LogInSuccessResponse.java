package org.iptime.yoon.blog.security.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author rival
 * @since 2023-08-14
 */

@Data
@Builder
public class LogInSuccessResponse {
    @Builder.Default
    private String message = "Successfully logged in.";
    private String token;
    private String username;
}
