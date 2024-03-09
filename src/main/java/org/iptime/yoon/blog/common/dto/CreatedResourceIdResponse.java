package org.iptime.yoon.blog.common.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author rival
 * @since 2024-01-03
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CreatedResourceIdResponse<T> {
    private T id;
}
