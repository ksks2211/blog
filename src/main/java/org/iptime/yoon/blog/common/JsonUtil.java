package org.iptime.yoon.blog.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author rival
 * @since 2024-02-20
 */

@Component
@RequiredArgsConstructor
public class JsonUtil {
    private final ObjectMapper objectMapper;

    public String toJson(Object object) {
        try {
            return objectMapper.writeValueAsString(object);
        } catch (JsonProcessingException e) {
            // Handle the exception or return a default/fallback string
            return "Error converting object to JSON: " + e.getMessage();
        }
    }
}
