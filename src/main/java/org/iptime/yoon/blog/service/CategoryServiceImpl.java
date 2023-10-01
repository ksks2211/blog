package org.iptime.yoon.blog.service;

import lombok.RequiredArgsConstructor;
import org.iptime.yoon.blog.dto.CategoryDto;
import org.iptime.yoon.blog.dto.CategoryRootDto;
import org.iptime.yoon.blog.entity.Category;
import org.iptime.yoon.blog.exception.CategoryEntityNotFoundException;
import org.iptime.yoon.blog.exception.CategoryNotEmptyException;
import org.iptime.yoon.blog.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * @author rival
 * @since 2023-09-01
 */
@Service
@RequiredArgsConstructor
@Transactional
public class CategoryServiceImpl implements CategoryService{

    private final CategoryRepository categoryRepository;

    @Override
    public void increasePostCount(Category category){
        category.increasePostCount();
        categoryRepository.save(category);
    }

    @Override
    public void decreasePostCount(Category category) {
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
    public Long createCategoryIfNotExists(String root, String sub){
        Category category = getCategory(root,sub);
        if(category.getId()==null)categoryRepository.save(category);
        return category.getId();
    }



    // Delete
    @Override
    public void deleteCategoryIfEmpty(String root, String sub) throws CategoryNotEmptyException{
        String fullName = root+sub;
        Category category = categoryRepository.findByFullName(fullName).orElseThrow(() -> new CategoryEntityNotFoundException(fullName));
        if(category.getPostCount() == 0){
            categoryRepository.delete(category);
        }else{
            throw new CategoryNotEmptyException(fullName);
        }
    }


    // Read
    @Override
    public Map<String, CategoryDto> getCategories(String root) {
        List<Category> categories = categoryRepository.findAllByRoot(root);
        CategoryRootDto categoryRoot = new CategoryRootDto(root);
        categories.forEach(el-> categoryRoot.insert(el.getFullName(),el.getPostCount()));
        return categoryRoot.getRoot();
    }


    // Update
    @Override
    public void changeCategory(String root, String beforeSub, String afterSub) {
        String fullName = root+beforeSub;
        Category category = categoryRepository.findByFullName(fullName).orElseThrow(() -> new CategoryEntityNotFoundException(fullName));
        String newFullName = root+afterSub;
        category.setFullName(newFullName);
        categoryRepository.save(category);
    }


}
