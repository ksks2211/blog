package org.iptime.yoon.blog.post.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.iptime.yoon.blog.category.Category;
import org.iptime.yoon.blog.category.CategoryService;
import org.iptime.yoon.blog.post.PostEntityNotFoundException;
import org.iptime.yoon.blog.post.PostMapper;
import org.iptime.yoon.blog.post.PostSpecification;
import org.iptime.yoon.blog.post.dto.*;
import org.iptime.yoon.blog.post.entity.Post;
import org.iptime.yoon.blog.post.entity.Tag;
import org.iptime.yoon.blog.post.repository.PostRepository;
import org.iptime.yoon.blog.post.repository.PostTagRepository;
import org.iptime.yoon.blog.post.repository.TagRepository;
import org.iptime.yoon.blog.post.repository.projection.PostPreviewDto;
import org.iptime.yoon.blog.post.repository.projection.PostPreviewProjection;
import org.iptime.yoon.blog.security.auth.JwtUser;
import org.iptime.yoon.blog.user.entity.BlogUser;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author rival
 * @since 2023-08-11
 */

@Service
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

        // Create Category
        Category category = categoryService.getCategory(authUser.getUsername(), postCreateRequest.getCategory());
        categoryService.increasePostCount(category);

        // Create|Fetch Tags
        List<Tag> tags = new ArrayList<>();
        for (String tagValue : postCreateRequest.getTags()) {
            Tag tag = tagRepository.findByValue(tagValue).orElseGet(() -> new Tag(tagValue));
            tags.add(tag);
        }
        tagRepository.saveAll(tags);

        // Mapping to PostRequest to Post
        Post post = postMapper.postCreateRequestToPost(postCreateRequest);


        // Fill Related Fields
        tags.forEach(post::addTag);
        post.setWriterName(authUser.getUsername());
        post.setWriterDisplayName(authUser.getDisplayName());
        post.setWriter(BlogUser.builder().id(authUser.getId()).build());
        post.setCategory(category);


        // Save Entity
        postRepository.save(post);

        // Return Created Post id
        return post.getId();
    }

    @Override
    @Cacheable(value = "posts", key = "#id")
    @Transactional
    public PostResponse findById(Long id) throws EntityNotFoundException {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostEntityNotFoundException(id));
        String category = post.getCategory().getFullName();
        List<String> tags = postTagRepository.findAllTagsByPostId(id);


        return postMapper.postToPostResponse(post, tags, category);
    }

    @Override
    public PostPageResponse findPostList(Pageable pageable) {
        Page<PostPreviewProjection> postPreviewPage = postRepository.findProjectedBy(pageable);
        return postMapper.postPreviewPageToPostPageResponse(postPreviewPage);
    }

    @Override
    @Transactional
    public PostPageResponse findPostListByCategory(Pageable pageable, String root, String sub) {
        Category category = categoryService.getCategory(root, sub);
        if(category.getId()==null){
            PostPageResponse emptyResponse = new PostPageResponse();
            emptyResponse.setTotalPages(0);
            return emptyResponse;
        }
        Page<PostPreviewProjection> postPreviewPage = postRepository.findPostListByCategory(pageable,category);
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
        return postMapper.postToPostResponse(post, tags, category);
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
        postRepository.delete(post);
    }

    @Override
    @Transactional
    public boolean isOwner(Long id, String username) {
        Optional<Post> optionalPost = postRepository.findById(id);
        if(optionalPost.isEmpty())return false;
        Post post = optionalPost.get();
        String writerName = post.getWriterName();
        return writerName.equals(username);
    }

    @Override
    @Transactional
    public PostPageResponse searchPostList(PostSearchQuery postSearchQuery, PageRequest pageable) {
        Specification<Post> spec = Specification.where(null);
        if (postSearchQuery != null) {
            if (postSearchQuery.getWriter() != null) {
                spec = spec.and(PostSpecification.wroteBy(postSearchQuery.getWriter()));
            }
            List<String> tags = postSearchQuery.getTags();
            if (tags != null && !tags.isEmpty()) {
                spec = spec.and(PostSpecification.haveAtLeastOneTag(tags.toArray(new String[0])));
            }
        }

        Page<PostPreviewDto> result = postRepository.searchAllPosts(spec, pageable);
        return postMapper.postPreviewDtoPageToPostPageResponse(result);
    }
}
