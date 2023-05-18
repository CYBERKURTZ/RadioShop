package com.example.radioshop.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

//КОНФИГУРАЦИЯ ДЛЯ ОТОБРАЖЕНИЯ ФОТОГРАФИЙ ИЗ ХРАНИЛИЩА
@Configuration
public class Config implements WebMvcConfigurer {

    @Value("${upload.path}")
    private String uploadPath;

    //ПЕРЕОПРЕДЕЛЕНИЕ МЕТОДА ALT+INS
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**")
                .addResourceLocations("file:///" + uploadPath + "/");
    }
}
