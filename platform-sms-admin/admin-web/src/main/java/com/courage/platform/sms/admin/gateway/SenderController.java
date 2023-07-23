package com.courage.platform.sms.admin.gateway;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.courage.platform.sms.admin.loader.SmsAdapterService;
import com.courage.platform.sms.admin.loader.processors.ProcessorRequest;
import com.courage.platform.sms.admin.loader.processors.ProcessorRequestCode;
import com.courage.platform.sms.admin.loader.processors.ProcessorResponse;
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
    private SmsAdapterService smsAdapterService;

    @RequestMapping("/sendByTemplateId")
    @ResponseBody
    public SmsSenderResult sendByTemplateId(HttpServletRequest request) {
        try {
            // 接收请求参数
            String q = request.getParameter("q");
            String appKey = request.getParameter("appKey");
            String time = request.getParameter("time");
            String random = request.getParameter("random");
            //构造唯一请求 id
            String uniqueId = time + random;
            logger.info("q:" + q + " appKey:" + appKey + " uniqueId:" + uniqueId);

            JSONObject jsonObject = JSON.parseObject(q);

            // 短信请求参数
            Map<String, String> data = new HashMap<>();
            data.put("mobile", jsonObject.getString("mobile"));
            data.put("templateId", jsonObject.getString("templateId"));
            data.put("appKey", appKey);
            data.put("templateParam", jsonObject.getString("templateParam"));

            ProcessorResponse processorResponse = smsAdapterService.processRequest(ProcessorRequestCode.SEND_MESSAGE, new ProcessorRequest(data));
            return new SmsSenderResult(SmsSenderResult.FAIL_CODE, "发送失败");
        } catch (Exception e) {
            logger.error("sendSingle error: ", e);
            return new SmsSenderResult(SmsSenderResult.FAIL_CODE, "发送失败");
        }
    }

}
