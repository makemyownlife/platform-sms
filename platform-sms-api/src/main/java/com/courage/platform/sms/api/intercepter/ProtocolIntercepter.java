package com.courage.platform.sms.api.intercepter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 协议拦截器
 */
@Component
public class ProtocolIntercepter implements HandlerInterceptor {

    private final static Logger logger = LoggerFactory.getLogger(ProtocolIntercepter.class);

    private static final String APP_ID = "appId";

    private static final String RANDOM = "random";

    private static final String SIGN = "sign";

    private static final String TIME = "time";

    private static final String Q = "q";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
    }

}
