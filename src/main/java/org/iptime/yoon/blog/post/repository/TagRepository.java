package org.iptime.yoon.blog.post.repository;

import org.iptime.yoon.blog.post.entity.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author rival
 * @since 2023-08-30
 */
public interface TagRepository extends JpaRepository<Tag,Long> {

    Optional<Tag> findByValue(String value);
}
