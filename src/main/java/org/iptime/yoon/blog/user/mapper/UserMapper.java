package org.iptime.yoon.blog.user.mapper;

import org.iptime.yoon.blog.security.auth.AuthUser;
import org.iptime.yoon.blog.security.auth.JwtUser;
import org.iptime.yoon.blog.security.jwt.JwtVerifyResult;
import org.iptime.yoon.blog.user.dto.BlogUserInfoResponse;
import org.iptime.yoon.blog.user.entity.BlogRole;
import org.iptime.yoon.blog.user.entity.BlogUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

/**
 * @author rival
 * @since 2023-12-31
 */
public class UserMapper {

    public static List<GrantedAuthority> getAuthorities(BlogRole role){
        SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority("ROLE_" + role.name());
        return List.of(grantedAuthority);
    }

    // Refresh Token 에서 사용
    public static JwtUser fromBlogUserToJwtUser(BlogUser blogUser){
        return JwtUser.builder()
            .username(blogUser.getUsername())
            .displayName(blogUser.getDisplayName())
            .id(blogUser.getId())
            .authorities(getAuthorities(blogUser.getRole()))
            .build();
    }

    // 로그인  UserDetailsService 에서 사용
    public static AuthUser fromBlogUserToAuthUser(BlogUser blogUser){
        return new AuthUser(
            blogUser.getUsername(),
            blogUser.getPassword(),
            getAuthorities(blogUser.getRole()),
            blogUser.getId(),
            blogUser.getProfile(),
            blogUser.getDisplayName());
    }


    //  로그인 성공 후 jwt 생성에 필요
    public static JwtUser fromAuthUserToJwtUser(AuthUser authUser){
        return JwtUser.builder()
            .id(authUser.getId())
            .username(authUser.getUsername())
            .displayName(authUser.getDisplayName())
            .authorities(authUser.getAuthorities())
            .build();
    }

    public static BlogUserInfoResponse fromBlogUserToBlogUserInfoResponse(BlogUser blogUser){
        return BlogUserInfoResponse.builder()
            .email(blogUser.getEmail()).username(blogUser.getUsername()).displayName(blogUser.getDisplayName())
            .build();
    }

    public static JwtUser fromJwtResultToJwtUser(JwtVerifyResult jwtVerifyResult) {

        List<SimpleGrantedAuthority> authorities = jwtVerifyResult.getAuthorities().stream().map(SimpleGrantedAuthority::new).toList();

        return JwtUser.builder()
            .id(jwtVerifyResult.getId())
            .username(jwtVerifyResult.getSubject())
            .displayName(jwtVerifyResult.getDisplayName())
            .authorities(authorities)
            .build();
    }
}
