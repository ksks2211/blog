package org.iptime.yoon.blog.post.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Tag tag;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Post post;

}
