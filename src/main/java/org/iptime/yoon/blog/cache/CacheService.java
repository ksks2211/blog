package org.iptime.yoon.blog.cache;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
 * @author rival
 * @since 2024-01-20
 */
@Service
@RequiredArgsConstructor
public class CacheService {

    @Value("${spring.data.redis.cache-prefix}")
    private String CACHE_PREFIX;


    private final StringRedisTemplate redisTemplate;
    private String getCacheKey(String cacheName, Long id){
        return CACHE_PREFIX+cacheName+"::"+id.toString();
    }


    public void deleteCache(String cacheName, Long id){
        String key = getCacheKey(cacheName, id);
        redisTemplate.delete(key);
    }

    public void deleteCaches(String cacheName, List<Long> ids){
        Collection<String> keys = ids.stream().map(id -> getCacheKey(cacheName, id)).toList();

        redisTemplate.delete(keys);
    }


}
