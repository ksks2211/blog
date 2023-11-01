package org.iptime.yoon.blog.security.dto.res;

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
public class BlogUserInfoResDto {
    private String username;
    private String email;
//    private String profile;
}
