package org.iptime.yoon.blog.user.dto;

import lombok.Data;

/**
 * @author rival
 * @since 2023-11-01
 */
@Data
public class BlogUserUpdateRequest
{
    private String profile;
    private String email;
}
