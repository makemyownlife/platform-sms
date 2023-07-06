package com.courage.platform.sms.admin.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 相关MVC拦截器配置
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {

            @Override
            public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
                return true;
            }
        }).addPathPatterns("/api/**").excludePathPatterns("/api/**/config/server_polling").excludePathPatterns("/api/**/config/instances_polling").excludePathPatterns("/api/**/config/instance_polling/**").excludePathPatterns("/api/**/user/login").excludePathPatterns("/api/**/user/logout").excludePathPatterns("/api/**/user/info");
   
    }
}
