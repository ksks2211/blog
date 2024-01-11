package org.iptime.yoon.blog.repository;

import org.iptime.yoon.blog.category.Category;
import org.iptime.yoon.blog.category.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author rival
 * @since 2023-09-14
 */

@SpringBootTest
class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void test(){

        Category cate = Category.builder()
            .fullName("ADMIN/dir1/dir2")
            .root("ADMIN")
            .build();

        categoryRepository.save(cate);


        Category category = categoryRepository.findByFullName("ADMIN/dir1/dir2").get();

        assertThat(category).isNotNull();

        assertThat(category.getFullName()).isEqualTo("ADMIN/dir1/dir2");


    }

}