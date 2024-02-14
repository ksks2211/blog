package org.iptime.yoon.blog.category;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iptime.yoon.blog.category.dto.CategoryCreateRequest;
import org.iptime.yoon.blog.category.dto.CategoryInfoDto;
import org.iptime.yoon.blog.category.exception.CategoryEntityNotFoundException;
import org.iptime.yoon.blog.category.exception.CategoryNotEmptyException;
import org.iptime.yoon.blog.common.dto.ErrorResponse;
import org.iptime.yoon.blog.security.CurrentUsername;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

import static org.iptime.yoon.blog.category.CategoryUtils.parseCategoryString;
import static org.iptime.yoon.blog.common.dto.ErrorResponse.createErrorResponse;

/**
 * @author rival
 * @since 2023-09-14
 */

@RestController
@RequestMapping(value = "/api/categories")
@RequiredArgsConstructor
@Slf4j
public class CategoryController {
    private final CategoryService categoryService;

    @Operation(
        summary = "Get structured Categories of user",
        description = "Get structured Categories of user",
        responses = {
            @ApiResponse(
                responseCode = "200",
                content = @Content(schema = @Schema(ref = "CategoriesSchema"))
            ),
            @ApiResponse(
                responseCode = "401",
                content = @Content(schema = @Schema(implementation = ErrorResponse.class))
            )
        })
    @GetMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(
        responseCode = "401",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public Map<String, CategoryInfoDto> getStructuredCategory(@Parameter(hidden = true) @CurrentUsername String username) {
        return categoryService.getCategories(username);
    }

    @GetMapping(value = "/list", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponse(
        responseCode = "401",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public Map<String, List<String>> getCategory(@Parameter(hidden = true) @CurrentUsername String username) {
        return categoryService.getCategoryList(username);
    }

    @PostMapping(value = "")
    @ApiResponse(responseCode = "201", description = "Create Category", content = @Content)
    @ApiResponse(
        responseCode = "401",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<?> createCategory(@Parameter(hidden = true) @CurrentUsername String username, @Valid @RequestBody CategoryCreateRequest reqBody) {
        String category = reqBody.getCategory();
        categoryService.createCategoryIfNotExists(username, category);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{categoryString}")
    @ApiResponse(responseCode = "200", description = "Category updated", content = @Content)
    @ApiResponse(
        responseCode = "401",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public void updateCategory(@PathVariable String categoryString, @Parameter(hidden = true) @CurrentUsername String username, @Valid @RequestBody CategoryCreateRequest reqBody) {
        String before = parseCategoryString(categoryString);
        String after = reqBody.getCategory();
        categoryService.changeCategory(username, before, after);
    }

    @DeleteMapping("/{categoryString}")
    @ApiResponse(responseCode = "204", description = "Category deleted", content = @Content)
    @ApiResponse(
        responseCode = "401",
        content = @Content(schema = @Schema(implementation = ErrorResponse.class))
    )
    public ResponseEntity<?> deleteCategory(@PathVariable String categoryString, @Parameter(hidden = true) @CurrentUsername String username) throws CategoryNotEmptyException {
        String sub = parseCategoryString(categoryString);
        categoryService.deleteCategoryIfEmpty(username, sub);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(value = CategoryNotEmptyException.class)
    public ResponseEntity<?> categoryNotEmptyExceptionHandler(CategoryNotEmptyException e) {
        log.info(e.getClass().getName());
        log.info(e.getMessage());
        return createErrorResponse(HttpStatus.CONFLICT, e.getMessage());
    }

    @ExceptionHandler(value = CategoryEntityNotFoundException.class)
    public ResponseEntity<?> categoryEntityNotFoundException(CategoryEntityNotFoundException e) {
        log.info(e.getClass().getName());
        log.info(e.getMessage());
        return createErrorResponse(HttpStatus.NOT_FOUND, e.getMessage());
    }

}
