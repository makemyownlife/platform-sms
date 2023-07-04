package com.courage.platform.sms.adapter.emay;

import cn.emay.ResultModel;
import cn.emay.eucp.inter.framework.dto.TemplateSmsIdAndMobile;
import cn.emay.eucp.inter.http.v1.dto.request.TemplateSmsSendRequest;
import cn.emay.util.AES;
import cn.emay.util.GZIPUtils;
import cn.emay.util.HttpUtil;
import cn.emay.util.JsonHelper;
import cn.emay.util.http.*;
import com.courage.platform.sms.adapter.OuterAdapter;
import com.courage.platform.sms.adapter.command.AddSmsTemplateCommand;
import com.courage.platform.sms.adapter.command.SendSmsCommand;
import com.courage.platform.sms.adapter.command.SmsResponseCommand;
import com.courage.platform.sms.adapter.support.SPI;
import com.courage.platform.sms.adapter.support.SmsChannelConfig;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

@SPI("emay")
public class EmayAdapter implements OuterAdapter {

    private final static Logger logger = LoggerFactory.getLogger(EmayAdapter.class);

    private final static String algorithm = "AES/ECB/PKCS5Padding";

    private final static boolean isGizp = true;

    private final static String encode = "UTF-8";

    private final static String extendCode = "111";

    private SmsChannelConfig smsChannelConfig;

    @Override
    public void init(SmsChannelConfig smsChannelConfig) {
        logger.info("初始化亿美短信客户端 渠道编号:[" + smsChannelConfig.getId() + "] appkey:[" + smsChannelConfig.getChannelAppKey() + "]");
        this.smsChannelConfig = smsChannelConfig;
    }

    @Override
    public SmsResponseCommand sendSmsByTemplateId(SendSmsCommand smsSendRequest) {
        TemplateSmsSendRequest pamars = new TemplateSmsSendRequest();
        String[] mobiles = StringUtils.split(smsSendRequest.getPhoneNumbers(), ",");
        List<TemplateSmsIdAndMobile> smsIdAndMobilesList = new ArrayList(mobiles.length);
        for (int i = 0; i < mobiles.length; i++) {
            TemplateSmsIdAndMobile templateSmsIdAndMobile = new TemplateSmsIdAndMobile(mobiles[i], UUID.randomUUID().toString().replaceAll("-", ""));
            smsIdAndMobilesList.add(templateSmsIdAndMobile);
        }
        pamars.setTemplateId(smsSendRequest.getTemplateCode());
        pamars.setExtendedCode(extendCode);
        pamars.setRequestTime(System.currentTimeMillis());
        pamars.setTimerTime(smsSendRequest.getTimerTime());
        logger.info(JsonHelper.toJsonString(pamars));
        ResultModel result = HttpUtil.request(
                smsChannelConfig.getChannelAppKey(),
                smsChannelConfig.getChannelAppSecret(),
                algorithm,
                pamars,
                smsChannelConfig.getChannelDomain() + "/inter/sendTemplateNormalSMS",
                isGizp,
                encode);
        if ("SUCCESS".equals(result.getCode())) {
            cn.emay.eucp.inter.http.v1.dto.response.SmsResponse[] response = JsonHelper.fromJson(cn.emay.eucp.inter.http.v1.dto.response.SmsResponse[].class, result.getResult());
            if (response != null) {
                for (cn.emay.eucp.inter.http.v1.dto.response.SmsResponse d : response) {
                    logger.info("data:" + d.getMobile() + "," + d.getSmsId() + "," + d.getCustomSmsId());
                }
                return new SmsResponseCommand(SmsResponseCommand.SUCCESS_CODE, result.getResult());
            }
        }
        return new SmsResponseCommand(SmsResponseCommand.FAIL_CODE, result.getResult());
    }

    @Override
    public SmsResponseCommand addSmsTemplate(AddSmsTemplateCommand addSmsTemplateCommand) {
        return null;
    }



    @Override
    public void destroy() {
    }

}
