package org.iptime.yoon.blog.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import org.iptime.yoon.blog.user.entity.BlogUser;

import java.util.ArrayList;
import java.util.List;

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
@Where(clause = "deleted = false")
public class Post extends Base{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;

    @Column(nullable = false)
    private String title;


    private String description;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn // nullable = false
    @ToString.Exclude
    private BlogUser writer;
    private String writerName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    @ToString.Exclude
    private Category category;





    // Read-Only
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    @Builder.Default
    @ToString.Exclude
    private List<PostTag> postTags = new ArrayList<>();

}
