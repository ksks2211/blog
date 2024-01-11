package org.iptime.yoon.blog.user.service;

import lombok.RequiredArgsConstructor;
import org.iptime.yoon.blog.security.exception.UsernameAlreadyTakenException;
import org.iptime.yoon.blog.user.dto.BlogUserInfoResponse;
import org.iptime.yoon.blog.user.dto.BlogUserRegisterRequest;
import org.iptime.yoon.blog.user.dto.BlogUserUpdateRequest;
import org.iptime.yoon.blog.user.entity.BlogUser;
import org.iptime.yoon.blog.user.mapper.UserMapper;
import org.iptime.yoon.blog.user.repository.BlogUserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;



/**
 * @author rival
 * @since 2023-08-14
 */

@RequiredArgsConstructor
public class BlogUserServiceImpl implements BlogUserService {

    private final BlogUserRepository blogUserRepository;
    private final PasswordEncoder passwordEncoder;


    @Override
    public BlogUser getBlogUserByUsername(String username) {
        return blogUserRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username : " + username + " is not found"));
    }


    @Transactional
    public BlogUserInfoResponse createBlogUser(BlogUserRegisterRequest dto){
        String username = dto.getUsername();
        if(blogUserRepository.existsByUsername(username)){
            throw new UsernameAlreadyTakenException("Username : "+username+" is already taken");
        }
        BlogUser blogUser = toEntity(username);
        blogUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        blogUserRepository.save(blogUser);

        return UserMapper.fromBlogUserToBlogUserInfoResponse(blogUser);
    }

    @Override
    @Transactional
    public BlogUserInfoResponse updateBlogUser(String username, BlogUserUpdateRequest dto) {
        BlogUser blogUser = getBlogUserByUsername(username);
        String profile = dto.getProfile();
        if(profile!=null){
            blogUser.setProfile(profile);
        }
        blogUserRepository.save(blogUser);
        return UserMapper.fromBlogUserToBlogUserInfoResponse(blogUser);
    }

    @Override
    @Transactional
    public void deleteBlogUser(String username) {
        BlogUser blogUser = getBlogUserByUsername(username);
        blogUser.softDelete();
        blogUserRepository.save(blogUser);
    }



    public boolean isUsernameTaken(String username){
        return blogUserRepository.existsByUsername(username);
    }

    @Override
    public BlogUserInfoResponse getBlogUserInfo(String username) {
        BlogUser blogUser = getBlogUserByUsername(username);
        return UserMapper.fromBlogUserToBlogUserInfoResponse(blogUser);
    }


    public static BlogUser toEntity(String username){
        return BlogUser.builder()
            .username(username)
            .displayName(username)
            .build();
    }

}
