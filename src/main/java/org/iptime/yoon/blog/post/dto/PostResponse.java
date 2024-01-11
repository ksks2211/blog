package org.iptime.yoon.blog.post.dto;

import lombok.Data;
import org.iptime.yoon.blog.post.entity.Post;

import java.io.Serializable;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

/**
 * @author rival
 * @since 2023-08-11
 */
@Data
public class PostResponse implements Serializable {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private Set<String> tags;
    private String createdAt;
    private String updatedAt;
    private String category;

    public static PostResponse fromEntity(Post post, Collection<String> tags, String category){
        PostResponse postResponse = new PostResponse();
        postResponse.setId(post.getId());
        postResponse.setTitle(post.getTitle());
        postResponse.setContent(post.getContent());
        postResponse.setWriter(post.getWriterName());
        postResponse.setTags(new HashSet<>(tags));
        postResponse.setCreatedAt(post.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME));
        postResponse.setUpdatedAt(post.getUpdatedAt().format(DateTimeFormatter.ISO_DATE_TIME));
        postResponse.setCategory(category);
        return postResponse;
    }
}
