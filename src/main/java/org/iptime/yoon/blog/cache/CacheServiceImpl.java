package org.iptime.yoon.blog.cache;

import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;

/**
* Service layer for caching response data to the clients
 */
@Service
@RequiredArgsConstructor
public class CacheServiceImpl implements CacheService{

    @Value("${spring.data.redis.cache-prefix}")
    private String CACHE_PREFIX;
    private final StringRedisTemplate redisTemplate;


    /**
     * Generate cache-key with the key-generating rule
     *
     * @param cacheName cache name used for @Cacheable
     * @param id String or Integer id of @Cacheable
     * @return actual cache-key used for redis
     */
    @NotNull
    @Contract(pure = true)
    public String getCacheKey(String cacheName, Object id){
        return CACHE_PREFIX+cacheName+"::"+id.toString();
    }


    /**
     * Delete cached data in batch
     *
     * @param cacheName cache name used for @Cacheable
     * @param ids ids used for @Cacheable
     */
    public void deleteCaches(String cacheName, List<?> ids){
        Collection<String> keys = ids.stream().map(id -> getCacheKey(cacheName, id)).toList();
        redisTemplate.delete(keys);
    }

}
