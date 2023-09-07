package com.courage.platform.sms.adapter.aliyun;

import com.alibaba.fastjson.JSON;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.*;
import com.courage.platform.sms.adapter.OuterAdapter;
import com.courage.platform.sms.adapter.command.request.AddSmsTemplateCommand;
import com.courage.platform.sms.adapter.command.request.QuerySmsTemplateCommand;
import com.courage.platform.sms.adapter.command.request.SendSmsCommand;
import com.courage.platform.sms.adapter.command.response.SmsResponseCommand;
import com.courage.platform.sms.adapter.support.SPI;
import com.courage.platform.sms.adapter.support.SmsChannelConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SPI("aliyun")
public class AliyunAdapter implements OuterAdapter {

    private final static Logger logger = LoggerFactory.getLogger(AliyunAdapter.class);

    private String instanceId = UUID.randomUUID().toString().replaceAll("-", "");

    private final static int SUCCESS_CODE = 200;

    private SmsChannelConfig smsChannelConfig;

    private Client client;

    @Override
    public void init(SmsChannelConfig smsChannelConfig) throws Exception {
        this.smsChannelConfig = smsChannelConfig;
        logger.info("阿里云短信客户端 渠道编号:[" + smsChannelConfig.getId() + "] appkey:[" + smsChannelConfig.getChannelAppkey() + "] 实例Id:" + instanceId);
        com.aliyun.teaopenapi.models.Config config = new com.aliyun.teaopenapi.models.Config()
                // 必填，您的 AccessKey ID
                .setAccessKeyId(smsChannelConfig.getChannelAppkey())
                // 必填，您的 AccessKey Secret
                .setAccessKeySecret(smsChannelConfig.getChannelAppsecret());
        // 访问的域名
        config.endpoint = "dysmsapi.aliyuncs.com";
        this.client = new com.aliyun.dysmsapi20170525.Client(config);
    }

    @Override
    public SmsResponseCommand sendSmsByTemplateId(SendSmsCommand sendSmsCommand) {
        try {
            com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest().
                    setSignName(sendSmsCommand.getSignName()).
                    setTemplateCode(sendSmsCommand.getTemplateCode()).
                    setPhoneNumbers(sendSmsCommand.getPhoneNumbers()).
                    setTemplateParam(sendSmsCommand.getTemplateParam());
            com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
            com.aliyun.dysmsapi20170525.models.SendSmsResponse resp = client.sendSmsWithOptions(sendSmsRequest, runtime);
            logger.info("aliyun resp code:{} body:{}", resp.getStatusCode(), JSON.toJSON(resp.getBody()));
            if (SUCCESS_CODE == resp.getStatusCode() && "OK".equals(resp.getBody().getCode())) {
                return new SmsResponseCommand(SmsResponseCommand.SUCCESS_CODE, resp.getBody().getBizId());
            }
            return new SmsResponseCommand(SmsResponseCommand.FAIL_CODE);
        } catch (Exception e) {
            logger.error("aliyun sendSms:", e);
            return new SmsResponseCommand(SmsResponseCommand.FAIL_CODE);
        }
    }

    @Override
    public SmsResponseCommand addSmsTemplate(AddSmsTemplateCommand addSmsTemplateCommand) {
        try {
            com.aliyun.dysmsapi20170525.models.AddSmsTemplateRequest addSmsTemplateRequest = new com.aliyun.dysmsapi20170525.models.AddSmsTemplateRequest();
            addSmsTemplateRequest.setRemark(addSmsTemplateCommand.getRemark());
            addSmsTemplateRequest.setTemplateContent(addSmsTemplateCommand.getTemplateContent());
            addSmsTemplateRequest.setTemplateType(addSmsTemplateCommand.getTemplateType());
            addSmsTemplateRequest.setTemplateName(addSmsTemplateCommand.getTemplateName());
            AddSmsTemplateResponse resp = client.addSmsTemplate(addSmsTemplateRequest);
            logger.info("resp=" + JSON.toJSONString(resp));
            if (SUCCESS_CODE == resp.getStatusCode()) {
                AddSmsTemplateResponseBody body = resp.getBody();
                Map<String, String> bodyMap = new HashMap<>();
                bodyMap.put("templateCode", body.getTemplateCode());
                bodyMap.put("templateContent", addSmsTemplateCommand.getTemplateContent());
                return new SmsResponseCommand(SmsResponseCommand.SUCCESS_CODE, bodyMap);
            }
            return new SmsResponseCommand(SmsResponseCommand.FAIL_CODE);
        } catch (Exception e) {
            logger.error("aliyun addSmsTemplate error :", e);
            return new SmsResponseCommand(SmsResponseCommand.FAIL_CODE, e.getMessage());
        }
    }

    @Override
    public SmsResponseCommand<Integer> querySmsTemplateStatus(QuerySmsTemplateCommand querySmsTemplateCommand) {
        try {
            com.aliyun.dysmsapi20170525.models.QuerySmsTemplateRequest querySmsTemplateRequest = new QuerySmsTemplateRequest();
            querySmsTemplateRequest.setTemplateCode(querySmsTemplateCommand.getTemplateCode());
            QuerySmsTemplateResponse resp = client.querySmsTemplate(querySmsTemplateRequest);
            logger.info("resp=" + JSON.toJSONString(resp));
            if (SUCCESS_CODE == resp.getStatusCode() && "OK".equals(resp.getBody().getCode())) {
                QuerySmsTemplateResponseBody body = new QuerySmsTemplateResponseBody();
                Integer templateStatus = body.getTemplateStatus();
                //模板审核状态
                //0：审核中。
                //1：审核通过。
                //2：审核未通过，请在返回参数Reason中查看审核失败原因。
                //10：取消审核。
                if (templateStatus == 0) {
                    return new SmsResponseCommand(SmsResponseCommand.SUCCESS_CODE, 1);
                }
                if (templateStatus == 1) {
                    return new SmsResponseCommand(SmsResponseCommand.SUCCESS_CODE, 2);
                }
                if (templateStatus == 2) {
                    return new SmsResponseCommand(SmsResponseCommand.SUCCESS_CODE, 3);
                }
                if (templateStatus == 10) {
                    return new SmsResponseCommand(SmsResponseCommand.SUCCESS_CODE, 3);
                }
            }
            return new SmsResponseCommand(SmsResponseCommand.FAIL_CODE);
        } catch (Exception e) {
            logger.error("aliyun querySmsTemplate error :", e);
            return new SmsResponseCommand(SmsResponseCommand.FAIL_CODE, e.getMessage());
        }
    }

    @Override
    public void destroy() {
        logger.info("销毁阿里云短信客户端渠道编号:[" + smsChannelConfig.getId() + "] appkey:[" + smsChannelConfig.getChannelAppkey() + "] 实例Id:" + instanceId);
    }

}
