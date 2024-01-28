package org.iptime.yoon.blog.category;

import com.nimbusds.oauth2.sdk.util.CollectionUtils;
import org.iptime.yoon.blog.common.config.JpaConfig;
import org.iptime.yoon.blog.post.repository.PostRepository;
import org.iptime.yoon.blog.user.entity.BlogUser;
import org.iptime.yoon.blog.user.repository.BlogUserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import java.util.List;
import java.util.Optional;

import static org.iptime.yoon.blog.post.repository.PostTestHelper.createPostWithCategory;
import static org.iptime.yoon.blog.user.repository.BlogUserTestHelper.createLocalUser;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author rival
 * @since 2024-01-28
 */

@DataJpaTest
@Import(JpaConfig.class)
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private BlogUserRepository blogUserRepository;

    @Autowired
    private PostRepository postRepository;

    public static Category createCategory(CategoryRepository categoryRepository, String root, String fullName){
        Category category = Category.builder()
            .root(root)
            .fullName(fullName)
            .build();
        return categoryRepository.save(category);
    }

    @Test
    public void createAndFind(){
        // Given
        String root = "example123";
        String path = "example123/dir1/dir2";
        Category category = Category.builder()
            .root(root)
            .fullName(path)
            .build();

        // When
        categoryRepository.save(category);

        // Then
        Optional<Category> optional = categoryRepository.findByFullName(path);
        assertTrue(optional.isPresent());
        optional.ifPresent(cat-> assertEquals(path, cat.getFullName()));
    }


    @Test
    public void findByRoot(){
        // Given
        String root = "example123";
        String pathA = "example123/dir1/dir2";
        String pathB = "example123/dir1/dir3";
        Category category1 = Category.builder()
            .root(root)
            .fullName(pathA)
            .build();
        Category category2 = Category.builder()
            .root(root)
            .fullName(pathB)
            .build();

        // When
        categoryRepository.saveAll(List.of(category1, category2));

        // Then
        List<Category> categories = categoryRepository.findAllByRoot(root);

        assertNotNull(categories);
        assertTrue(CollectionUtils.isNotEmpty(categories));
        assertEquals(2, categories.size());
    }


    @Test
    void findRelatedPostIds() {

        // Given
        String username = "example123";
        String fullName = "example123/dir1/dir2";
        BlogUser user = createLocalUser(blogUserRepository, username);
        String titleA = "Hello World";
        String titleB = "Hi";
        Category category = createCategory(categoryRepository, username, fullName);
        Long idA = createPostWithCategory(postRepository, user, titleA, category).getId();
        Long idB = createPostWithCategory(postRepository, user, titleB, category).getId();

        // When
        List<Long> relatedPostIds = categoryRepository.findRelatedPostIds(fullName);

        // Then
        assertEquals(2, relatedPostIds.size());
        assertTrue(relatedPostIds.contains(idA));
        assertTrue(relatedPostIds.contains(idB));
    }
}