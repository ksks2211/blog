package org.iptime.yoon.blog.security.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


/**
 * @author rival
 * @since 2024-02-02
 */
@Getter
@Component
@ConfigurationProperties(prefix = "cors")
public class CorsProperties {
    private final List<String> allowedOrigins = new ArrayList<>();
}
