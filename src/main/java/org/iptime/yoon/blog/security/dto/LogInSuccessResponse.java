package org.iptime.yoon.blog.security.dto;

import lombok.*;

/**
 * @author rival
 * @since 2023-08-14
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LogInSuccessResponse {
    @Builder.Default
    private String message = "Successfully logged in.";
    private String token;
    private String username;
    private String displayName;
    private Long profileImageId;
}
