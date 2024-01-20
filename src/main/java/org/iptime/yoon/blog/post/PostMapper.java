package org.iptime.yoon.blog.post;

import org.iptime.yoon.blog.post.dto.PostCreateRequest;
import org.iptime.yoon.blog.post.dto.PostPageResponse;
import org.iptime.yoon.blog.post.dto.PostPreviewResponse;
import org.iptime.yoon.blog.post.dto.PostResponse;
import org.iptime.yoon.blog.post.entity.Post;
import org.iptime.yoon.blog.post.repository.projection.PostPreviewDto;
import org.iptime.yoon.blog.post.repository.projection.PostPreviewProjection;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.Named;
import org.springframework.data.domain.Page;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * @author rival
 * @since 2024-01-15
 */
@Mapper(componentModel = "spring")
public interface PostMapper {

    @Mappings({
        @Mapping(source = "writerName", target = "writer"),
        @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "localDateTimeConverter"),
        @Mapping(source = "updatedAt", target = "updatedAt", qualifiedByName = "localDateTimeConverter"),
        @Mapping(target = "category", ignore = true),
        @Mapping(target = "tags", ignore = true)
    })
    PostResponse postToPostResponse(Post post);

    @Mappings({
        @Mapping(source = "writerName", target = "writer"),
        @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "localDateTimeConverter"),
        @Mapping(source = "updatedAt", target = "updatedAt", qualifiedByName = "localDateTimeConverter")
    })
    PostPreviewResponse postProjToPostRes(PostPreviewProjection projection);


    @Mappings({
        @Mapping(source = "writerName", target = "writer"),
        @Mapping(source = "createdAt", target = "createdAt", qualifiedByName = "localDateTimeConverter"),
        @Mapping(source = "updatedAt", target = "updatedAt", qualifiedByName = "localDateTimeConverter")
    })
    PostPreviewResponse postDtoToPostRes(PostPreviewDto projection);



    @Mappings({
        @Mapping(target = "category", ignore = true),
        @Mapping(target = "postTags", ignore = true),
        @Mapping(target = "id", ignore = true),
        @Mapping(target="writer", ignore = true),
        @Mapping(target = "writerName",ignore = true)
    })
    Post postCreateRequestToPost(PostCreateRequest request);


    @Mapping(source = "content", target = "postList")
    PostPageResponse postPreviewPageToPostPageResponse(Page<PostPreviewProjection> postPreviewPage);

    @Mapping(source = "content", target = "postList")
    PostPageResponse postPreviewDtoPageToPostPageResponse(Page<PostPreviewDto> postPreviewPage);


    default PostResponse postToPostResponse(Post post, List<String> tags, String category) {
        PostResponse postResponse = postToPostResponse(post);
        postResponse.setTags(tags);
        postResponse.setCategory(category);
        return postResponse;
    }





    @Named("localDateTimeConverter")
    default String localDateTimeConverter(LocalDateTime localDateTime) {
        return localDateTime.format(DateTimeFormatter.ISO_DATE_TIME);
    }


}
