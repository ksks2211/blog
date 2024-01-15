package org.iptime.yoon.blog.post.dto;

import lombok.Data;
import org.iptime.yoon.blog.post.repository.projection.PostPreviewProjection;

import java.io.Serial;
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

}
