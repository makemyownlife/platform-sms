package com.courage.platform.sms.adapter.emay;

import cn.emay.sdk.client.SmsSDKClient;
import cn.emay.sdk.core.dto.sms.common.ResultModel;
import cn.emay.sdk.core.dto.sms.request.SmsSingleRequest;
import cn.emay.sdk.core.dto.sms.response.SmsResponse;
import cn.emay.sdk.util.HttpUtil;
import cn.emay.sdk.util.exception.SDKParamsException;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.courage.platform.sms.adapter.OuterAdapter;
import com.courage.platform.sms.adapter.command.request.AddSmsTemplateCommand;
import com.courage.platform.sms.adapter.command.request.SendSmsCommand;
import com.courage.platform.sms.adapter.command.response.SmsResponseCommand;
import com.courage.platform.sms.adapter.support.SPI;
import com.courage.platform.sms.adapter.support.SmsChannelConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SPI("emay")
public class EmayAdapter implements OuterAdapter {

    private final static Logger logger = LoggerFactory.getLogger(EmayAdapter.class);

    private final static String extendedCode = "01";

    private SmsChannelConfig smsChannelConfig;

    private String instanceId = UUID.randomUUID().toString().replaceAll("-", "");

    private SmsSDKClient client;

    @Override
    public void init(SmsChannelConfig smsChannelConfig) throws SDKParamsException {
        logger.info("初始化亿美短信客户端 渠道编号:[" + smsChannelConfig.getId() + "] appkey:[" + smsChannelConfig.getChannelAppkey() + "] 实例Id:" + instanceId);
        this.smsChannelConfig = smsChannelConfig;
        String channelDomain = smsChannelConfig.getChannelDomain();
        String appId = smsChannelConfig.getChannelAppkey();
        String appSecret = smsChannelConfig.getChannelAppsecret();
        this.client = new SmsSDKClient(channelDomain, -1, appId, appSecret);
    }

    @Override
    public SmsResponseCommand sendSmsByTemplateId(SendSmsCommand smsSendRequest) {
        String mobile = smsSendRequest.getPhoneNumbers();
        String signName = smsSendRequest.getSignName();
        String content = "【张勇的博客】短信内容";
        String customSmsId = UUID.randomUUID().toString().replaceAll("-", "");
        SmsSingleRequest request = new SmsSingleRequest(mobile, content, customSmsId, extendedCode, "");
        ResultModel<SmsResponse> result = client.sendSingleSms(request);
        if ("SUCCESS".equals(result.getCode())) {

        }
        return null;
    }

    @Override
    public SmsResponseCommand addSmsTemplate(AddSmsTemplateCommand addSmsTemplateCommand) {
        // 将标准模版转换成 亿美模版 信息
        String templateContent = addSmsTemplateCommand.getTemplateContent();
        Map<String, String> templateMap = new HashMap<String, String>();
        templateMap.put("templateContent", templateContent);
        templateMap.put("requestTime", String.valueOf(System.currentTimeMillis()));
        templateMap.put("requestValidPeriod", "60");
        ResultModel<HashMap> result = HttpUtil.request(
                smsChannelConfig.getChannelAppkey(),
                smsChannelConfig.getChannelAppsecret(),
                smsChannelConfig.getChannelDomain() + "/inter/createTemplateSMS",
                templateMap,
                HashMap.class);
        logger.info("result:" + result.getResult());
        if ("SUCCESS".equals(result.getCode())) {
            // {"templateId":"168984611698400172"}
            Map<String, String> bodyMap = new HashMap<>();
            bodyMap.put("templateCode", (String) result.getResult().get("templateId"));
            bodyMap.put("templateContent", templateContent);
            // 0 待提交:  1：待审核  2：审核成功 3：审核失败 亿美提交申请默认成功
            bodyMap.put("status", "2");
            return new SmsResponseCommand(SmsResponseCommand.SUCCESS_CODE, bodyMap);
        }
        return new SmsResponseCommand(SmsResponseCommand.FAIL_CODE);
    }

    @Override
    public void destroy() {
        logger.warn("销毁亿美短信客户端 渠道编号:[" + smsChannelConfig.getId() + "] appkey:[" + smsChannelConfig.getChannelAppkey() + "] 实例Id:" + instanceId);
    }

}
