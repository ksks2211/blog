package org.iptime.yoon.blog.repository;


import org.iptime.yoon.blog.entity.BlogUser;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author rival
 * @since 2023-08-11
 */
public interface BlogUserRepository extends JpaRepository<BlogUser,Long> {
}
