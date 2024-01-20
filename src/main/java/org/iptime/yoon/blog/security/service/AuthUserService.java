package org.iptime.yoon.blog.security.service;

import lombok.RequiredArgsConstructor;
import org.iptime.yoon.blog.user.BlogUserMapper;
import org.iptime.yoon.blog.user.entity.BlogUser;
import org.iptime.yoon.blog.user.service.BlogUserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;



/**
 * @author rival
 * @since 2023-12-31
 */

@RequiredArgsConstructor
public class AuthUserService implements UserDetailsService {

    private final BlogUserService blogUserService;
    private final BlogUserMapper blogUserMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BlogUser blogUser = blogUserService.getBlogUserByUsername(username);
        return blogUserMapper.blogUserToAuthUser(blogUser);
    }


}
