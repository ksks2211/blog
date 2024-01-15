package org.iptime.yoon.blog.post.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.iptime.yoon.blog.category.Category;
import org.iptime.yoon.blog.category.CategoryService;
import org.iptime.yoon.blog.post.PostEntityNotFoundException;
import org.iptime.yoon.blog.post.PostMapper;
import org.iptime.yoon.blog.post.dto.*;
import org.iptime.yoon.blog.post.entity.Post;
import org.iptime.yoon.blog.post.entity.PostTag;
import org.iptime.yoon.blog.post.entity.Tag;
import org.iptime.yoon.blog.post.repository.PostRepository;
import org.iptime.yoon.blog.post.repository.PostTagRepository;
import org.iptime.yoon.blog.post.repository.TagRepository;
import org.iptime.yoon.blog.post.repository.projection.PostPreviewProjection;
import org.iptime.yoon.blog.security.auth.JwtUser;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rival
 * @since 2023-08-11
 */

@Service(value="postServiceBean")
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostTagRepository postTagRepository;
    private final TagRepository tagRepository;

    private final PostMapper postMapper;
    private final CategoryService categoryService;


    @Override
    @Transactional
    public Long createPost(PostCreateRequest postCreateRequest, JwtUser authUser) {

        // Create category
        Category category = categoryService.getCategory(authUser.getUsername(), postCreateRequest.getCategory());
        categoryService.increasePostCount(category);


        // Create post
        Post post = postCreateRequest.toEntity(authUser.getId(), authUser.getUsername());
        post.setCategory(category);
        postRepository.save(post);

        // Create tags
        List<Tag> tags = new ArrayList<>();
        List<PostTag> postTags = new ArrayList<>();
        postCreateRequest.getTags().forEach(tagValue -> {
            if (!tagRepository.existsByValue(tagValue)) {
                Tag newTag = new Tag();
                newTag.setValue(tagValue);
                tags.add(newTag);

                PostTag postTag = PostTag.builder()
                    .post(post).tag(newTag).build();
                postTags.add(postTag);
            }
        });
        tagRepository.saveAll(tags);
        postTagRepository.saveAll(postTags);

        return post.getId();
    }

    @Override
    @Cacheable(value = "posts", key = "#id")
    @Transactional
    public PostResponse findById(Long id) throws EntityNotFoundException {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostEntityNotFoundException(id));
        String category = post.getCategory().getFullName();
        List<String> tags = postTagRepository.findAllTagsByPostId(id);


        return postMapper.postToPostResponse(post,tags,category);
    }

    @Override
    public PostPageResponse findPostList(Pageable pageable) {
        Page<PostPreviewProjection> postPreviewPage = postRepository.findPostList(pageable);
        return postMapper.postPreviewPageToPostPageResponse(postPreviewPage);
    }

    @Override
    @Transactional
    public PostPageResponse findPostListByCategory(Pageable pageable, String root, String sub) {
        Long id = categoryService.createCategoryIfNotExists(root, sub);
        Page<PostPreviewProjection> postPreviewPage = postRepository.findPostListByCategory(pageable, Category.builder().id(id).build());
        return postMapper.postPreviewPageToPostPageResponse(postPreviewPage);
    }


    @Override
    @Transactional
    public PostPrevAndNextResponse findPrevAndNextPosts(Long id) {
        PostPrevAndNextResponse prevAndNext = new PostPrevAndNextResponse();
        postRepository.findNextPost(id).ifPresent(
            next -> prevAndNext.setNext(postMapper.postProjToPostRes(next)));
        postRepository.findPrevPost(id).ifPresent(
            prev -> prevAndNext.setPrev(postMapper.postProjToPostRes(prev)));
        return prevAndNext;
    }


    @Override
    @Transactional
    @CachePut(value = "posts", key = "#id")
    public PostResponse updatePost(Long id, PostCreateRequest postCreateRequest) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostEntityNotFoundException(id));
        String category = post.getCategory().getFullName();
        List<String> tags = postTagRepository.findAllTagsByPostId(post.getId());
        return postMapper.postToPostResponse(post,tags,category);
    }
    // changeTags

    // changeCategory
    @Override
    @Transactional
    @CacheEvict(value = "posts", key = "#id")
    public void deletePost(Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostEntityNotFoundException(id));
        Category category = post.getCategory();
        post.setCategory(null);
        categoryService.decreasePostCount(category);

        post.softDelete();
        postTagRepository.deleteAllByPost(post);
        postRepository.save(post);
    }

    @Override
    public boolean isOwner(Long id, String username) {
        return postRepository.findById(id)
            .map(Post::getWriterName)
            .filter(username::equals)
            .isPresent();
    }
}
