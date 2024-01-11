package org.iptime.yoon.blog.category;

import jakarta.persistence.*;
import lombok.*;

/**
 * @author rival
 * @since 2023-09-01
 */

@Entity
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
@Table(indexes = @Index(name = "index_category_root", columnList = "root"))
@ToString
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    private Long id;


    private String root;

    @Column(unique = true)
    @Setter
    private String fullName;

    @Builder.Default
    private Integer postCount = 0;

    public void increasePostCount(){
        this.postCount++;
    }

    public void decreasePostCount(){
        this.postCount--;
    }
}
