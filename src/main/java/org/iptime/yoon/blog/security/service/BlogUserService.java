package org.iptime.yoon.blog.security.service;

import org.iptime.yoon.blog.security.dto.req.BlogUserRegisterReqDto;
import org.iptime.yoon.blog.security.dto.req.BlogUserUpdateReqDto;
import org.iptime.yoon.blog.security.dto.res.BlogUserInfoResDto;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * @author rival
 * @since 2023-08-31
 */
public interface BlogUserService {


    UserDetails loadUserByUsername(String username);

    boolean isUsernameTaken(String username);

    // Read
    BlogUserInfoResDto getBlogUserInfo(String username);

    // Create
    BlogUserInfoResDto createBlogUser(BlogUserRegisterReqDto dto);
    // Update
    BlogUserInfoResDto updateBlogUser(String username, BlogUserUpdateReqDto dto);

    // Delete
    void deleteBlogUser(String username);







}
