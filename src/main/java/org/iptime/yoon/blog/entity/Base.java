package org.iptime.yoon.blog.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;
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
@Data
public abstract class Base {

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
