package org.iptime.yoon.blog.security.dto.res;

import lombok.Builder;
import lombok.Data;

/**
 * @author rival
 * @since 2023-08-14
 */

@Data
@Builder
public class JwtLogInResDto {

    private int statusCode;
    private String message;
    private String token;
}
