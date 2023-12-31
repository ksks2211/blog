package org.iptime.yoon.blog.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.iptime.yoon.blog.entity.Base;

import java.time.LocalDateTime;

/**
 * @author rival
 * @since 2023-08-17
 */

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@ToString(callSuper = true)
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
