package org.iptime.yoon.blog.repository;

import org.iptime.yoon.blog.entity.ImageMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author rival
 * @since 2023-08-13
 */
public interface ImageMetadataRepository extends JpaRepository<ImageMetadata,Long> {
}
