package org.iptime.yoon.blog.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * @author rival
 * @since 2023-09-01
 */
public interface CategoryRepository extends JpaRepository<Category,Long> {
    Optional<Category> findByFullName(String fullName);
    List<Category> findAllByRoot(String root);

    @Query(value = "select p.id from Category c JOIN c.posts p where c.fullName = :fullName")
    List<Long> findRelatedPostIds(String fullName);
}
