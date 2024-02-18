package org.iptime.yoon.blog.service;

import org.iptime.yoon.blog.cache.CacheService;
import org.iptime.yoon.blog.category.Category;
import org.iptime.yoon.blog.category.CategoryRepository;
import org.iptime.yoon.blog.category.CategoryServiceImpl;
import org.iptime.yoon.blog.category.dto.CategoryInfoDto;
import org.iptime.yoon.blog.category.exception.CategoryNotEmptyException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

/**
 * @author rival
 * @since 2023-09-13
 */
@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Mock
    private CacheService cacheService;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @Test
    public void createCategoryTest(){

        final long[] idCounter = {0L};

        List<Category> categoryList = new ArrayList<>();
        String username = "userA";
        String sub1 = "/java/spring-boot";
        String sub2 = "/java/android";
        String sub3 = "/python/tensorflow";
        String sub4 = "/js/express";

        when(categoryRepository.findByFullName(any(String.class))).thenAnswer(invocation -> {
            String fullName = invocation.getArgument(0);
            return Optional.of(Category.builder().root(username).fullName(fullName).build());
        });
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> {
            Category category = invocation.getArgument(0);
            idCounter[0]++;
            category.setId(idCounter[0]);
            categoryList.add(category);
            return category;
        });
        when(categoryRepository.findAllByRoot(any(String.class))).thenReturn(categoryList);


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


    @Test
    public void deleteCategoryTestWhenEmpty() {

        // Given
        String username = "userB";
        String sub = "/java/spring-boot";

        when(categoryRepository.findByFullName(any(String.class))).thenAnswer(invocation -> {
            String fullName = invocation.getArgument(0);
            return Optional.of(Category.builder().root(username).fullName(fullName).build());
        });
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> {
            Category category = invocation.getArgument(0);
            category.setId(1L);
            return category;
        });
        doNothing().when(categoryRepository).delete(any(Category.class));
        doNothing().when(cacheService).deleteCaches(any(), any());

        categoryService.createCategoryIfNotExists(username, sub);

        // When
        categoryService.deleteCategoryIfEmpty(username,sub);

        // Then
        Category category = categoryService.getCategory(username, sub);
        assertThat(category.getId()).isNull();
    }

    @Test
    public void deleteCategoryFailTestWhenNotEmpty() {

        // Given
        String username = "userB";
        String sub = "/java/spring-boot";
        Integer count = 1;

        when(categoryRepository.findByFullName(any(String.class))).thenAnswer(invocation -> {
            String fullName = invocation.getArgument(0);
            return Optional.of(Category.builder().root(username).fullName(fullName).postCount(count).build());
        });
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> {
            Category category = invocation.getArgument(0);
            category.setId(1L);
            return category;
        });

        categoryService.createCategoryIfNotExists(username, sub);

        assertThrows( CategoryNotEmptyException.class, ()-> categoryService.deleteCategoryIfEmpty(username,sub));
    }

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



}