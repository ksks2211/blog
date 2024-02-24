package org.iptime.yoon.blog.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.iptime.yoon.blog.common.dto.MsgResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.util.StringUtils;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * @author rival
 * @since 2024-02-20
 */
class JsonUtilTest {


    private JsonUtil jsonUtil;

    @BeforeEach
    public void init() {
        ObjectMapper objectMapper = new ObjectMapper();
        this.jsonUtil = new JsonUtil(objectMapper);
    }

    @Test
    public void testJsonUtil() {

        MsgResponse res = new MsgResponse();
        res.setMessage("Hello World");

        String json = jsonUtil.toJson(res);

        assertNotNull(json);
        assertTrue(StringUtils.hasText(json));
    }

}