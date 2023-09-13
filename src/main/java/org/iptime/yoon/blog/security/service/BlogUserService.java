package org.iptime.yoon.blog.security.service;

import org.iptime.yoon.blog.security.dto.req.BlogUserRegisterReqDto;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author rival
 * @since 2023-08-31
 */
public interface BlogUserService {


    UserDetails loadUserByUsername(String username);
    void createBlogUser(BlogUserRegisterReqDto dto);

    boolean isUsernameTaken(String username);

}
