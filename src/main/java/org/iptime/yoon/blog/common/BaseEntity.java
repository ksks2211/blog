package org.iptime.yoon.blog.common;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * @author rival
 * @since 2023-08-10
 */


@MappedSuperclass
@EntityListeners(value= AuditingEntityListener.class)
@Getter
public abstract class BaseEntity {

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column
    private LocalDateTime updatedAt;

    @Column
    private LocalDateTime deletedAt;


    @Column
    private boolean deleted;

    public void softDelete(){
        deleted = true;
        deletedAt = LocalDateTime.now();
    }
}
