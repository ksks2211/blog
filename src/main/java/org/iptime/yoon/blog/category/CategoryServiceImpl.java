package org.iptime.yoon.blog.category;

import lombok.RequiredArgsConstructor;
import org.iptime.yoon.blog.cache.CacheService;
import org.iptime.yoon.blog.category.dto.CategoryInfoDto;
import org.iptime.yoon.blog.category.dto.CategoryRootDto;
import org.iptime.yoon.blog.category.exception.CategoryEntityNotFoundException;
import org.iptime.yoon.blog.category.exception.CategoryNotEmptyException;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author rival
 * @since 2023-09-01
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    private final CacheService cacheService;

    @Override
    public void increasePostCount(Category category){
        category.increasePostCount();
        categoryRepository.save(category);
    }

    @Override
    public void decreasePostCount(Category category){
        category.decreasePostCount();
        categoryRepository.save(category);
    }

    // Read
    @Override
    public Category getCategory(String root, String sub){
        String fullName = root + sub;
        return categoryRepository.findByFullName(fullName)
            .orElseGet(() -> Category.builder()
                .fullName(fullName)
                .root(root)
                .build());
    }

    // Create + Read
    @Override
    @Transactional
    public void createCategoryIfNotExists(String root, String sub){
        Category category = getCategory(root,sub);
        if(category.getId()==null)categoryRepository.save(category);
    }



    // Delete
    @Override
    @Transactional
    public void deleteCategoryIfEmpty(String root, String sub) throws CategoryNotEmptyException{
        String fullName = root+sub;
        Category category = categoryRepository.findByFullName(fullName).orElseThrow(() -> new CategoryEntityNotFoundException(fullName));
        if(category.getPostCount() == 0){
            categoryRepository.delete(category);
            cacheService.deleteCaches("categories:list", List.of(root));
            cacheService.deleteCaches("categories",List.of(root));
        }else{
            throw new CategoryNotEmptyException(fullName);
        }
    }


    // Read
    @Cacheable(value="categories",key="#username")
    @Override
    @Transactional
    public Map<String, CategoryInfoDto> getCategories(String username) {
        List<Category> categories = categoryRepository.findAllByRoot(username);
        CategoryRootDto categoryRoot = new CategoryRootDto(username);
        categories.forEach(el-> categoryRoot.insert(el.getFullName(),el.getPostCount()));
        return categoryRoot.getRoot();
    }


    @Cacheable(value="categories:list",key = "#username")
    @Override
    @Transactional
    public Map<String, List<String>> getCategoryList(String username) {
        var categories = categoryRepository.findAllByRoot(username);
        var categoryList = categories.stream().map(category -> {
            String fullName = category.getFullName();
            int i = fullName.indexOf("/");
            return fullName.substring(i);
        }).collect(Collectors.toCollection(ArrayList::new));

        var map = new HashMap<String, List<String>>();
        map.put("categories",categoryList);
        return map;
    }


    // Update
    @Override
    @Transactional
    public void changeCategory(String username, String beforeSub, String afterSub) {
        String fullName = username+beforeSub;

        List<Long> relatedPostsIdList = categoryRepository.findRelatedPostIds(fullName);
        Category category = categoryRepository.findByFullName(fullName).orElseThrow(() -> new CategoryEntityNotFoundException(fullName));

        String newFullName = username+afterSub;
        category.setFullName(newFullName);
        categoryRepository.save(category);

        // Cache Clear
        cacheService.deleteCaches("posts",relatedPostsIdList);
        cacheService.deleteCaches("categories:list", List.of(username));
        cacheService.deleteCaches("categories",List.of(username));
    }


}
