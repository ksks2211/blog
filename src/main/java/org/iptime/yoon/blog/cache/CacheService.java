package org.iptime.yoon.blog.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;

/**
 * @author rival
 * @since 2024-01-03
 */
@Service
@RequiredArgsConstructor
public class CacheService {

    private final StringRedisTemplate redisTemplate;

    @Value("${spring.data.redis.cache-prefix}")
    private String CACHE_PREFIX;

    public String generateCacheKey(String key,String... cacheNames) {
        // Implement your custom logic to generate the cache key
        // For example, concatenate cacheNames with the key
        return CACHE_PREFIX+String.join("::", cacheNames) + "::" + key;
    }

    public void deleteMultipleKeys(Collection<String> keys){
        redisTemplate.delete(keys);
    }

    public void evictByPattern(String pattern){
        Set<String> keys = redisTemplate.keys(pattern);
        if (keys != null && !keys.isEmpty()) {
            redisTemplate.delete(keys);
        }
    }

}
