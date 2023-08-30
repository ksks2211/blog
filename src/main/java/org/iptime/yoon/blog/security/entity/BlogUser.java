package org.iptime.yoon.blog.security.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.Where;
import org.iptime.yoon.blog.entity.Base;

/**
 * @author rival
 * @since 2023-08-11
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
public class BlogUser extends Base {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    private String password;

    private String email;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private BlogRole role = BlogRole.USER;
}
