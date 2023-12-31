package org.iptime.yoon.blog.user.mapper;

import org.iptime.yoon.blog.security.auth.AuthUser;
import org.iptime.yoon.blog.security.auth.JwtUser;
import org.iptime.yoon.blog.user.entity.BlogRole;
import org.iptime.yoon.blog.user.entity.BlogUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author rival
 * @since 2023-12-31
 */
@Component
public class UserMapper {

    public static List<GrantedAuthority> getAuthorities(BlogRole role){
        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_" + role.name());
        return List.of(grantedAuthority);
    }

    public static JwtUser fromBlogUserToJwtUser(BlogUser blogUser){
        return JwtUser.builder()
            .username(blogUser.getUsername())
            .id(blogUser.getId())
            .authorities(getAuthorities(blogUser.getRole()))
            .build();
    }

    public static AuthUser fromBlogUserToAuthUser(BlogUser blogUser){
        return new AuthUser(
            blogUser.getUsername(),
            blogUser.getPassword(),
            getAuthorities(blogUser.getRole()),
            blogUser.getId(),
            blogUser.getProfile());
    }

    public static JwtUser fromAuthUserToJwtUser(AuthUser authUser){
        return JwtUser.builder()
            .id(authUser.getId())
            .username(authUser.getUsername())
            .authorities(authUser.getAuthorities())
            .build();
    }
}
