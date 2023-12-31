package org.iptime.yoon.blog.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.iptime.yoon.blog.user.entity.BlogUser;

/**
 * @author rival
 * @since 2023-08-17
 */

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Data
public class Bookmark {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private BlogUser user;

    @ManyToOne
    @JoinColumn(nullable=false)
    private Post post;
}
