package org.iptime.yoon.blog.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author rival
 * @since 2023-08-12
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostPreviewResponse {

    private Long id;
    private String title;
    private String createdAt;
    private String updatedAt;
    private String writer;
    private String writerDisplayName;
    private String description;

}
