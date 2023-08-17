package org.iptime.yoon.blog.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import org.iptime.yoon.blog.security.entity.BlogUser;

/**
 * @author rival
 * @since 2023-08-10
 */


@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
@Where(clause = "deleted = false")
public class Post extends Base{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String content;


    @ManyToOne
    @JoinColumn // nullable = false
    @ToString.Exclude
    private BlogUser writer;


    private String writerEmail;
}
