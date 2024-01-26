package org.iptime.yoon.blog.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

/**
 * @author rival
 * @since 2024-01-15
 */
@Data
public class PostSearchQuery {

    private String writer;
    private List<
        @NotBlank(message="Keyword cannot be blank")
        @Pattern(regexp = "^[a-zA-z0-9]*$", message = "Must be alphanumeric") String> tags;
}
