package org.iptime.yoon.blog.service;

import org.apache.commons.lang3.StringUtils;
import org.iptime.yoon.blog.config.JpaConfig;
import org.iptime.yoon.blog.dto.CategoryDto;
import org.iptime.yoon.blog.entity.Category;
import org.iptime.yoon.blog.exception.CategoryNotEmptyException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author rival
 * @since 2023-09-13
 */
@SpringBootTest
@Import(JpaConfig.class)
class CategoryServiceTest {

    @Autowired
    private CategoryService categoryService;

    @Test
    public void createCategoryTest(){
        String root = "userA";
        String sub1 = "/java/spring-boot";
        String sub2 = "/java/android";
        String sub3 = "/python/tensorflow";
        String sub4 = "/js/express";
        categoryService.createCategoryIfNotExists(root,sub1);
        categoryService.createCategoryIfNotExists(root,sub2);
        categoryService.createCategoryIfNotExists(root,sub3);
        categoryService.createCategoryIfNotExists(root,sub4);
        Map<String, CategoryDto> categories = categoryService.getCategories(root);

        String join = StringUtils.join(categories);
        System.out.println(join);
        assertThat(categories.isEmpty()).isFalse();
        assertThat(categories.get(root).getSubCategories().size()).isEqualTo(3);
    }


    @Test
    public void deleteCategoryTest() throws CategoryNotEmptyException {

        // Given
        String root = "userB";
        String sub = "/java/spring-boot";
        categoryService.createCategoryIfNotExists(root, sub);


        // When
        categoryService.deleteCategoryIfEmpty(root,sub);

        // Then
        Category category = categoryService.getCategory(root, sub);
        assertThat(category.getId()).isNull();
    }


    @Test
    public void updateCategoryTest(){

        // Given
        String root = "userC";
        String sub = "/java/spring-boot";
        String sub2 = "/JAVA/SPRING_BOOT";
        Long id = categoryService.createCategoryIfNotExists(root, sub);

        // When
        categoryService.changeCategory(root,sub,sub2);


        // Then
        Category category = categoryService.getCategory(root, sub2);
        assertThat(category.getId()).isNotNull();
        assertThat(category.getId()).isEqualTo(id);

    }



}