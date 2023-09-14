package org.iptime.yoon.blog.service;

import org.iptime.yoon.blog.dto.CategoryDto;

import java.util.Map;

/**
 * @author rival
 * @since 2023-09-01
 */
public interface CategoryService {
    Long createIfNotExists(String root, String sub);
    void deleteIfEmpty(String root, String sub);

    Map<String, CategoryDto> getCategories(String root);
}
