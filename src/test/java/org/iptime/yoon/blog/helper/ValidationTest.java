package org.iptime.yoon.blog.helper;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import org.junit.jupiter.api.BeforeEach;

/**
 * @author rival
 * @since 2024-02-11
 */
public class ValidationTest {

    public Validator validator;


    @BeforeEach
    public void setUp(){
        try(ValidatorFactory factory = Validation.buildDefaultValidatorFactory()){
            validator = factory.getValidator();
        }
    }


}
