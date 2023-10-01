package org.iptime.yoon.blog.service;

import org.apache.commons.lang3.StringUtils;
import org.iptime.yoon.blog.config.JpaConfig;
import org.iptime.yoon.blog.dto.CategoryDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.Map;

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
        String root = "ADMIN";
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

    }
}