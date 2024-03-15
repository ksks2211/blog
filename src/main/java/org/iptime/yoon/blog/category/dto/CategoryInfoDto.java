package org.iptime.yoon.blog.category.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * @author rival
 * @since 2023-09-14
 */
@Schema(description = "Category Info DTO")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CategoryInfoDto {

    @Schema(description = "Number of Posts", example = "10")
    @Builder.Default
    Integer numOfPosts = 0;

    @Schema(description = "Number of All Posts", example = "50")
    @Builder.Default
    Integer numOfAllPosts = 0;

    @Builder.Default
    @Schema(description = "Sub Categories")
    Map<String, CategoryInfoDto> subCategories = new HashMap<>();
}
