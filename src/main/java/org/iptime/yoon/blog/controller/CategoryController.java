package org.iptime.yoon.blog.controller;

import lombok.RequiredArgsConstructor;
import org.iptime.yoon.blog.dto.CategoryDto;
import org.iptime.yoon.blog.security.dto.User;
import org.iptime.yoon.blog.service.CategoryService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

/**
 * @author rival
 * @since 2023-09-14
 */

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("")
    public Map<String, CategoryDto> getCategories(@AuthenticationPrincipal User user){
        return categoryService.getCategories(user.getUsername());
    }
}
