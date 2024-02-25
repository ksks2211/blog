package org.iptime.yoon.blog.cache;


import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.RedisSerializer;

import java.time.Duration;

/**
 * @author rival
 * @since 2023-08-17
 */

@Configuration
@EnableCaching
@RequiredArgsConstructor
public class CacheConfig {

    @Value("${spring.data.redis.entry-ttl-minutes}")
    private int ENTRY_TTL_MINUTES;

    @Value("${spring.data.redis.cache-prefix}")
    private String CACHE_PREFIX;

    private final RedisSerializer<Object> redisSerializer;

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(ENTRY_TTL_MINUTES))
            .disableCachingNullValues()
            .prefixCacheNameWith(CACHE_PREFIX)
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(redisSerializer)
            );
    }
}
