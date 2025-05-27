package com.corp.pixelro.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/*
MVC 설정 파일
- CORS, 정적 자원 매핑, 인터셉터 등록 처리
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

    @Value("${spring.file.upload.path}")
    private String filePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/upload/**")
                .addResourceLocations("file:" + filePath);
    }

}
