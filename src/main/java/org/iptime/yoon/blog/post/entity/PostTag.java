package org.iptime.yoon.blog.post.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * @author rival
 * @since 2023-08-30
 */
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor  // JPA needs this
public class PostTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Tag tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Post post;

}
