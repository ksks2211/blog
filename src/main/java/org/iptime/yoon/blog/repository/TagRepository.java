package org.iptime.yoon.blog.repository;

import org.iptime.yoon.blog.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author rival
 * @since 2023-08-30
 */
public interface TagRepository extends JpaRepository<Tag,Long> {
}
