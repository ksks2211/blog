package org.iptime.yoon.blog.cache;

import java.util.List;

/**
 * @author rival
 * @since 2024-02-14
 */
public interface CacheService {

    String getCacheKey(String cacheName, Object id);

    void deleteCaches(String cacheName, List<?> ids);

}
