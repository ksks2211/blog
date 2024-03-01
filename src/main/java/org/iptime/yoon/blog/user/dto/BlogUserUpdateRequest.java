package org.iptime.yoon.blog.user.dto;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author rival
 * @since 2023-11-01
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BlogUserUpdateRequest
{

    private String profile;

    private Long profileImageId;

    @Email
    private String email;
}
