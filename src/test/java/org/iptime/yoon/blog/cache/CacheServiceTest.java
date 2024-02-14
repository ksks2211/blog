package org.iptime.yoon.blog.cache;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * @author rival
 * @since 2024-02-14
 */



@ExtendWith(MockitoExtension.class)
@ContextConfiguration
class CacheServiceTest {

    @Mock
    StringRedisTemplate stringRedisTemplate;

    @InjectMocks
    private CacheServiceImpl cacheService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(cacheService, "CACHE_PREFIX", "testPrefix:");
    }


    @Test
    void testGetCacheKey() {
        String cacheName = "userCache";
        Object id = 1;
        String expectedKey = "testPrefix:userCache::1";
        String actualKey = cacheService.getCacheKey(cacheName, id);
        assertEquals(expectedKey, actualKey, "Cache key generated is not as expected.");
    }

    @Test
    @SuppressWarnings("unchecked")
    void testDeleteCaches() {
        String cacheName = "userCache";
        List<Integer> ids = Arrays.asList(1, 2, 3);
        when(stringRedisTemplate.delete(any(Collection.class))).thenReturn(3L);
        cacheService.deleteCaches(cacheName, ids);
        verify(stringRedisTemplate, times(1)).delete(any(Collection.class));
    }

}