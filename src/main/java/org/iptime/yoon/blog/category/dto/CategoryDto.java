package org.iptime.yoon.blog.category.dto;

import lombok.*;

import java.util.HashMap;
import java.util.Map;

/**
 * @author rival
 * @since 2023-09-14
 */

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    @Builder.Default
    int numOfPosts = 0;

    @Builder.Default
    int numOfAllPosts = 0;

    @Builder.Default
    Map<String, CategoryDto> subCategories = new HashMap<>();
}
