package org.iptime.yoon.blog.security.service;

import lombok.RequiredArgsConstructor;
import org.iptime.yoon.blog.security.dto.User;
import org.iptime.yoon.blog.security.entity.BlogRole;
import org.iptime.yoon.blog.security.entity.BlogUser;
import org.iptime.yoon.blog.security.repository.BlogUserRepository;
import org.iptime.yoon.blog.security.dto.req.BlogUserRegisterReqDto;
import org.iptime.yoon.blog.security.exception.UsernameAlreadyTakenException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author rival
 * @since 2023-08-14
 */

@RequiredArgsConstructor
public class BlogUserServiceImpl implements UserDetailsService, BlogUserService {

    private final BlogUserRepository blogUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        BlogUser blogUser = blogUserRepository.findByUsername(username).orElseThrow(() -> new UsernameNotFoundException("Username : " + username + " is not found"));
        return fromEntity(blogUser);
    }

    @Transactional
    public  void createBlogUser(BlogUserRegisterReqDto dto){
        String username = dto.getUsername();
        if(blogUserRepository.existsByUsername(username)){
            throw new UsernameAlreadyTakenException("Username : "+username+" is already taken");
        }
        BlogUser blogUser = toEntity(username,dto.getEmail());
        blogUser.setPassword(passwordEncoder.encode(dto.getPassword()));
        blogUserRepository.save(blogUser);
    }
    public boolean isUsernameTaken(String username){
        return blogUserRepository.existsByUsername(username);
    }

    public static List<GrantedAuthority> getAuthorities(BlogRole role){
        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_" + role.name());
        return List.of(grantedAuthority);
    }

    public static User fromEntity(BlogUser blogUser){
        return new User(blogUser.getUsername(),blogUser.getPassword(), getAuthorities(blogUser.getRole()), blogUser.getId());
    }

    public static BlogUser toEntity(String username, String email){
        return BlogUser.builder()
            .username(username)
//            .password(passwordEncoder.encode(password))
            .email(email)
            .build();
    }

}
