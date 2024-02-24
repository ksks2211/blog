package org.iptime.yoon.blog.post;

import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import jakarta.persistence.criteria.Subquery;
import org.iptime.yoon.blog.post.dto.PostSearchQuery;
import org.iptime.yoon.blog.post.entity.Post;
import org.iptime.yoon.blog.post.entity.PostTag;
import org.iptime.yoon.blog.post.entity.Tag;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author rival
 * @since 2024-01-15
 */
public class PostSpecification {

    public static Specification<Post> wroteBy(String writerName){
        return (root,query,criteriaBuilder)-> {
            if(!StringUtils.hasText(writerName)){
                return null;
            }
            return criteriaBuilder.equal(root.get("writerName"), writerName);
        };
    }

    public static Specification<Post> haveTag(String tagValue){

        return (root,query,criteriaBuilder)->{

            if(!StringUtils.hasText(tagValue)){
                return null;
            }
            Join<Post, PostTag>  postJoin = root.join("postTags");
            Join<PostTag, Tag> tagJoin = postJoin.join("tag");
            return criteriaBuilder.equal(
                criteriaBuilder.lower(tagJoin.get("value"))
                ,tagValue.toLowerCase()
            );
        };
    }

    public static Specification<Post> haveAtLeastOneTag(String[] tags){

        return (root, query, criteriaBuilder) -> {
            if (tags == null || tags.length == 0) {
                return criteriaBuilder.conjunction(); // No-op condition.
            }

            // Convert tags to lowercase
            List<String> lowerCaseTags = Arrays.stream(tags)
                .map(String::toLowerCase)
                .collect(Collectors.toList());

            Join<Post, PostTag> postTagJoin = root.join("postTags");
            Join<PostTag, Tag> tagJoin = postTagJoin.join("tag");

            // Use criteriaBuilder.lower() to ensure the database values are compared in lowercase
            return tagJoin.get("value").in(lowerCaseTags);
        };
    }


    public static Specification<Post> haveAllTags(String[] tags){

        return (root, query, criteriaBuilder) -> {
            if (tags == null || tags.length == 0) {
                return criteriaBuilder.conjunction(); // No-op condition.
            }

            // Convert tags to lowercase
            List<String> lowerCaseTags = Arrays.stream(tags)
                .map(String::toLowerCase)
                .collect(Collectors.toList());

            // Subquery to count the matching tags for each post
            Subquery<Long> subquery = query.subquery(Long.class);
            Root<PostTag> subqueryRoot = subquery.from(PostTag.class);
            Join<PostTag, Tag> tagJoin = subqueryRoot.join("tag");

            // Create a predicate for the subquery to match the tags
            Predicate tagsMatch = tagJoin.get("value").in(lowerCaseTags);
            Predicate postMatch = criteriaBuilder.equal(subqueryRoot.get("post"), root); // Ensures we're counting tags for this post

            subquery.select(criteriaBuilder.countDistinct(tagJoin.get("value")))
                .where(criteriaBuilder.and(tagsMatch, postMatch));

            // Ensure the count of distinct matching tags is equal to the number of tags we're looking for
            return criteriaBuilder.equal(subquery, (long) tags.length);
        };
    }


    public static Specification<Post> getPostSpecification(PostSearchQuery postSearchQuery) {

        Specification<Post> spec = Specification.where(null);
        if (postSearchQuery.getWriter() != null) {
            spec = spec.and(wroteBy(postSearchQuery.getWriter()));
        }

        List<String> tags = postSearchQuery.getTags();
        if (tags != null && !tags.isEmpty()) {
            if(postSearchQuery.isAllTags()){
                spec = spec.and(haveAllTags(tags.toArray(new String[0])));
            }else {
                spec = spec.and(haveAtLeastOneTag(tags.toArray(new String[0])));
            }
        }
        return spec;
    }
}
