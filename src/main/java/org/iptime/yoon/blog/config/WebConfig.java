package org.iptime.yoon.blog.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author rival
 * @since 2023-09-04
 */
@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

//    @Override
//    public void addCorsMappings(CorsRegistry registry) {
//        long MAX_AGE_SECS = 3600;
//        registry.addMapping("/**") // 경로 지정
//            .allowedOrigins("*") // cors 허용할 origin 설정
//            .allowedMethods("GET","POST","PUT","PATCH","DELETE","OPTIONS") // 허용 메서드
//            .allowedHeaders("*") // 허용 헤더
//            .allowCredentials(true) // credential : true
//            .maxAge(MAX_AGE_SECS); // 허용 시간
//    }
}
