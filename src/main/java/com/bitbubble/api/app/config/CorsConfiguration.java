package com.bitbubble.api.app.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;



@Configuration
public class CorsConfiguration {
    private static final String GET = "GET";
    private static final String POST = "POST";
    private static final String DELETE = "DELETE";
    private static final String PUT = "PUT";
    public WebMvcConfigurer corsConfigurer(){
       
        return new WebMvcConfigurer() {
            // @Override
            private void addCorsMappings(CorsRegistry registry){
               
                registry.addMapping("/**")
                .allowedMethods(GET, PUT, DELETE, POST)
                .allowCredentials(true)
                .allowedHeaders("*")
                .allowedOriginPatterns("*");
            }
        };
    }

     
}