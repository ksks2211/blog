package org.iptime.yoon.blog.user.service;

import org.iptime.yoon.blog.security.auth.JwtUser;
import org.iptime.yoon.blog.user.dto.BlogUserRegisterRequest;
import org.iptime.yoon.blog.user.dto.BlogUserUpdateRequest;
import org.iptime.yoon.blog.user.dto.BlogUserInfoResponse;
import org.iptime.yoon.blog.user.entity.BlogUser;

/**
 * @author rival
 * @since 2023-08-31
 */
public interface BlogUserService {


    BlogUser getBlogUserByUsername(String username);

    boolean isUsernameTaken(String username);

    // Read
    BlogUserInfoResponse getBlogUserInfo(String username);

    // Create
    BlogUserInfoResponse createBlogUser(BlogUserRegisterRequest dto);
    // Update
    BlogUserInfoResponse updateBlogUser(String username, BlogUserUpdateRequest dto);

    // Delete
    void deleteBlogUser(String username);







}
