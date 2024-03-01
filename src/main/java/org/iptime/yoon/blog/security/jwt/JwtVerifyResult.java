package org.iptime.yoon.blog.security.jwt;

import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * @author rival
 * @since 2023-08-14
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtVerifyResult {
    private boolean verified;
    private boolean decoded;
    private String subject;
    private String displayName;
    private Long id;
    private List<String> authorities;
    private Date expiryDate;

    private Long profileImageId;

}
