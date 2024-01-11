package org.iptime.yoon.blog.post.dto;

import lombok.Data;
import org.iptime.yoon.blog.post.repository.projection.PostPreviewProjection;

import java.time.format.DateTimeFormatter;

/**
 * @author rival
 * @since 2023-08-12
 */
@Data
public class PostPreviewResponse {

    private Long id;
    private String title;
    private String createdAt;
    private String updatedAt;
    private String writer;
    private String description;

    public static PostPreviewResponse fromPostPreview(PostPreviewProjection post){
        PostPreviewResponse dto = new PostPreviewResponse();
        dto.setId(post.getId());
        dto.setWriter(post.getWriterName());
        dto.setTitle(post.getTitle());
        dto.setDescription(post.getDescription());
        dto.setCreatedAt(post.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME));
        dto.setUpdatedAt(post.getUpdatedAt().format(DateTimeFormatter.ISO_DATE_TIME));
        return dto;
    }
}
