package org.iptime.yoon.blog.post.repository;

import org.iptime.yoon.blog.post.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author rival
 * @since 2023-08-30
 */
public interface TagRepository extends JpaRepository<Tag,Long> {

    boolean existsByValue(String tag);
}
