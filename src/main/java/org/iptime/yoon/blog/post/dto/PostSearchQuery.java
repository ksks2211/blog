package org.iptime.yoon.blog.post.dto;

import lombok.Data;

import java.util.List;

/**
 * @author rival
 * @since 2024-01-15
 */
@Data
public class PostSearchQuery {
    private String writer;
    private List<String> tags;
}
