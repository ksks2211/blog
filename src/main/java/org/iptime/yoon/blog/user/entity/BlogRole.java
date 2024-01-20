package org.iptime.yoon.blog.user.entity;

import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

/**
 * @author rival
 * @since 2023-08-15
 */
@Getter
public enum BlogRole {
    ADMIN("ADMIN"), MANAGER("MANAGER"), USER("USER");

    public final String role;
    BlogRole(String role){
        this.role=role;
    }

    public GrantedAuthority getGrantedAuthority(){
        return new SimpleGrantedAuthority("ROLE_" + role);
    }

}
