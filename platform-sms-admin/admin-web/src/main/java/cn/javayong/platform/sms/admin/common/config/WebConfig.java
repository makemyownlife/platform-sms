package cn.javayong.platform.sms.admin.common.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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

    @Autowired
    private ApiInterceptor apiInterceptor;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

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
        // api网关
        registry.addInterceptor(apiInterceptor).addPathPatterns("/gateway/**");
        
        // 控制台接口
        registry.addInterceptor(new HandlerInterceptor() {

            @Override
            public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
                String token = httpServletRequest.getHeader("X-Token");
                if (StringUtils.isEmpty(token)) {
                    return false;
                }
                String userInfoStr = (String) redisTemplate.opsForValue().get(RedisKeyConstants.LOGIN_USER + token);
                if (StringUtils.isEmpty(userInfoStr)) {
                    UtilsAll.responseJSONToClient(
                            httpServletResponse,
                            JSON.toJSONString(ResponseEntity.build(50014, "Invalid token"))
                    );
                    return false;
                }
                return true;
            }
        }).addPathPatterns("/api/**").
                excludePathPatterns("/api/**/user/login")
                .excludePathPatterns("/api/**/user/logout")
                .excludePathPatterns("/api/**/user/info");
    }

}
