package org.iptime.yoon.blog.security.service;

import lombok.RequiredArgsConstructor;
import org.iptime.yoon.blog.user.entity.BlogUser;
import org.iptime.yoon.blog.user.service.BlogUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import static org.iptime.yoon.blog.user.mapper.UserMapper.fromBlogUserToAuthUser;


/**
 * @author rival
 * @since 2023-12-31
 */

@RequiredArgsConstructor
public class AuthUserService implements UserDetailsService {

    private final BlogUserService blogUserService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BlogUser blogUser = blogUserService.getBlogUserByUsername(username);
        return fromBlogUserToAuthUser(blogUser);
    }


}
