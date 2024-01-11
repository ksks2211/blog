package org.iptime.yoon.blog.user.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author rival
 * @since 2023-11-01
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BlogUserInfoResponse {
    private String username;
    private String displayName;
    private String email;
//    private String profile;
}
