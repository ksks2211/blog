package org.iptime.yoon.blog.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.HashSet;
import java.util.Set;

/**
 * @author rival
 * @since 2023-08-11
 */

@Data
public class PostCreateRequest {

    @NotBlank(message = "Title cannot be empty")
    private String title;
    private String content;
    private String description;
    private Set<
        @NotBlank(message="Keyword cannot be blank")
        @Pattern(regexp = PostValidationConstants.TAG_RULE_REGEX, message = PostValidationConstants.TAG_RULE_MESSAGE) String> tags = new HashSet<>();


    @NotBlank(message = "Category cannot be empty value")
    @Pattern(regexp = PostValidationConstants.CATEGORY_DEPTH_REGEX, message = PostValidationConstants.CATEGORY_DEPTH_MESSAGE)
    private String category;

}
