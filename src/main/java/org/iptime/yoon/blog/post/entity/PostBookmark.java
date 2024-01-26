package org.iptime.yoon.blog.post.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.iptime.yoon.blog.user.entity.BlogUser;

/**
 * @author rival
 * @since 2023-08-17
 */

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
public class PostBookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private BlogUser user;

    @ManyToOne
    @JoinColumn(nullable=false)
    private Post post;
}
