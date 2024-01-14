package org.iptime.yoon.blog.user.repository;


import org.iptime.yoon.blog.user.entity.AuthProvider;
import org.iptime.yoon.blog.user.entity.BlogUser;
import org.iptime.yoon.blog.user.repository.projection.BlogUserIdProjection;
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

    Optional<BlogUser> findByProviderAndSubject(AuthProvider provider, String sub);
}
