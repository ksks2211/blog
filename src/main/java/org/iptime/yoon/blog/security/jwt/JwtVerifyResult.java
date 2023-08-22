package org.iptime.yoon.blog.security.jwt;

import lombok.Builder;
import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * @author rival
 * @since 2023-08-14
 */
@Data
@Builder
public class JwtVerifyResult {
    private boolean verified;
    private boolean decoded;
    private String subject;
    private List<String> authorities;
    private Date expiryDate;

}
