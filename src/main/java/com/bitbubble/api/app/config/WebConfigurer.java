package com.bitbubble.api.app.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


import com.bitbubble.api.app.util.Interceptors.VerifyCodeInterceptor;

@Configuration
public class WebConfigurer implements WebMvcConfigurer{

    @Autowired
    VerifyCodeInterceptor verifyCodeInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(verifyCodeInterceptor).addPathPatterns("/auth/**");
    }
}
