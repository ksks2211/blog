package org.iptime.yoon.blog.service;

import org.iptime.yoon.blog.dto.internal.CategoryDto;
import org.iptime.yoon.blog.entity.Category;
import org.iptime.yoon.blog.exception.CategoryNotEmptyException;

import java.util.Map;

/**
 * @author rival
 * @since 2023-09-01
 */
public interface CategoryService {
    void increasePostCount(Category category);
    void decreasePostCount(Category category);
    Category getCategory(String root, String sub);
    Long createCategoryIfNotExists(String root, String sub);

    void changeCategory(String root, String beforeSub, String afterSub);
    void deleteCategoryIfEmpty(String root, String sub) throws CategoryNotEmptyException;

    Map<String, CategoryDto> getCategories(String root);
}
