package org.iptime.yoon.blog.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author rival
 * @since 2023-08-11
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostCreateRequest {

    @NotBlank(message = "Title cannot be empty")
    private String title;
    private String content;
    private String description;
    private List<
            @NotBlank(message="Keyword cannot be blank")
            @Pattern(regexp = PostValidationConstants.TAG_RULE_REGEX, message = PostValidationConstants.TAG_RULE_MESSAGE) String> tags = new ArrayList<>();


    @NotBlank(message = "Category cannot be empty value")
    @Pattern(regexp = PostValidationConstants.CATEGORY_DEPTH_REGEX, message = PostValidationConstants.CATEGORY_DEPTH_MESSAGE)
    private String category;

}
