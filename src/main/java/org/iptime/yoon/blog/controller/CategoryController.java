package org.iptime.yoon.blog.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iptime.yoon.blog.annotation.CurrentUsername;
import org.iptime.yoon.blog.dto.internal.CategoryDto;
import org.iptime.yoon.blog.dto.req.CategoryCreateReqDto;
import org.iptime.yoon.blog.dto.res.PostPageResDto;
import org.iptime.yoon.blog.exception.CategoryEntityNotFoundException;
import org.iptime.yoon.blog.exception.CategoryNotEmptyException;
import org.iptime.yoon.blog.security.auth.AuthUser;
import org.iptime.yoon.blog.service.CategoryService;
import org.iptime.yoon.blog.service.PostService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

import static org.iptime.yoon.blog.controller.PostController.SIZE_PER_PAGE;
import static org.iptime.yoon.blog.dto.res.ErrorResDto.createErrorResponse;

/**
 * @author rival
 * @since 2023-09-14
 */

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {

    private final CategoryService categoryService;
    private final PostService postService;


    private String parseCategoryString(String categoryString) {
        // validate string logic
        String[] parts = categoryString.split("-");
        return "/" + String.join("/", Arrays.copyOfRange(parts, 1, parts.length));
    }

    @GetMapping("")
    public Map<String, CategoryDto> getCategories(@AuthenticationPrincipal AuthUser authUser) {
        return categoryService.getCategories(authUser.getUsername());
    }


    @PostMapping("")
    public ResponseEntity<?> createCategory(@CurrentUsername String root, @RequestBody CategoryCreateReqDto reqBody){
        String category = reqBody.getCategory();
        categoryService.createCategoryIfNotExists(root,category);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{categoryString}")
    public void updateCategory(@PathVariable String categoryString,@CurrentUsername String root, @RequestBody CategoryCreateReqDto reqBody){
        String before = parseCategoryString(categoryString);
        String after = reqBody.getCategory();
        categoryService.changeCategory(root,before,after);
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

    @GetMapping("/{categoryString}")
    public PostPageResDto getPostPageByCategory(@RequestParam(value = "page", defaultValue = "1") int page, @PathVariable String categoryString, @CurrentUsername String root) {
        int zeroBasedPage = page > 0 ? page - 1 : 0;


        String sub = parseCategoryString(categoryString);

        log.info("Username : {}, Category : {}", root, sub);
        Sort sort = Sort.by("id").descending();
        PageRequest pageRequest = PageRequest.of(zeroBasedPage, SIZE_PER_PAGE, sort);

        return postService.findPostListByCategory(pageRequest, root, sub);
    }

}
