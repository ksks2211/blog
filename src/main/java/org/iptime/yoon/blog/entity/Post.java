package org.iptime.yoon.blog.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * @author rival
 * @since 2023-08-10
 */


@Entity
@Table(name="posts")
@Getter
@Setter
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private String title;


    @Column(columnDefinition = "TEXT",nullable = false)
    private String content;

}
