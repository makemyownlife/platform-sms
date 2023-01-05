package com.courage.platform.sms.server.config;

import com.courage.platform.sms.server.service.AppInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 协议请求拦截器
 * Created by zhangyong on 2023/1/5.
 */
@Component
public class ApiInterceptor extends HandlerInterceptorAdapter {

    private final static Logger logger = LoggerFactory.getLogger(ApiInterceptor.class);

    @Autowired
    private AppInfoService appInfoService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String q = request.getParameter("q");
        String sign = request.getParameter("sign");
        String time = request.getParameter("time");
        String appKey = request.getParameter("appKey");

        
        return true;
    }

}
