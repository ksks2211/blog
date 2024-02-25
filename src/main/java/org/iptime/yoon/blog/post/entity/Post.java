package org.iptime.yoon.blog.post.entity;

import jakarta.persistence.*;
import lombok.*;
//import org.hibernate.annotations.DynamicUpdate;
import org.iptime.yoon.blog.category.Category;
import org.iptime.yoon.blog.common.entity.BaseEntity;
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
@Table(
    indexes = {
        @Index(name="post_writer_index",columnList ="writerName")
    }
)
@EntityListeners(PostEntityListener.class)
//@DynamicUpdate
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn // nullable = false
    private BlogUser writer;

    private String writerName;
    private String writerDisplayName;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn
    private Category category;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private List<PostTag> postTags = new ArrayList<>();

    public void addTag(Tag tag){
        PostTag postTag = PostTag.builder().post(this).tag(tag).build();
        this.postTags.add(postTag);
    }

    public void removeAllPostTags(){
        if(postTags !=null && !postTags.isEmpty()){
            postTags.clear();
        }
    }

}
