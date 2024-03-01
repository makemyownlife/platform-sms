package cn.javayong.platform.sms.admin.common.config;

import cn.javayong.platform.sms.admin.domain.TSmsAppinfo;
import cn.javayong.platform.sms.admin.service.AppInfoService;
import cn.javayong.platform.sms.client.util.ResponseCode;
import com.alibaba.fastjson.JSON;
import cn.javayong.platform.sms.admin.common.utils.UtilsAll;
import cn.javayong.platform.sms.client.SmsSenderResult;
import cn.javayong.platform.sms.client.util.SmsSenderUtil;
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
        try {
            String q = request.getParameter("q");
            String sign = request.getParameter("sign");
            String time = request.getParameter("time");
            String random = request.getParameter("random");
            String appKey = request.getParameter("appKey");
            if (StringUtils.isEmpty(q) || StringUtils.isEmpty(sign) || StringUtils.isEmpty(time) || StringUtils.isEmpty(appKey)) {
                SmsSenderResult smsSenderResult = new SmsSenderResult(ResponseCode.ERROR.getCode(), "参数错误");
                UtilsAll.responseJSONToClient(response, JSON.toJSONString(smsSenderResult));
                return false;
            }
            //查询应用信息
            TSmsAppinfo tSmsAppinfo = appInfoService.getAppinfoByAppKeyFromLocalCache(appKey);
            if (tSmsAppinfo == null) {
                SmsSenderResult smsSenderResult = new SmsSenderResult(ResponseCode.APP_NOT_EXIST.getCode(), "应用不存在");
                UtilsAll.responseJSONToClient(response, JSON.toJSONString(smsSenderResult));
                return false;
            }
            //验证签名开始
            String apiSign = SmsSenderUtil.calculateSignature(tSmsAppinfo.getAppSecret(), random, time, q);
            if (!StringUtils.equals(sign, apiSign)) {
                SmsSenderResult smsSenderResult = new SmsSenderResult(ResponseCode.SIGN_ERROR.getCode(), "签名验证失败");
                UtilsAll.responseJSONToClient(response, JSON.toJSONString(smsSenderResult));
                return false;
            }
        } catch (Exception e) {
            logger.error("preHandle error: ", e);
            SmsSenderResult smsSenderResult = new SmsSenderResult(SmsSenderResult.FAIL_CODE, "请求异常");
            UtilsAll.responseJSONToClient(response, JSON.toJSONString(smsSenderResult));
            return false;
        }
        //验证签名结束
        return true;
    }

}
