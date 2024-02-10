package org.iptime.yoon.blog.post.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author rival
 * @since 2023-08-11
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class PostResponse extends PostPreviewResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String content;
    private List<String> tags;
    private String category;
}
