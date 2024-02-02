package org.iptime.yoon.blog.security.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import java.util.List;
import java.util.ArrayList;


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
