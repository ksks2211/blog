package org.iptime.yoon.blog.cache;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

import java.time.Duration;

/**
 * @author rival
 * @since 2023-08-17
 */

@Configuration
@EnableCaching
public class CacheConfig {

    @Value("${spring.data.redis.entry-ttl-minutes}")
    private int ENTRY_TTL_MINUTES;

    @Value("${spring.data.redis.cache-prefix}")
    private String CACHE_PREFIX;

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        GenericJackson2JsonRedisSerializer serializer = new GenericJackson2JsonRedisSerializer();
        return RedisCacheConfiguration.defaultCacheConfig()
            .entryTtl(Duration.ofMinutes(ENTRY_TTL_MINUTES))
            .disableCachingNullValues()
            .prefixCacheNameWith(CACHE_PREFIX)
            .serializeValuesWith(
                RedisSerializationContext.SerializationPair.fromSerializer(serializer)
            );
    }
}
