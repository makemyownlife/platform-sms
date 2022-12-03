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
 *
 * @author rewerma 2019-07-13 下午05:12:16
 * @version 1.0.0
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {

            @Override
            public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
                httpServletResponse.setHeader("Access-Control-Allow-Origin", "*");
                httpServletResponse.setHeader("Access-Control-Allow-Methods", "*");
                httpServletResponse.setHeader("Access-Control-Allow-Headers", "Origin, X-Requested-With, Content-Type, Accept, Authorization, X-Token");
                httpServletResponse.setHeader("Access-Control-Allow-Credentials", "true");
                httpServletResponse.setHeader("Access-Control-Max-Age", String.valueOf(3600 * 24));

                if (HttpMethod.OPTIONS.toString().equals(httpServletRequest.getMethod())) {
                    httpServletResponse.setStatus(HttpStatus.NO_CONTENT.value());
                    return false;
                }

                return true;
            }
        }).addPathPatterns("/api/**");

        registry.addInterceptor(new HandlerInterceptor() {

            @Override
            public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
                return true;
            }
        }).addPathPatterns("/api/**").excludePathPatterns("/api/**/config/server_polling").excludePathPatterns("/api/**/config/instances_polling").excludePathPatterns("/api/**/config/instance_polling/**").excludePathPatterns("/api/**/user/login").excludePathPatterns("/api/**/user/logout").excludePathPatterns("/api/**/user/info");
    }
}
