package org.iptime.yoon.blog.security.config;

import lombok.RequiredArgsConstructor;
import org.iptime.yoon.blog.security.filter.JwtRefreshFilter;
import org.iptime.yoon.blog.security.repository.BlogUserRepository;
import org.iptime.yoon.blog.security.filter.JwtAuthenticationFilter;
import org.iptime.yoon.blog.security.jwt.JwtManager;
import org.iptime.yoon.blog.security.filter.JwtLoginFilter;
import org.iptime.yoon.blog.security.service.BlogUserServiceImpl;
import org.iptime.yoon.blog.security.service.RefreshTokenService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author rival
 * @since 2023-08-13
 */

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig{
    @Value("${spring.security.debug:false}") // :기본값
    boolean securityDebug;

    private final BlogUserRepository blogUserRepository;
    private final AuthenticationConfiguration authConfig;
    private final JwtManager jwtManager;

    private final RefreshTokenService refreshTokenService;

    @Bean
    PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
    @Bean
    BlogUserServiceImpl blogUserService(){
        return  new BlogUserServiceImpl(blogUserRepository, passwordEncoder());
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer(){
        return (web)-> web.debug(securityDebug);
    }
    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(blogUserService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public JwtLoginFilter jwtSignInFilter() throws Exception {
        return  new JwtLoginFilter("/auth/log-in",authenticationManager(), jwtManager,refreshTokenService);
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(){
        return new JwtAuthenticationFilter(jwtManager);
    }

    @Bean
    public JwtRefreshFilter jwtRefreshFilter(){
        return new JwtRefreshFilter("/refresh",refreshTokenService, jwtManager);
    }
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable);
        http.sessionManagement(man->man.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.httpBasic(Customizer.withDefaults());

        http.addFilterBefore(jwtRefreshFilter(), BasicAuthenticationFilter.class);
        http.addFilterAt(jwtAuthenticationFilter(), BasicAuthenticationFilter.class);
        http.addFilterAt(jwtSignInFilter(), UsernamePasswordAuthenticationFilter.class);
        http.authorizeHttpRequests((auth)->
            auth.requestMatchers(new AntPathRequestMatcher("/auth/**")).permitAll().anyRequest().authenticated());
        return  http.build();
    }

    @Bean
    RoleHierarchy roleHierarchy(){
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_MANAGER > ROLE_USER");
        return roleHierarchy;
    }

}
