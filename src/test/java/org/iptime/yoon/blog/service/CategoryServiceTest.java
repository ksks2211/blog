package org.iptime.yoon.blog.service;

import org.iptime.yoon.blog.cache.CacheService;
import org.iptime.yoon.blog.category.CategoryRepository;
import org.iptime.yoon.blog.category.CategoryService;
import org.iptime.yoon.blog.category.CategoryServiceImpl;
import org.iptime.yoon.blog.category.dto.CategoryInfoDto;
import org.iptime.yoon.blog.common.config.JpaConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author rival
 * @since 2023-09-13
 */
@DataJpaTest
@Import(JpaConfig.class)
class CategoryServiceTest {

    @MockBean
    private CacheService cacheService;

    @Autowired
    private CategoryRepository categoryRepository;

    private CategoryService categoryService;


    @BeforeEach
    void setUp(){
        categoryService = new CategoryServiceImpl(categoryRepository, cacheService);
    }

    @Test
    public void createCategoryTest(){
        String username = "userA";
        String sub1 = "/java/spring-boot";
        String sub2 = "/java/android";
        String sub3 = "/python/tensorflow";
        String sub4 = "/js/express";
        categoryService.createCategoryIfNotExists(username,sub1);
        categoryService.createCategoryIfNotExists(username,sub2);
        categoryService.createCategoryIfNotExists(username,sub3);
        categoryService.createCategoryIfNotExists(username,sub4);
        Map<String, CategoryInfoDto> categories = categoryService.getCategories(username);
        Map<String, CategoryInfoDto> rootCategory = categories.get(username).getSubCategories();

        assertThat(categories.isEmpty()).isFalse();
        assertThat(rootCategory.size()).isEqualTo(3);
        assertThat(rootCategory.get("java").getSubCategories().size()).isEqualTo(2);
        assertThat(rootCategory.get("python").getSubCategories().size()).isEqualTo(1);
        assertThat(rootCategory.get("js").getSubCategories().size()).isEqualTo(1);
    }
//
//
//    @Test
//    public void deleteCategoryTest() throws CategoryNotEmptyException {
//
//        // Given
//        String root = "userB";
//        String sub = "/java/spring-boot";
//        categoryService.createCategoryIfNotExists(root, sub);
//
//
//        // When
//        categoryService.deleteCategoryIfEmpty(root,sub);
//
//        // Then
//        Category category = categoryService.getCategory(root, sub);
//        assertThat(category.getId()).isNull();
//    }
//
//
//    @Test
//    public void updateCategoryTest(){
//
//        // Given
//        String root = "userC";
//        String sub = "/java/spring-boot";
//        String sub2 = "/JAVA/SPRING_BOOT";
//        Long id = categoryService.createCategoryIfNotExists(root, sub);
//
//        // When
//        categoryService.changeCategory(root,sub,sub2);
//
//
//        // Then
//        Category category = categoryService.getCategory(root, sub2);
//        assertThat(category.getId()).isNotNull();
//        assertThat(category.getId()).isEqualTo(id);
//
//    }
//


}