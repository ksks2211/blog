package org.iptime.yoon.blog.category.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author rival
 * @since 2023-09-14
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryRootDto {

    private Map<String, CategoryInfoDto> root;
    private String key;
    public CategoryRootDto(String key){
        this.key=key;
        this.root = new HashMap<>();
        this.root.put(key, CategoryInfoDto.builder().build());
    }

    public void insert(String path, int numOfPosts){

        String[] parts = path.split("/");
        Map<String, CategoryInfoDto> current = this.root;

        for(int i = 0; i< parts.length ; i++){
            CategoryInfoDto newCategory = current.computeIfAbsent(parts[i], (k) -> CategoryInfoDto.builder().build());
            if(i==parts.length -1){
                newCategory.numOfPosts=numOfPosts;
            }
            newCategory.numOfAllPosts+=numOfPosts;
            current = newCategory.getSubCategories();
        }
    }
    
}
