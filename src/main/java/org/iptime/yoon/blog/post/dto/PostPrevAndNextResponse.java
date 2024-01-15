package org.iptime.yoon.blog.post.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

/**
 * @author rival
 * @since 2023-09-17
 */


@Data
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
