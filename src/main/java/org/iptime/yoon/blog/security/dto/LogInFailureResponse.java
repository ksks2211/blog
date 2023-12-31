package org.iptime.yoon.blog.security.dto;

import lombok.Builder;
import lombok.Data;

/**
 * @author rival
 * @since 2023-12-31
 */

@Data
@Builder
public class LogInFailureResponse {
    @Builder.Default
    private String message = "Check your username and password.";

}
