package org.iptime.yoon.blog.post.entity;

import jakarta.persistence.PreRemove;
import org.iptime.yoon.blog.common.BeanUtil;
import org.iptime.yoon.blog.post.PostMapper;
import org.iptime.yoon.blog.post.dto.PostResponse;
import org.iptime.yoon.blog.post.repository.DeletedPostRepository;
import org.iptime.yoon.blog.post.service.PostService;

import java.time.LocalDateTime;

/**
 * @author rival
 * @since 2024-01-21
 */
public class PostEntityListener {

    @PreRemove
    public void onPreRemove(Post post){
        PostMapper postMapper = BeanUtil.getBean(PostMapper.class);
        PostService postService = BeanUtil.getBean(PostService.class);
        PostResponse postResponse = postService.findById(post.getId());

        DeletedPost deletedPost = postMapper.postToDeletedPost(post);
        deletedPost.setDeletedAt(LocalDateTime.now());
        deletedPost.setCategory(postResponse.getCategory());
        deletedPost.setTags(postResponse.getTags());

        DeletedPostRepository deletedPostRepository = BeanUtil.getBean(DeletedPostRepository.class);
        deletedPostRepository.save(deletedPost);
    }
}
