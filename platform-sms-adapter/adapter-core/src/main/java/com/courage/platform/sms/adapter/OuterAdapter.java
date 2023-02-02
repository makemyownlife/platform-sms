package com.courage.platform.sms.adapter;


import com.courage.platform.sms.adapter.support.SPI;
import com.courage.platform.sms.adapter.support.SmsChannelConfig;

/**
 * 外部适配器接口
 */
@SPI("logger")
public interface OuterAdapter {

    /**
     * 适配器初始化接口
     */
    void init(SmsChannelConfig smsChannelConfig) throws Exception;

    /**
     * 外部适配器销毁接口
     */
    void destroy();

}
