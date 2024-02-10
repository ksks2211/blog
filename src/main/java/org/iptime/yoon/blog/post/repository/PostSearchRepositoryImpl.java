package org.iptime.yoon.blog.post.repository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.iptime.yoon.blog.post.entity.Post;
import org.iptime.yoon.blog.post.repository.projection.PostPreviewDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

/**
 * @author rival
 * @since 2024-01-20
 */
public class PostSearchRepositoryImpl implements PostSearchRepository{

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Page<PostPreviewDto> searchAllPosts(Specification<Post> spec, Pageable pageable) {


        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<PostPreviewDto> query = cb.createQuery(PostPreviewDto.class);

        // From post
        Root<Post> root = query.from(Post.class);


        // Select id, title, writerName,   ...
        query.select(cb.construct(PostPreviewDto.class,root.get("id"), root.get("title"), root.get("writerName"), root.get("writerDisplayName"),root.get("description"), root.get("createdAt"), root.get("updatedAt")));


        // Where ...
        query.where(spec.toPredicate(root,query,cb));


        TypedQuery<PostPreviewDto> typedQuery = entityManager.createQuery(query);


        // limit + offset (paging)
        typedQuery.setFirstResult((int)pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());


        List<PostPreviewDto> content = typedQuery.getResultList();


        long total = getCountQuery(spec).getSingleResult();


        return new PageImpl<>(content, pageable, total);
    }


    private TypedQuery<Long> getCountQuery(Specification<Post> spec){
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Long> countQuery = cb.createQuery(Long.class);

        Root<Post> countRoot = countQuery.from(Post.class);
        countQuery.select(cb.count(countRoot));
        countQuery.where(spec.toPredicate(countRoot, countQuery, cb));

        return entityManager.createQuery(countQuery);
    }
}
