package org.iptime.yoon.blog.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

/**
 * @author rival
 * @since 2023-08-11
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostResponse extends PostPreviewResponse implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private String content;
    private List<String> tags;
    private String category;
}
