package org.iptime.yoon.blog.service;

import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.iptime.yoon.blog.dto.req.PostReqDto;
import org.iptime.yoon.blog.dto.res.PostPageResDto;
import org.iptime.yoon.blog.dto.res.PostPreviewDto;
import org.iptime.yoon.blog.dto.res.PostResDto;
import org.iptime.yoon.blog.entity.Post;
import org.iptime.yoon.blog.exception.PostEntityNotFoundException;
import org.iptime.yoon.blog.repository.PostRepository;
import org.iptime.yoon.blog.repository.projection.PostPreview;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author rival
 * @since 2023-08-11
 */

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService{

    private final PostRepository postRepository;
    @Override
    public PostResDto create(PostReqDto postReqDto) {
        Post post = postReqDto.toEntity();
        return PostResDto.fromEntity(postRepository.save(post));
    }

    @Override
    public PostResDto findById(Long id) throws EntityNotFoundException {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostEntityNotFoundException(id));
        return PostResDto.fromEntity(post);
    }

    @Override
    public PostPageResDto findPostList(Pageable pageable) {
        PostPageResDto postListRes = new PostPageResDto();
        Page<PostPreview> postPreviewPage = postRepository.findPostList(pageable);

        List<PostPreviewDto> postList = postPreviewPage.getContent().stream().map(PostPreviewDto::fromPostPreview).toList();

        postListRes.setPostList(postList);
        postListRes.setTotalPages(postPreviewPage.getTotalPages());

        return postListRes;
    }


    @Override
    @Transactional
    public PostResDto update(Long id, PostReqDto postReqDto) {
        Post post = postRepository.findById(id).orElseThrow(() -> new PostEntityNotFoundException(id));

        post.setTitle(postReqDto.getTitle());
        post.setContent(postReqDto.getContent());

        return PostResDto.fromEntity(postRepository.save(post));
    }

    @Override
    @Transactional
    public void delete(Long id) {
        postRepository.findById(id).orElseThrow(() -> new PostEntityNotFoundException(id));
        postRepository.deleteById(id);
    }
}
