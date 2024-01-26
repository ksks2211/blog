package org.iptime.yoon.blog.common.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;

/**
 * @author rival
 * @since 2024-01-21
 */
@Converter
public class StringListConverter implements AttributeConverter<List<String>,String> {
    private final String SEPARATOR = ",";
    @Override
    public String convertToDatabaseColumn(List<String> attribute) {
        return StringUtils.join(attribute,SEPARATOR);
    }

    @Override
    public List<String> convertToEntityAttribute(String dbData) {

        return List.of(StringUtils.split(dbData,SEPARATOR));
    }
}
