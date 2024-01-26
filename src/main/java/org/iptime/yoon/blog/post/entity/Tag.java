package org.iptime.yoon.blog.post.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author rival
 * @since 2023-08-30
 */

@Entity
@Getter
@NoArgsConstructor  // JPA needs this
public class Tag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "tag_value",unique = true)
    @Setter
    private String value;


    public Tag(String value){
        this.value = value;
    }
}