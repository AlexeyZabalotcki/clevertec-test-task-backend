package com.example.clevertectesttaskbackend.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Collections;
import java.util.List;

@Configuration
public class Config implements WebMvcConfigurer {


    @Override
    public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
        MappingJackson2HttpMessageConverter xmlConverter = new MappingJackson2HttpMessageConverter();
        xmlConverter.setSupportedMediaTypes(Collections.singletonList(MediaType.APPLICATION_XML));
        converters.add(xmlConverter);
    }
}
