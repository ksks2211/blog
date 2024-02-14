package org.iptime.yoon.blog.category.dto;

import jakarta.validation.Validator;
import org.iptime.yoon.blog.helper.ValidationTest;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author rival
 * @since 2024-01-28
 */
class CategoryCreateRequestTest extends ValidationTest {


    @Test
    public void testValidation(){

        var req = new CategoryCreateRequest();
        String category = "/dir/dir";
        req.setCategory(category);

        var falseReq = new CategoryCreateRequest();
        String fCategory = "/";
        falseReq.setCategory(fCategory);

        var tooDeepReq = new CategoryCreateRequest();
        String fCategory2 = "/dir1/dir2/dir3/dir4/dir5/dir6";
        tooDeepReq.setCategory(fCategory2);


        var violations = validator.validate(req);
        var violations2= validator.validate(falseReq);
        var violations3 = validator.validate(tooDeepReq);

        assertTrue(violations.isEmpty());
        assertFalse(violations2.isEmpty());
        assertFalse(violations3.isEmpty());
    }

}