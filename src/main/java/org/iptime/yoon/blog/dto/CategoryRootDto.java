package org.iptime.yoon.blog.dto;

import lombok.Data;
import lombok.ToString;

import java.util.HashMap;
import java.util.Map;

/**
 * @author rival
 * @since 2023-09-14
 */

@Data
@ToString
public class CategoryRootDto {

    private Map<String,CategoryDto> root;
    private String key;
    public CategoryRootDto(String key){
        this.key=key;
        this.root = new HashMap<>();
        this.root.put(key,CategoryDto.builder().build());
    }

    public void insert(String path, int numOfPosts){

        String[] parts = path.split("/");
        Map<String, CategoryDto> current = this.root;

        for(int i = 0; i< parts.length ; i++){
            CategoryDto newCategory = current.computeIfAbsent(parts[i], (k) -> CategoryDto.builder().build());
            if(i==parts.length -1){
                newCategory.numOfPosts=numOfPosts;
            }
            current = newCategory.getSubCategories();
        }
    }
    
}
