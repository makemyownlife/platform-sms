package com.courage.platform.sms.admin.common.config;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
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

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();
        corsConfig.setAllowCredentials(true);
        corsConfig.addAllowedOrigin(CorsConfiguration.ALL);
        corsConfig.addAllowedMethod(CorsConfiguration.ALL);
        corsConfig.addAllowedHeader(CorsConfiguration.ALL);
        //默认可不设置这个暴露的头。这个为了安全问题，不能使用*。设置成*，后面会报错：throw new IllegalArgumentException("'*' is not a valid exposed header value");
        //corsConfig.addExposedHeader("");
        corsConfig.setMaxAge(3600L);

        UrlBasedCorsConfigurationSource configSource = new UrlBasedCorsConfigurationSource();
        configSource.registerCorsConfiguration("/**", corsConfig);

        FilterRegistrationBean<CorsFilter> corsBean = new FilterRegistrationBean<CorsFilter>(new CorsFilter(configSource));
        corsBean.setName("crossOriginFilter");
        corsBean.setOrder(0);//这个顺序也有可能会有影响，尽量设置在拦截器前面
        return corsBean;
    }

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
