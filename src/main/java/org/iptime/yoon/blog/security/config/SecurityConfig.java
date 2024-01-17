package org.iptime.yoon.blog.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iptime.yoon.blog.security.filter.JwtAuthenticationFilter;
import org.iptime.yoon.blog.security.filter.JwtLoginFilter;
import org.iptime.yoon.blog.security.filter.JwtRefreshFilter;
import org.iptime.yoon.blog.security.handler.AuthFailureHandler;
import org.iptime.yoon.blog.security.handler.AuthSuccessHandler;
import org.iptime.yoon.blog.security.handler.CustomAccessDeniedHandler;
import org.iptime.yoon.blog.security.handler.CustomAuthenticationEntryPoint;
import org.iptime.yoon.blog.security.jwt.JwtManager;
import org.iptime.yoon.blog.security.service.AuthUserService;
import org.iptime.yoon.blog.user.repository.BlogUserRepository;
import org.iptime.yoon.blog.user.service.BlogUserService;
import org.iptime.yoon.blog.user.service.BlogUserServiceImpl;
import org.iptime.yoon.blog.user.service.RefreshTokenService;
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
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

/**
 * @author rival
 * @since 2023-08-13
 */

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
@Slf4j
public class SecurityConfig {
    @Value("${spring.security.debug:false}") // :기본값
    boolean securityDebug;

    private final BlogUserRepository blogUserRepository;
    private final AuthenticationConfiguration authConfig;
    private final JwtManager jwtManager;
    private final ObjectMapper objectMapper;
    private final RefreshTokenService refreshTokenService;


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public BlogUserService blogUserService() {
        return new BlogUserServiceImpl(blogUserRepository, passwordEncoder());
    }

    @Bean
    public AuthUserService authUserService() {
        return new AuthUserService(blogUserService());
    }

    @Bean
    public AuthSuccessHandler authSuccessHandler() {
        return new AuthSuccessHandler(jwtManager, objectMapper, blogUserService());
    }

    @Bean
    public AuthFailureHandler authFailureHandler() {
        return new AuthFailureHandler(objectMapper);
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) ->
            web.debug(securityDebug);
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authConfig.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(authUserService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public JwtLoginFilter jwtLogInFilter() throws Exception {
        JwtLoginFilter jwtLoginFilter = new JwtLoginFilter("/api/auth/login", authenticationManager(), objectMapper);
        jwtLoginFilter.setAuthenticationFailureHandler(authFailureHandler());
        jwtLoginFilter.setAuthenticationSuccessHandler(authSuccessHandler());
        return jwtLoginFilter;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtManager);
    }

    @Bean
    public JwtRefreshFilter jwtRefreshFilter() {
        return new JwtRefreshFilter("/api/token/renew", refreshTokenService, jwtManager, objectMapper);
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final var configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3306", "http://localhost:8080"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "OPTIONS", "DELETE", "PUT"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setExposedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);

        final var source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);

        return source;
    }

    @Bean
    AccessDeniedHandler accessDeniedHandler(){
        return new CustomAccessDeniedHandler(objectMapper);
    }

    @Bean
    AuthenticationEntryPoint authenticationEntryPoint(){
        return  new CustomAuthenticationEntryPoint(objectMapper);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((auth) ->
                auth
                    .requestMatchers(
                        new AntPathRequestMatcher("/api/categories/**"),
                        new AntPathRequestMatcher("/api/images/**"),
                        new AntPathRequestMatcher("/api/posts/**")).authenticated()
                    .anyRequest().permitAll())

            .cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(
                man -> man.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .httpBasic(Customizer.withDefaults())
            .addFilterBefore(jwtRefreshFilter(), BasicAuthenticationFilter.class)
            .addFilterAt(jwtAuthenticationFilter(), BasicAuthenticationFilter.class)
            .addFilterAt(jwtLogInFilter(), UsernamePasswordAuthenticationFilter.class)

            .exceptionHandling(
                config -> config
                        .accessDeniedHandler(accessDeniedHandler())
                        .authenticationEntryPoint(authenticationEntryPoint())
            )
            .oauth2Login(oauth2Login -> oauth2Login
                .successHandler(authSuccessHandler())
                .failureHandler(authFailureHandler())
            );


        return http.build();
    }

    @Bean
    RoleHierarchy roleHierarchy() {
        RoleHierarchyImpl roleHierarchy = new RoleHierarchyImpl();
        roleHierarchy.setHierarchy("ROLE_ADMIN > ROLE_MANAGER > ROLE_USER");
        return roleHierarchy;
    }

}
