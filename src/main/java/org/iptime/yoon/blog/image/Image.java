package org.iptime.yoon.blog.image;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import org.iptime.yoon.blog.entity.Base;
import org.iptime.yoon.blog.user.entity.BlogUser;

/**
 * @author rival
 * @since 2023-08-13
 */

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString(callSuper = true)
@Where(clause = "deleted = false")
public class Image extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable=false,unique = true)
    private String filename;

    @Column
    private String contentType;
    private Long size;
    private String originalName;


    @ManyToOne
    @JoinColumn
    @ToString.Exclude
    private BlogUser owner;

}
