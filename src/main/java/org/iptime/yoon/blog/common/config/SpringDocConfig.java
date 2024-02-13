package org.iptime.yoon.blog.common.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author rival
 * @since 2024-02-13
 */

@Configuration
public class SpringDocConfig {


    @Bean
    public GroupedOpenApi healthCheckApi(){
        return GroupedOpenApi.builder()
            .group("health-check-api").pathsToMatch("/api/health/**")
            .build();
    }

    @Bean
    public GroupedOpenApi userApi(){
        return GroupedOpenApi.builder()
            .group("user-api").pathsToMatch("/api/auth/**")
            .build();
    }
}
