package org.iptime.yoon.blog.post.repository;

import org.iptime.yoon.blog.post.entity.Post;
import org.iptime.yoon.blog.post.entity.PostTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author rival
 * @since 2023-08-30
 */
public interface PostTagRepository extends JpaRepository<PostTag, Long> {

    @Query("select pt from PostTag pt where pt.tag.value = :tag")
    List<PostTag> findAllByTag(String tag);


    @Query("select pt.tag.value from PostTag pt where pt.post.id = :id")
    List<String> findAllTagsByPostId(Long id);


    @Modifying
    void deleteAllByPost(Post post);
}
