package org.iptime.yoon.blog.post.entity;

import jakarta.persistence.*;
import lombok.*;
import org.iptime.yoon.blog.common.entity.StringListConverter;

import java.time.LocalDateTime;
import java.util.List;

/**
 * @author rival
 * @since 2024-01-21
 */

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class DeletedPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long postId;
    private String title;
    private String description;


    @Column(columnDefinition = "TEXT")
    private String content;


    private LocalDateTime createdAt;
    private LocalDateTime deletedAt;


    // Related Fields
    private String writerName;
    private String writerDisplayName;
    private String category;

    @Convert(converter = StringListConverter.class)
    private List<String> tags;

}
