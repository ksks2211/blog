package org.iptime.yoon.blog.user.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;
import org.iptime.yoon.blog.common.entity.BaseEntity;

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
public class RefreshToken extends BaseEntity {
    @Id
    @UuidGenerator
    private String id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    @ToString.Exclude
    private BlogUser user;

    private LocalDateTime expiryDate;

    @Transient
    public boolean isExpired() {
        return LocalDateTime.now().isAfter(expiryDate);
    }


}
