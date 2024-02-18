package org.iptime.yoon.blog.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author rival
 * @since 2023-09-17
 */


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PostPrevAndNextResponse {

    private PostPreviewResponse prev;
    private PostPreviewResponse next;


    @JsonProperty("hasPrev")
    public boolean hasPrev(){
        return prev!=null;
    }

    @JsonProperty("hasNext")
    public boolean hasNext(){
        return next!=null;
    }

}
