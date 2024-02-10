package org.iptime.yoon.blog.common.config;

import lombok.RequiredArgsConstructor;
import org.iptime.yoon.blog.security.CurrentUsernameArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

/**
 * @author rival
 * @since 2023-09-04
 */
@Configuration
@EnableWebMvc
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final CurrentUsernameArgumentResolver currentUsernameArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(currentUsernameArgumentResolver);
    }


    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Do not add any super call that adds resource handler
    }
}
