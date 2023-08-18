package com.courage.platform.sms.admin.api.gateway;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.courage.platform.sms.admin.dao.domain.TSmsAppinfo;
import com.courage.platform.sms.admin.loader.SmsAdapterController;
import com.courage.platform.sms.admin.loader.processor.ProcessorRequest;
import com.courage.platform.sms.admin.loader.processor.ProcessorRequestCode;
import com.courage.platform.sms.admin.loader.processor.ProcessorResponse;
import com.courage.platform.sms.admin.loader.processor.body.SendMessageRequestBody;
import com.courage.platform.sms.admin.service.AppInfoService;
import com.courage.platform.sms.client.SmsSenderResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("/gateway/")
public class SenderController {

    private final static Logger logger = LoggerFactory.getLogger(SenderController.class);

    @Autowired
    private SmsAdapterController smsAdapterController;

    @Autowired
    private AppInfoService appInfoService;

    @RequestMapping("/sendByTemplateId")
    @ResponseBody
    public SmsSenderResult sendByTemplateId(HttpServletRequest request) {
        try {
            // 接收请求参数
            String q = request.getParameter("q");
            String appKey = request.getParameter("appKey");
            String time = request.getParameter("time");
            String random = request.getParameter("random");
            // 构造唯一请求 id
            String uniqueId = time + random;
            logger.info("q:" + q + " appKey:" + appKey + " uniqueId:" + uniqueId);
            // 短信请求参数
            SendMessageRequestBody sendMessageRequestBody = new SendMessageRequestBody();
            {
                JSONObject jsonObject = JSON.parseObject(q);
                sendMessageRequestBody.setTemplateId(jsonObject.getString("templateId"));
                sendMessageRequestBody.setTemplateParam(jsonObject.getString("templateParam"));
                TSmsAppinfo tSmsAppinfo = appInfoService.getAppInfoByAppkey(appKey);
                sendMessageRequestBody.setAppId(tSmsAppinfo.getAppKey());
                sendMessageRequestBody.setMobile(jsonObject.getString("mobile"));
            }
            // 处理请求
            ProcessorResponse<SmsSenderResult> processorResponse = smsAdapterController.processRequest(ProcessorRequestCode.SEND_MESSAGE, new ProcessorRequest<SendMessageRequestBody>(sendMessageRequestBody));
            return processorResponse.getData();
        } catch (Exception e) {
            logger.error("sendSingle error: ", e);
            return new SmsSenderResult(SmsSenderResult.FAIL_CODE, "发送失败");
        }
    }

}
