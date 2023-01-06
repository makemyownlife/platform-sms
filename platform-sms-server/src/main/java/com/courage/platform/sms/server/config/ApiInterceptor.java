package com.courage.platform.sms.server.config;

import com.alibaba.fastjson.JSON;
import com.courage.platform.sms.client.SmsSenderResult;
import com.courage.platform.sms.client.util.SmsSenderUtil;
import com.courage.platform.sms.domain.TSmsAppinfo;
import com.courage.platform.sms.server.service.AppInfoService;
import com.courage.platform.sms.server.utils.UtilAll;
import org.apache.commons.lang3.StringUtils;
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
        String random = request.getParameter("random");
        String appKey = request.getParameter("appKey");
        if (StringUtils.isEmpty(q) || StringUtils.isEmpty(sign) || StringUtils.isEmpty(time) || StringUtils.isEmpty(appKey)) {
            SmsSenderResult smsSenderResult = new SmsSenderResult(SmsSenderResult.SIGN_CODE, "参数错误");
            UtilAll.responseJSONToClient(response, JSON.toJSONString(smsSenderResult));
            return false;
        }
        //查询应用信息
        TSmsAppinfo tSmsAppinfo = appInfoService.getAppinfoByAppKey("1001");
        if (tSmsAppinfo == null) {
            SmsSenderResult smsSenderResult = new SmsSenderResult(SmsSenderResult.SIGN_CODE, "参数错误");
            UtilAll.responseJSONToClient(response, JSON.toJSONString(smsSenderResult));
            return false;
        }
        //验证签名开始
        String apiSign = SmsSenderUtil.calculateSignature(tSmsAppinfo.getAppSecret(), random, time, q);
        if (!StringUtils.equals(sign, apiSign)) {
            SmsSenderResult smsSenderResult = new SmsSenderResult(SmsSenderResult.SIGN_CODE, "签名验证失败");
            UtilAll.responseJSONToClient(response, JSON.toJSONString(smsSenderResult));
            return false;
        }
        //验证签名结束
        return true;
    }

}
