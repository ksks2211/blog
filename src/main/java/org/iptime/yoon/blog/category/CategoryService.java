package org.iptime.yoon.blog.category;

import org.iptime.yoon.blog.category.dto.CategoryDto;
import org.iptime.yoon.blog.category.exception.CategoryNotEmptyException;

import java.util.List;
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

    Map<String, List<String>> getCategoryList(String username);
}
