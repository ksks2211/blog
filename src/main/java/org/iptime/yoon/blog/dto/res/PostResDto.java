package org.iptime.yoon.blog.dto.res;

import lombok.Data;
import org.iptime.yoon.blog.entity.Post;

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
public class PostResDto implements Serializable {
    private Long id;
    private String title;
    private String content;
    private String writer;
    private Set<String> tags;
    private String createdAt;
    private String updatedAt;
    private String category;

    public static PostResDto fromEntity(Post post, Collection<String> tags, String category){
        PostResDto postResDto = new PostResDto();
        postResDto.setId(post.getId());
        postResDto.setTitle(post.getTitle());
        postResDto.setContent(post.getContent());
        postResDto.setWriter(post.getWriterName());
        postResDto.setTags(new HashSet<>(tags));
        postResDto.setCreatedAt(post.getCreatedAt().format(DateTimeFormatter.ISO_DATE_TIME));
        postResDto.setUpdatedAt(post.getUpdatedAt().format(DateTimeFormatter.ISO_DATE_TIME));
        postResDto.setCategory(category);
        return postResDto;
    }
}
