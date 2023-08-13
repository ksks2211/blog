package org.iptime.yoon.blog.dto.res;

import lombok.Data;

import java.util.List;

/**
 * @author rival
 * @since 2023-08-12
 */

@Data
public class PostPageResDto {
    private Integer totalPages;
    private List<PostPreviewDto> postList;
}
