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
        @Pattern(regexp = "^[a-zA-z0-9]*$", message = "Must be alphanumeric") String> tags = new HashSet<>();


    // "^/(\w+(/\w+){0,7})?$"
    @Pattern(regexp = "^/(\\w+(/\\w+){0,7})?$", message = "Category depth cannot be deeper than 8")
    private String category = "/";

}
