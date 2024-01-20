package org.iptime.yoon.blog.post.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rival
 * @since 2023-08-12
 */

@Data
public class PostPageResponse {
    private Integer totalPages;
    private List<PostPreviewResponse> postList = new ArrayList<>();
}
