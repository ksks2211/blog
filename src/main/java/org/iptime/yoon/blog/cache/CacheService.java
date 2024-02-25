package org.iptime.yoon.blog.cache;

import org.iptime.yoon.blog.post.dto.PostResponse;

import java.util.List;

/**
 * @author rival
 * @since 2024-02-14
 */
public interface CacheService {

    String getCacheKey(String cacheName, Object id);

    void deleteCaches(String cacheName, List<?> ids);

    void createPostCache(String cacheName, Object id, PostResponse value);

}
