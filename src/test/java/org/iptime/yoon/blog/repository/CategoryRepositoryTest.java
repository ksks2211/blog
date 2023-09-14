package org.iptime.yoon.blog.repository;

import org.iptime.yoon.blog.config.JpaConfig;
import org.iptime.yoon.blog.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

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