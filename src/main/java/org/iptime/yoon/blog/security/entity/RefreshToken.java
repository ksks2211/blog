package org.iptime.yoon.blog.security.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.iptime.yoon.blog.entity.Base;
import org.iptime.yoon.blog.security.entity.BlogUser;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @author rival
 * @since 2023-08-17
 */

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class RefreshToken extends Base {
    @Id
    @UuidGenerator
    private String id;

    @OneToOne
    @JoinColumn(nullable = false)
    @ToString.Exclude
    private BlogUser user;

    private LocalDateTime expiryDate;

    @Transient
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }

    @Transient
    public String getValue(){
        return id;
    }
}
