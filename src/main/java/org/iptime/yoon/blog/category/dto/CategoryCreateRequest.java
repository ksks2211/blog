package org.iptime.yoon.blog.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import org.iptime.yoon.blog.post.dto.PostValidationConstants;

/**
 * @author rival
 * @since 2023-10-03
 */

@Data
public class CategoryCreateRequest {

    @NotBlank(message = "Category cannot be empty value")
    @Pattern(regexp = PostValidationConstants.CATEGORY_DEPTH_REGEX, message = PostValidationConstants.CATEGORY_DEPTH_MESSAGE)
    private String category;
}
