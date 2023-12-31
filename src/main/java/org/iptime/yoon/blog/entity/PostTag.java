package org.iptime.yoon.blog.entity;

import jakarta.persistence.*;
import lombok.*;
import org.iptime.yoon.blog.image.Image;

/**
 * @author rival
 * @since 2023-08-30
 */
@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor  // JPA needs this
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PostTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Tag tag;

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Post post;

    @ToString.Exclude
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn
    private Image profile;

}
