package com.courage.platform.sms.adapter.logger;

import com.courage.platform.sms.adapter.OuterAdapter;
import com.courage.platform.sms.adapter.support.SPI;
import com.courage.platform.sms.adapter.support.SmsChannelConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@SPI("logger")
public class LoggerAdapter implements OuterAdapter {

    private final static Logger logger = LoggerFactory.getLogger(LoggerAdapter.class);

    @Override
    public void init(SmsChannelConfig smsChannelConfig) {
        logger.info("初始化日志客户端 appKey:" + smsChannelConfig.getChannelAppKey());
    }

    @Override
    public void destroy() {
    }

}
