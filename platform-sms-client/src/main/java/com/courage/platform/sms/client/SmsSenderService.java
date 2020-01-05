package com.courage.platform.sms.client;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * 短信发送服务类
 * Created by zhangyong on 2020/1/5.
 */
public class SmsSenderService {

    private final static Logger logger = LoggerFactory.getLogger(SmsSenderService.class);

    private SmsConfig smsConfig;

    public SmsSenderService(SmsConfig smsConfig) {
        this.smsConfig = smsConfig;
    }

    public static SmsSenderResult sendSingle(String mobile, String content) throws IOException {
        return null;
    }

    public static SmsSenderResult sendMultiple(String[] mobiles, String content) throws IOException {
        if (StringUtils.isEmpty(content) || mobiles == null || mobiles.length == 0) {
            throw new RuntimeException("mobile or content is null , please check param ");
        }
        //群发短信必须小于200
        if (mobiles.length > 200) {
            throw new RuntimeException("mobile length should be less than 200!");
        }
        return null;
    }

    public static SmsSenderResult sendMarket(String[] mobiles, String content) throws IOException {
        return null;
    }

}
