package org.iptime.yoon.blog.image;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import java.io.Serializable;

/**
 * @author rival
 * @since 2023-08-13
 */

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class ImageDto implements Serializable {

    private Long id;


    private String originalName;


    private byte[] bytes;
    private String contentType;
    private Long size;


    private String createdDate;


}
