package org.iptime.yoon.blog.category.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

/**
 * @author rival
 * @since 2023-10-03
 */

@Data
public class CategoryCreateRequest {

    @NotBlank(message = "Category cannot be empty value")
    @Pattern(regexp = "^(/\\w+){1,7}$", message = "Category depth cannot be deeper than 7")
    private String category;
}
