package org.iptime.yoon.blog.user.service;

import lombok.RequiredArgsConstructor;
import org.iptime.yoon.blog.security.auth.JwtUser;
import org.iptime.yoon.blog.security.exception.UsernameAlreadyTakenException;
import org.iptime.yoon.blog.user.BlogUserMapper;
import org.iptime.yoon.blog.user.dto.BlogUserInfoResponse;
import org.iptime.yoon.blog.user.dto.BlogUserRegisterRequest;
import org.iptime.yoon.blog.user.dto.BlogUserUpdateRequest;
import org.iptime.yoon.blog.user.entity.AuthProvider;
import org.iptime.yoon.blog.user.entity.BlogUser;
import org.iptime.yoon.blog.user.repository.BlogUserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;


/**
 * @author rival
 * @since 2023-08-14
 */

@RequiredArgsConstructor
@Service
public class BlogUserServiceImpl implements BlogUserService {

    private final BlogUserRepository blogUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final BlogUserMapper blogUserMapper;

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
        blogUser.setEmail(dto.getEmail());
        blogUserRepository.save(blogUser);
        return blogUserMapper.blogUserToBlogUserInfo(blogUser);
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
        return blogUserMapper.blogUserToBlogUserInfo(blogUser);
    }

    @Override
    @Transactional
    public void deleteBlogUser(String username) {
        BlogUser blogUser = getBlogUserByUsername(username);
        blogUser.softDelete();
        blogUserRepository.save(blogUser);
    }

    @Override
    public JwtUser createOAuth2BlogUserIfNotExists(AuthProvider provider, String sub, String displayName, String email) {

        BlogUser blogUser = blogUserRepository.findByProviderAndSubject(provider, sub).orElseGet(() -> toEntity(provider, sub, displayName, email));
        blogUserRepository.save(blogUser);

        return blogUserMapper.blogUserToJwtUser(blogUser);
    }


    public boolean isUsernameTaken(String username){
        return blogUserRepository.existsByUsername(username);
    }

    @Override
    public BlogUserInfoResponse getBlogUserInfo(String username) {
        BlogUser blogUser = getBlogUserByUsername(username);
        return blogUserMapper.blogUserToBlogUserInfo(blogUser);
    }


    public static BlogUser toEntity(String username){
        return BlogUser.builder()
            .username(username)
            .displayName(username)
            .build();
    }

    public static BlogUser toEntity(AuthProvider provider, String subject, String displayName, String email){
        UUID uuid = UUID.randomUUID();
        String username = uuid.toString().replace("-","");
        return BlogUser.builder()
            .provider(provider)
            .username(username)   // UUID
            .displayName(displayName)
            .subject(subject)
            .email(email).build();
    }

}
