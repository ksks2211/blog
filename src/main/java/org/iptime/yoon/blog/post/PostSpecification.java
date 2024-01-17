package org.iptime.yoon.blog.post;

import jakarta.persistence.criteria.Join;
import org.iptime.yoon.blog.post.entity.Post;
import org.iptime.yoon.blog.post.entity.PostTag;
import org.iptime.yoon.blog.post.entity.Tag;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;

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
                return null; // No condition applied if tags are empty
            }

            Specification<Post> spec = haveTag(tags[0]);
            for (int i = 1; i < tags.length; i++) {
                spec = spec.or(haveTag(tags[i]));
            }
            return spec.toPredicate(root, query, criteriaBuilder);
        };
    }




}
