package org.iptime.yoon.blog.post.service;

import lombok.RequiredArgsConstructor;
import org.iptime.yoon.blog.cache.CacheService;
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
    private final CacheService cacheService;



    @Override
    @Transactional
    @CacheEvict(value = "categories", key = "#authUser.username")
    public Long createPost(PostCreateRequest postCreateRequest, JwtUser authUser) {

        // Create Category
        Category category = categoryService.getCategory(authUser.getUsername(), postCreateRequest.getCategory());
        categoryService.increasePostCount(category);

        // Create|Fetch Tags
        List<Tag> tags = new ArrayList<>();
        for (String tagValue : postCreateRequest.getTags()) {



            Tag tag = tagRepository.findByValueIgnoreCase(tagValue).orElseGet(() -> new Tag(tagValue));
            tags.add(tag);
        }
        tagRepository.saveAll(tags);

        // Mapping to PostRequest to Post
        Post post = postMapper.postCreateRequestToPost(postCreateRequest, authUser, category, tags);

        // Save Entity
        postRepository.save(post);



        // Cache created Post
        PostResponse postResponse = postMapper.postToPostResponse(post,postCreateRequest.getTags(), category.getFullName(), category.getName());
        cacheService.createPostCache("posts",post.getId(), postResponse);

        // Return Created Post id
        return post.getId();
    }

    @Override
    @Cacheable(value = "posts", key = "#id")
    @Transactional
    public PostResponse findById(Long id) throws PostEntityNotFoundException {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostEntityNotFoundException(id));
        String category = post.getCategory().getFullName();
        List<String> tags = postTagRepository.findAllTagsByPostId(id);
        return postMapper.postToPostResponse(post, tags, category, post.getCategory().getName());
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
    public PostResponse updatePost(String username,Long id, PostCreateRequest postCreateRequest) {

        Post originalPost = postRepository.findById(id).orElseThrow(() -> new PostEntityNotFoundException(id));

        // Category change of a post
        Category originalCategory = originalPost.getCategory();
        if(!originalCategory.getName().equals(postCreateRequest.getCategory())){

            categoryService.decreasePostCount(originalCategory);
            Category newCategory = categoryService.createCategoryIfNotExists(username, postCreateRequest.getCategory());
            categoryService.increasePostCount(newCategory);
            originalPost.setCategory(newCategory);

            // Delete Cache
            cacheService.deleteCaches("categories",List.of(username));
        }

        List<String> originalTags = postTagRepository.findAllTagsByPostId(originalPost.getId());
        List<Tag> newTags = new ArrayList<>();
        postCreateRequest.getTags().forEach(tag->{
            if(!originalTags.contains(tag)){
                Tag newTag = tagRepository.findByValueIgnoreCase(tag).orElseGet(() -> new Tag(tag));
                newTags.add(newTag);
                originalPost.addTag(newTag);
            }
        });
        tagRepository.saveAll(newTags);

        originalPost.setContent(postCreateRequest.getContent());
        originalPost.setTitle(postCreateRequest.getTitle());
        postRepository.save(originalPost);

        originalTags.addAll(newTags.stream().map(Tag::getValue).toList());
        return postMapper.postToPostResponse(originalPost,originalTags, originalPost.getCategory().getFullName(), originalPost.getCategory().getName());
    }
    // changeTags

    // changeCategory
    @Override
    @Transactional
    @CacheEvict(value = "posts", key = "#id")
    public void deletePost(String username, Long id) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostEntityNotFoundException(id));



        // Category  Relation 정리
        Category category = post.getCategory();
        post.setCategory(null);
        categoryService.decreasePostCount(category);
        cacheService.deleteCaches("categories",List.of(username));


        //  User Relation 정리
        post.setWriter(null);

        // PostTag Relation 정리
//        postTagRepository.deleteAll(post.getPostTags());
        post.removeAllPostTags();

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
            spec = spec.and(PostSpecification.getPostSpecification(postSearchQuery));
        }
        Page<PostPreviewDto> result = postRepository.searchAllPosts(spec, pageable);
        return postMapper.postPreviewDtoPageToPostPageResponse(result);
    }

    @Override
    @Transactional
    public String findCategoryFullName(Long id) {
        return postRepository.findCategoryFullNameById(id).orElse("No Category");
    }
}
