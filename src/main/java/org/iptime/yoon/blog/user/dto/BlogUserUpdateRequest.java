package org.iptime.yoon.blog.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

/**
 * @author rival
 * @since 2023-11-01
 */
@Data
public class BlogUserUpdateRequest
{

    @NotBlank
    private String profile;

    @Email
    private String email;
}
