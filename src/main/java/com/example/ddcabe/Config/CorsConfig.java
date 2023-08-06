package com.example.ddcabe.Config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**") ;// Adjust the mapping path as per your backend API
//                .allowedOriginPatterns("*") // Replace with the actual origin of your frontend application
//                .allowedMethods("*")
//                .allowedHeaders("*")
//                .allowCredentials(true);
    }
}

