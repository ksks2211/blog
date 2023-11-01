package org.iptime.yoon.blog.security.dto.req;

import lombok.Data;

/**
 * @author rival
 * @since 2023-11-01
 */
@Data
public class BlogUserUpdateReqDto
{
    private String profile;
    private String email;
}
