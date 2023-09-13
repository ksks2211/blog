package org.iptime.yoon.blog.repository;

import org.iptime.yoon.blog.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author rival
 * @since 2023-09-01
 */
public interface CategoryRepository extends JpaRepository<Category,Long> {
    Optional<Category> findByCategory(String category);
}
