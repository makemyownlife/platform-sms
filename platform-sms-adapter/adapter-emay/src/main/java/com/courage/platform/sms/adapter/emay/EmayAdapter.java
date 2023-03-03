package com.courage.platform.sms.adapter.emay;

import com.courage.platform.sms.adapter.OuterAdapter;
import com.courage.platform.sms.adapter.send.SmsSenderAdapterRequest;
import com.courage.platform.sms.adapter.send.SmsSendAdapterResponse;
import com.courage.platform.sms.adapter.support.SPI;
import com.courage.platform.sms.adapter.support.SmsChannelConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SPI("emay")
public class EmayAdapter implements OuterAdapter {

    private final static Logger logger = LoggerFactory.getLogger(EmayAdapter.class);

    @Override
    public void init(SmsChannelConfig smsChannelConfig) {
        logger.info("初始化亿美短信客户端 渠道编号:[" + smsChannelConfig.getId() + "] appkey:[" + smsChannelConfig.getChannelAppKey() + "]") ;
    }

    @Override
    public SmsSendAdapterResponse sendSmsByTemplateId(SmsSenderAdapterRequest smsSendRequest) {
        return null;
    }

    @Override
    public void destroy() {

    }

    public static void main(String[] args) {
        System.out.println(1231);
    }

}
