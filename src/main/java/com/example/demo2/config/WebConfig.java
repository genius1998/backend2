package com.example.demo2.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // "uploads/" 경로에 대해 HTTP URL 매핑
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:///C:/Users/effor/Downloads/demo2/demo2/uploads/");
    }
}
