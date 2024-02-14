package org.iptime.yoon.blog.common.config;

import io.swagger.v3.core.converter.AnnotatedType;
import io.swagger.v3.core.converter.ModelConverters;
import io.swagger.v3.core.converter.ResolvedSchema;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;

import io.swagger.v3.oas.models.media.Schema;
import org.iptime.yoon.blog.category.dto.CategoryInfoDto;
import org.jetbrains.annotations.NotNull;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author rival
 * @since 2024-02-13
 */

@Configuration
//@OpenAPIDefinition
public class SpringDocConfig {


    @Bean
    public GroupedOpenApi healthCheckApi(){
        return GroupedOpenApi.builder()
            .group("health-check-api").pathsToMatch("/api/health/**")
            .build();
    }

    @Bean
    public GroupedOpenApi userApi(){
        return GroupedOpenApi.builder()
            .group("user-api").pathsToMatch("/api/auth/**")
            .build();
    }


    @Bean
    public OpenAPI openAPI(){

        var root = getStringCategoryInfoDtoHashMap();


        ResolvedSchema resolvedSchema = ModelConverters.getInstance().resolveAsResolvedSchema(new AnnotatedType(CategoryInfoDto.class));




        var categoriesSchema = new Schema<Map<String, CategoryInfoDto>>();

//            .addProperty("< * >",resolvedSchema.schema).example(root);

        categoriesSchema.setType("object");
        categoriesSchema.setAdditionalProperties(resolvedSchema.schema);
        categoriesSchema.setDescription("Root Category");
        categoriesSchema.setExample(root);

        List<String> categories = new ArrayList<>();
        categories.add("/java/spring_boot/mvc");
        categories.add("/java/spring_boot/spring_doc");
        categories.add("/java/spring_boot/actuator");
        categories.add("/javascript/react");


        var categoryListSchema = new Schema<Map<String,List<String>>>()
            .addProperty("categories", new Schema<List<String>>().example(categories));


        return new OpenAPI()
            .components(
                new Components()
                    .addSchemas("CategoryInfoDto", resolvedSchema.schema)

                    .addSchemas("CategoriesSchema", categoriesSchema)
                    .addSchemas("CategoryListSchema",categoryListSchema)
            );
    }

    @NotNull
    private static HashMap<String, CategoryInfoDto> getStringCategoryInfoDtoHashMap() {
        var subCategories = new HashMap<String, CategoryInfoDto>();
        CategoryInfoDto sub1 = new CategoryInfoDto();
        sub1.setNumOfPosts(10);
        sub1.setNumOfAllPosts(10);
        subCategories.put("spring_boot",sub1);

        CategoryInfoDto up1 = new CategoryInfoDto();
        up1.setNumOfPosts(10);
        up1.setNumOfAllPosts(20);
        up1.setSubCategories(subCategories);

        var root = new HashMap<String, CategoryInfoDto>();
        root.put("username",up1);
        return root;
    }

    @Bean
    public GroupedOpenApi categoryApi(){
        return GroupedOpenApi.builder()
            .group("category-api")
            .pathsToMatch("/api/categories/**")
            .build();
    }
}
