package org.iptime.yoon.blog.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.iptime.yoon.blog.dto.req.PostReqDto;
import org.iptime.yoon.blog.dto.res.PostPageResDto;
import org.iptime.yoon.blog.dto.res.PostPreviewDto;
import org.iptime.yoon.blog.dto.res.PostResDto;
import org.iptime.yoon.blog.entity.Category;
import org.iptime.yoon.blog.entity.Post;
import org.iptime.yoon.blog.entity.PostTag;
import org.iptime.yoon.blog.entity.Tag;
import org.iptime.yoon.blog.exception.PostEntityNotFoundException;
import org.iptime.yoon.blog.repository.CategoryRepository;
import org.iptime.yoon.blog.repository.PostRepository;
import org.iptime.yoon.blog.repository.PostTagRepository;
import org.iptime.yoon.blog.repository.TagRepository;
import org.iptime.yoon.blog.repository.projection.PostPreviewProjection;
import org.iptime.yoon.blog.security.dto.User;
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

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final PostTagRepository postTagRepository;
    private final TagRepository tagRepository;
    private final CategoryRepository categoryRepository;


    @Override
    @Transactional
    public PostResDto create(PostReqDto postReqDto, User user) {

        // Create category
        String userCategory = user.getUsername() + postReqDto.getCategory();
        Category category = categoryRepository.findByCategory(userCategory)
            .orElseGet(() ->
                Category.builder()
                    .category(userCategory)
                    .root(user.getUsername()).build());
        category.increaseFileCount();
        categoryRepository.save(category);

        // Create post
        Post post = postReqDto.toEntity(user.getId(), user.getUsername());
        post.setCategory(category);
        postRepository.save(post);

        // Create tags
        List<Tag> tags = new ArrayList<>();
        List<PostTag> postTags = new ArrayList<>();
        postReqDto.getTags().forEach(tag -> {
            if (!tagRepository.existsByTag(tag)) {
                Tag newTag = new Tag();
                newTag.setTag(tag);
                tags.add(newTag);
                PostTag postTag = PostTag.builder()
                    .post(post)
                    .tag(newTag).build();
                postTags.add(postTag);
            }
        });
        tagRepository.saveAll(tags);
        postTagRepository.saveAll(postTags);

        return PostResDto.fromEntity(post, postReqDto.getTags(),userCategory);
    }

    @Override
    @Cacheable(value = "posts", key = "#id")
    @Transactional
    public PostResDto findById(Long id) throws EntityNotFoundException {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostEntityNotFoundException(id));
        List<String> tags = postTagRepository.findAllTagsByPostId(id);
        return PostResDto.fromEntity(post, tags, post.getCategory().getCategory());
    }

    @Override
    public PostPageResDto findPostList(Pageable pageable) {
        PostPageResDto postListRes = new PostPageResDto();
        Page<PostPreviewProjection> postPreviewPage = postRepository.findPostList(pageable);
        List<PostPreviewDto> postList = postPreviewPage.getContent().stream().map(PostPreviewDto::fromPostPreview).toList();
        postListRes.setPostList(postList);
        postListRes.setTotalPages(postPreviewPage.getTotalPages());
        return postListRes;
    }


    @Override
    @Transactional
    @CachePut(value = "posts", key = "#id")
    public PostResDto update(Long id, PostReqDto postReqDto) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostEntityNotFoundException(id));
        post.setTitle(postReqDto.getTitle());
        post.setContent(postReqDto.getContent());
        List<String> tags = postTagRepository.findAllTagsByPostId(post.getId());
        return PostResDto.fromEntity(postRepository.save(post), tags, post.getCategory().getCategory());
    }
    // changeTags

    // changeCategory
    @Override
    @Transactional
    @CacheEvict(value = "posts", key = "#id")
    public void delete(Long id) {
        postTagRepository.deleteAllByPost(Post.builder().id(id).build());
        postRepository.findById(id).orElseThrow(() -> new PostEntityNotFoundException(id));
        postRepository.deleteById(id);
    }
}
