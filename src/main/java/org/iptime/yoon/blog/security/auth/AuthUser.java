package org.iptime.yoon.blog.security.auth;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

/**
 * @author rival
 * @since 2023-08-31
 */
@Getter
@Setter
public class AuthUser extends User  {



    private final Long id;
    private final String profile;
    private final String displayName;


    public AuthUser(String username, String password, Collection<? extends GrantedAuthority> authorities, Long id, String profile, String displayName) {
        super(username, password, authorities);
        this.id=id;
        this.profile = profile;
        this.displayName = displayName;
    }

}
