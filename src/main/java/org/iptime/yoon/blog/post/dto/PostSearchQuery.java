package org.iptime.yoon.blog.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author rival
 * @since 2024-01-15
 */


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostSearchQuery {

    private String writer;
    private List<
        @NotBlank(message="Keyword cannot be blank")
        @Pattern(regexp =PostValidationConstants.TAG_RULE_REGEX, message = PostValidationConstants.TAG_RULE_MESSAGE) String> tags = new ArrayList<>();
    private boolean allTags = false;
}
