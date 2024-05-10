package cn.javayong.platform.sms.admin.api.gateway;

import cn.javayong.platform.sms.admin.dispatcher.processor.requeset.body.SendMessageRequestBody;
import cn.javayong.platform.sms.admin.domain.TSmsAppinfo;
import cn.javayong.platform.sms.admin.service.AppInfoService;
import cn.javayong.platform.sms.admin.service.SmsRecordService;
import cn.javayong.platform.sms.client.util.ResponseCode;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import cn.javayong.platform.sms.client.SmsSenderResult;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

@Controller
@RequestMapping("/gateway/")
public class SenderController {

    private final static Logger logger = LoggerFactory.getLogger(SenderController.class);

    @Autowired
    private AppInfoService appInfoService;

    @Autowired
    private SmsRecordService smsRecordService;

    @Autowired
    private RedisTemplate redisTemplate;

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
            logger.info(" uniqueId: {} , q: {} , appKey: {}", uniqueId, q, appKey);

            // 短信请求参数
            SendMessageRequestBody sendMessageRequestBody = new SendMessageRequestBody();
            JSONObject jsonObject = JSON.parseObject(q);
            sendMessageRequestBody.setTemplateId(jsonObject.getString("templateId"));
            sendMessageRequestBody.setTemplateParam(jsonObject.getString("templateParam"));
            sendMessageRequestBody.setAttime(jsonObject.getString("attime"));

            String mobile = jsonObject.getString("mobile");

            Long counter = redisTemplate.opsForValue().increment("gw" + DateFormatUtils.format(new Date() , "yyyy-MM-dd"));
            if(counter > 20) {
                logger.info("超过限额，不能发送了");
                return new SmsSenderResult(ResponseCode.ERROR.getCode(), "发送失败");
            }

            TSmsAppinfo tSmsAppinfo = appInfoService.getAppinfoByAppKeyFromLocalCache(appKey);
            sendMessageRequestBody.setAppId(tSmsAppinfo.getId());
            sendMessageRequestBody.setMobile(jsonObject.getString("mobile"));

            SmsSenderResult smsSenderResult = smsRecordService.sendMessage(sendMessageRequestBody);
            logger.info(" uniqueId: {} , result:{}", uniqueId, JSON.toJSONString(smsSenderResult));
            return smsSenderResult;
        } catch (Exception e) {
            logger.error("sendSingle error: ", e);
            return new SmsSenderResult(ResponseCode.ERROR.getCode(), "发送失败");
        }
    }

}
