package org.iptime.yoon.blog.security.auth;

import lombok.Builder;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author rival
 * @since 2023-12-31
 */

@Data
@Builder
public class JwtUser {

    private final Long id;
    private final String username;
    private final String email;

    @Builder.Default
    private final Collection<? extends GrantedAuthority> authorities = new ArrayList<>();








}
