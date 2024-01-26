package org.iptime.yoon.blog.common.config;

import lombok.RequiredArgsConstructor;
import org.iptime.yoon.blog.security.CurrentUsernameArgumentResolver;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.*;
import org.springframework.web.servlet.resource.PathResourceResolver;

import java.io.IOException;
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
    public void addViewControllers(ViewControllerRegistry registry) {
        // Map the root URL to index.html
        registry.addViewController("/").setViewName("forward:/index.html");
        registry.addViewController("").setViewName("forward:/index.html");
    }



    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {

        registry.addResourceHandler("/**")
            .addResourceLocations("classpath:/static/")
            .resourceChain(true)
            .addResolver(new PathResourceResolver(){
                @Override
                protected Resource getResource(@NotNull String resourcePath, @NotNull Resource location) throws IOException {
                    Resource requestedResource = location.createRelative(resourcePath);
                    return requestedResource.exists() && requestedResource.isReadable() ? requestedResource
                        : new ClassPathResource("/static/index.html");}
            });
    }


    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") // or specify a more specific path
            .allowedOrigins("http://localhost:3306") // specify the allowed origin
            .allowCredentials(true) // allow credentials including cookies
            .allowedMethods("GET", "POST", "PUT", "DELETE") // allowed HTTP methods
            .allowedHeaders("*"); // allowed headers
    }
}
