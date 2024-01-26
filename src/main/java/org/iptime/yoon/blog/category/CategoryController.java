package org.iptime.yoon.blog.category;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iptime.yoon.blog.category.dto.CategoryCreateRequest;
import org.iptime.yoon.blog.category.dto.CategoryDto;
import org.iptime.yoon.blog.category.exception.CategoryEntityNotFoundException;
import org.iptime.yoon.blog.category.exception.CategoryNotEmptyException;
import org.iptime.yoon.blog.security.CurrentUsername;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

import static org.iptime.yoon.blog.category.CategoryUtils.parseCategoryString;
import static org.iptime.yoon.blog.common.dto.ErrorResponse.createErrorResponse;

/**
 * @author rival
 * @since 2023-09-14
 */

@RestController
@RequestMapping("/api/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;
    @GetMapping("")
    public Map<String, CategoryDto> getCategories(@CurrentUsername String username) {
        return categoryService.getCategories(username);
    }


    @PostMapping("")
    public ResponseEntity<?> createCategory(@CurrentUsername String username, @Valid @RequestBody CategoryCreateRequest reqBody){
        String category = reqBody.getCategory();
        categoryService.createCategoryIfNotExists(username,category);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{categoryString}")
    public void updateCategory(@PathVariable String categoryString,@CurrentUsername String username,@Valid @RequestBody CategoryCreateRequest reqBody){
        String before = parseCategoryString(categoryString);
        String after = reqBody.getCategory();
        categoryService.changeCategory(username,before,after);
    }

    @DeleteMapping("/{categoryString}")
    public ResponseEntity<?> deleteCategory(@PathVariable String categoryString,@CurrentUsername String root) throws CategoryNotEmptyException {
        String sub = parseCategoryString(categoryString);
        categoryService.deleteCategoryIfEmpty(root, sub);
        return ResponseEntity.noContent().build();
    }


    @ExceptionHandler(value = CategoryNotEmptyException.class)
    public ResponseEntity<?> categoryNotEmptyExceptionHandler(CategoryNotEmptyException e) {
        log.info(e.getClass().getName());
        log.info(e.getMessage());
        return createErrorResponse(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(value= CategoryEntityNotFoundException.class)
    public ResponseEntity<?> categoryEntityNotFoundException(CategoryEntityNotFoundException e){
        log.info(e.getClass().getName());
        log.info(e.getMessage());
        return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

}
