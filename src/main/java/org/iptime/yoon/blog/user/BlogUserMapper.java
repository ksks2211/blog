package org.iptime.yoon.blog.user;

import org.iptime.yoon.blog.security.auth.AuthUser;
import org.iptime.yoon.blog.security.auth.JwtUser;
import org.iptime.yoon.blog.security.jwt.JwtVerifyResult;
import org.iptime.yoon.blog.user.dto.BlogUserInfoResponse;
import org.iptime.yoon.blog.user.entity.BlogRole;
import org.iptime.yoon.blog.user.entity.BlogUser;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;

/**
 * @author rival
 * @since 2024-01-18
 */
@Mapper(componentModel = "spring")
public interface BlogUserMapper {

    @Mapping(target="authorities", source="role", qualifiedByName="roleToAuthorityListConverter")
    JwtUser blogUserToJwtUser(BlogUser blogUser);

    @Mapping(target="authorities", source="authorities", qualifiedByName="authorityListConverter")
    @Mapping(target="username", source="subject")
    JwtUser jwtVerifyResultToJwtUser(JwtVerifyResult jwtVerifyResult);

    @Named("roleToAuthorityListConverter")
    default List<GrantedAuthority> roleToAuthorityListConverter(BlogRole role){
        return List.of(role.getGrantedAuthority());
    }


    BlogUserInfoResponse blogUserToBlogUserInfo(BlogUser blogUser);


    @Mapping(target="authorities", source="role", qualifiedByName="roleToAuthorityListConverter")
    AuthUser blogUserToAuthUser(BlogUser blogUser);



    JwtUser authUserToJwtUser(AuthUser authUser);

    @Named("authorityListConverter")
    default List<? extends GrantedAuthority> authorityListConverter(List<String> authorities){
        return authorities.stream().map(SimpleGrantedAuthority::new).toList();
    }





}
