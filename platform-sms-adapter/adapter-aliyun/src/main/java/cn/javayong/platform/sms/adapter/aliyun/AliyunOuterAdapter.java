package cn.javayong.platform.sms.adapter.aliyun;

import com.alibaba.fastjson.JSON;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.*;
import cn.javayong.platform.sms.adapter.OuterAdapter;
import cn.javayong.platform.sms.adapter.command.req.AddSmsTemplateReqCommand;
import cn.javayong.platform.sms.adapter.command.req.QuerySmsTemplateReqCommand;
import cn.javayong.platform.sms.adapter.command.req.SendSmsReqCommand;
import cn.javayong.platform.sms.adapter.command.resp.SmsRespCommand;
import cn.javayong.platform.sms.adapter.support.SPI;
import cn.javayong.platform.sms.adapter.support.SmsChannelConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 *  参考文档： https://help.aliyun.com/zh/sms/getting-started/use-sms-api
 */
@SPI("aliyun")
public class AliyunOuterAdapter implements OuterAdapter {

    private final static Logger logger = LoggerFactory.getLogger(AliyunOuterAdapter.class);

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
    public SmsRespCommand sendSmsByTemplateId(SendSmsReqCommand sendSmsReqCommand) {
        try {
            com.aliyun.dysmsapi20170525.models.SendSmsRequest sendSmsRequest = new com.aliyun.dysmsapi20170525.models.SendSmsRequest().
                    setSignName(sendSmsReqCommand.getSignName()).
                    setTemplateCode(sendSmsReqCommand.getTemplateCode()).
                    setPhoneNumbers(sendSmsReqCommand.getPhoneNumbers()).
                    setTemplateParam(sendSmsReqCommand.getTemplateParam());
            com.aliyun.teautil.models.RuntimeOptions runtime = new com.aliyun.teautil.models.RuntimeOptions();
            com.aliyun.dysmsapi20170525.models.SendSmsResponse resp = client.sendSmsWithOptions(sendSmsRequest, runtime);
            logger.info("aliyun resp code:{} body:{}", resp.getStatusCode(), JSON.toJSON(resp.getBody()));
            if (SUCCESS_CODE == resp.getStatusCode() && "OK".equals(resp.getBody().getCode())) {
                return new SmsRespCommand(SmsRespCommand.SUCCESS_CODE, resp.getBody().getBizId());
            }
            String erorMsg = resp.getBody().getMessage();
            return new SmsRespCommand(SmsRespCommand.FAIL_CODE, null, erorMsg);
        } catch (Exception e) {
            logger.error("aliyun sendSms:", e);
            return new SmsRespCommand(SmsRespCommand.FAIL_CODE, null, e.getMessage());
        }
    }

    @Override
    public SmsRespCommand addSmsTemplate(AddSmsTemplateReqCommand addSmsTemplateReqCommand) {
        try {
            com.aliyun.dysmsapi20170525.models.AddSmsTemplateRequest addSmsTemplateRequest = new com.aliyun.dysmsapi20170525.models.AddSmsTemplateRequest();
            addSmsTemplateRequest.setRemark(addSmsTemplateReqCommand.getRemark());
            addSmsTemplateRequest.setTemplateContent(addSmsTemplateReqCommand.getTemplateContent());
            addSmsTemplateRequest.setTemplateType(addSmsTemplateReqCommand.getTemplateType());
            addSmsTemplateRequest.setTemplateName(addSmsTemplateReqCommand.getTemplateName());
            AddSmsTemplateResponse resp = client.addSmsTemplate(addSmsTemplateRequest);
            logger.info("resp=" + JSON.toJSONString(resp));
            if (SUCCESS_CODE == resp.getStatusCode()) {
                AddSmsTemplateResponseBody body = resp.getBody();
                Map<String, String> bodyMap = new HashMap<>();
                bodyMap.put("templateCode", body.getTemplateCode());
                bodyMap.put("templateContent", addSmsTemplateReqCommand.getTemplateContent());
                return new SmsRespCommand(SmsRespCommand.SUCCESS_CODE, bodyMap);
            }
            return new SmsRespCommand(SmsRespCommand.FAIL_CODE);
        } catch (Exception e) {
            logger.error("aliyun addSmsTemplate error :", e);
            return new SmsRespCommand(SmsRespCommand.FAIL_CODE, e.getMessage());
        }
    }

    @Override
    public SmsRespCommand<Integer> querySmsTemplateStatus(QuerySmsTemplateReqCommand querySmsTemplateReqCommand) {
        try {
            com.aliyun.dysmsapi20170525.models.QuerySmsTemplateRequest querySmsTemplateRequest = new QuerySmsTemplateRequest();
            querySmsTemplateRequest.setTemplateCode(querySmsTemplateReqCommand.getTemplateCode());
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
                    return new SmsRespCommand(SmsRespCommand.SUCCESS_CODE, 1);
                }
                if (templateStatus == 1) {
                    return new SmsRespCommand(SmsRespCommand.SUCCESS_CODE, 2);
                }
                if (templateStatus == 2) {
                    return new SmsRespCommand(SmsRespCommand.SUCCESS_CODE, 3);
                }
                if (templateStatus == 10) {
                    return new SmsRespCommand(SmsRespCommand.SUCCESS_CODE, 3);
                }
            }
            return new SmsRespCommand(SmsRespCommand.FAIL_CODE);
        } catch (Exception e) {
            logger.error("aliyun querySmsTemplate error :", e);
            return new SmsRespCommand(SmsRespCommand.FAIL_CODE, e.getMessage());
        }
    }

    @Override
    public void destroy() {
        logger.info("销毁阿里云短信客户端渠道编号:[" + smsChannelConfig.getId() + "] appkey:[" + smsChannelConfig.getChannelAppkey() + "] 实例Id:" + instanceId);
    }

}
