package org.iptime.yoon.blog.security.repository;


import org.iptime.yoon.blog.security.entity.BlogUser;
import org.iptime.yoon.blog.security.repository.projection.BlogUserIdProjection;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author rival
 * @since 2023-08-11
 */
public interface BlogUserRepository extends JpaRepository<BlogUser,Long> {
    Optional<BlogUser> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<BlogUserIdProjection> findIdByUsername(String username);
}
