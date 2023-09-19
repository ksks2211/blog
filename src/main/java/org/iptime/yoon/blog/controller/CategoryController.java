package org.iptime.yoon.blog.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.iptime.yoon.blog.dto.CategoryDto;
import org.iptime.yoon.blog.dto.res.PostPageResDto;
import org.iptime.yoon.blog.security.dto.User;
import org.iptime.yoon.blog.service.CategoryService;
import org.iptime.yoon.blog.service.PostService;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Map;

import static org.iptime.yoon.blog.controller.PostController.SIZE_PER_PAGE;

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

    @GetMapping("")
    public Map<String, CategoryDto> getCategories(@AuthenticationPrincipal User user){
        return categoryService.getCategories(user.getUsername());
    }

    @GetMapping("/{categoryString}")
    public PostPageResDto getPostPageByCategory(@RequestParam(value="page", defaultValue = "1")int page,@PathVariable String categoryString, @AuthenticationPrincipal User user){
        int zeroBasedPage = page > 0 ? page - 1 : 0;

        String root = user.getUsername();
        String[] parts = categoryString.split("-");
        String sub = "/" + String.join("/", Arrays.copyOfRange(parts,1,parts.length));

        log.info("Username : {}, Category : {}",root,sub);
        Sort sort = Sort.by("id").descending();
        PageRequest pageRequest = PageRequest.of(zeroBasedPage, SIZE_PER_PAGE,sort);

        return postService.findPostListByCategory(pageRequest,root,sub);
    }

}
