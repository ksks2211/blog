package org.iptime.yoon.blog.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

/**
 * @author rival
 * @since 2024-01-11
 */
@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 900)
public class SessionConfig {

}

