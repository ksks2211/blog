package org.iptime.yoon.blog.post.dto;

import lombok.Data;
import org.iptime.yoon.blog.post.entity.Post;

import java.io.Serial;
import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.*;

/**
 * @author rival
 * @since 2023-08-11
 */
@Data
public class PostResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;
    private String title;
    private String content;
    private String writer;
    private List<String> tags;
    private String createdAt;
    private String updatedAt;
    private String category;

}
