package org.iptime.yoon.blog.dto.internal;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * @author rival
 * @since 2023-09-14
 */

@Data
@Builder
@ToString
public class CategoryDto {

    @Builder.Default
    int numOfPosts = 0;

    @Builder.Default
    int numOfAllPosts = 0;

    @Builder.Default
    Map<String, CategoryDto> subCategories = new HashMap<>();
}
