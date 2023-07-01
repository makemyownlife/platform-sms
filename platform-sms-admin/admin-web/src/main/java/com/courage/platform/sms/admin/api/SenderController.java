package com.courage.platform.sms.admin.api;

import com.courage.platform.sms.client.SmsSenderResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/api")
public class SenderController {

    private final static Logger logger = LoggerFactory.getLogger(SenderController.class);

    @Value("${rocketmq.topic}")
    private String smsTopic;

    @RequestMapping("/sendByTemplateId")
    @ResponseBody
    public SmsSenderResult sendByTemplateId(HttpServletRequest request) {
        try {
            //接收请求参数
            String q = request.getParameter("q");
            String appKey = request.getParameter("appKey");
            String time = request.getParameter("time");
            String random = request.getParameter("random");
            //构造唯一请求id
            String uniqueId = time + random;
            logger.info("q:" + q + " appKey:" + appKey + " uniqueId:" + uniqueId);
            //发送消息
            String tag = "template";
            return new SmsSenderResult(SmsSenderResult.FAIL_CODE, "发送失败");
        } catch (Exception e) {
            logger.error("sendSingle error: ", e);
            return new SmsSenderResult(SmsSenderResult.FAIL_CODE, "发送失败");
        }
    }

}
