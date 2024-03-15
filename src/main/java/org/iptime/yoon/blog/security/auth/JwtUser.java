package org.iptime.yoon.blog.security.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author rival
 * @since 2023-12-31
 */

@Setter
@Getter
@AllArgsConstructor
@Builder
public class JwtUser implements UserDetails {

    private final Long id;
    private final String username;
    private final String profile;
    private final String displayName;


    @Builder.Default
    private final Collection<? extends GrantedAuthority> authorities = new ArrayList<>();

    @Override
    public String getPassword() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
