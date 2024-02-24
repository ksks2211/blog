package org.iptime.yoon.blog.user;

import org.iptime.yoon.blog.user.dto.BlogUserInfoResponse;
import org.iptime.yoon.blog.user.entity.AuthProvider;
import org.iptime.yoon.blog.user.entity.BlogRole;
import org.iptime.yoon.blog.user.entity.BlogUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author rival
 * @since 2024-02-20
 */
class BlogUserMapperTest {

    private BlogUserMapper blogUserMapper;


    @BeforeEach
    public void init() {
        blogUserMapper = Mappers.getMapper(BlogUserMapper.class);
    }


    @Test
    public void testBlogUserToBlogUserInfo() {
        Long id = 1L;
        String username = "example123";
        String email = "example123@email.com";
        BlogUser blogUser = BlogUser.builder()
            .id(id)
            .username(username)
            .email(email)
            .provider(AuthProvider.LOCAL)
            .dateOfBirth(LocalDate.now())
            .password("1234")
            .profile("https://sdsdsd/image.png")
            .displayName(username)
            .role(BlogRole.USER)
            .build();

        BlogUserInfoResponse blogUserInfoResponse = blogUserMapper.blogUserToBlogUserInfo(blogUser);
        assertEquals(blogUser.getUsername(), blogUserInfoResponse.getUsername());
        assertEquals(blogUser.getEmail(), blogUserInfoResponse.getEmail());
        assertEquals(blogUser.getDisplayName(), blogUserInfoResponse.getDisplayName());
    }

}