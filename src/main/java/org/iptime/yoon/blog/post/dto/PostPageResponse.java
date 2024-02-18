package org.iptime.yoon.blog.post.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rival
 * @since 2023-08-12
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostPageResponse {
    private Integer totalPages;
    private List<PostPreviewResponse> postList = new ArrayList<>();
}
