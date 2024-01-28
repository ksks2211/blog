package org.iptime.yoon.blog.category.dto;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author rival
 * @since 2024-01-28
 */
class CategoryCreateRequestTest {

    private Validator validator;



    @BeforeEach
    public void setUp(){
        try(ValidatorFactory factory = Validation.buildDefaultValidatorFactory()){
            validator = factory.getValidator();
        }
    }


    @Test
    public void testValidation(){
        var req = new CategoryCreateRequest();
        String category = "/dir/dir";
        req.setCategory(category);

        var falseReq = new CategoryCreateRequest();
        String fCategory = "/";
        falseReq.setCategory(fCategory);


        var violations = validator.validate(req);
        var violations2= validator.validate(falseReq);

        assertTrue(violations.isEmpty());
        assertFalse(violations2.isEmpty());


    }




}