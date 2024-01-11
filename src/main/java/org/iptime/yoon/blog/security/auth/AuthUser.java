package org.iptime.yoon.blog.security.auth;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @author rival
 * @since 2023-08-31
 */
@Getter
public class AuthUser extends User {

    private final Long id;
    private String profile;
    private String displayName;


    public AuthUser(String username, String password, Collection<? extends GrantedAuthority> authorities, Long id, String profile, String displayName) {
        super(username, password, authorities);
        this.id=id;
        this.profile = profile;
        this.displayName = displayName;
    }





}
