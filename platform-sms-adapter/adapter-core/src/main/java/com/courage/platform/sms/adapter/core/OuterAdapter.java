package com.courage.platform.sms.adapter.core;


import com.courage.platform.sms.adapter.core.support.SPI;
import com.courage.platform.sms.adapter.core.support.SmsChannelConfig;

/**
 * 外部适配器接口
 */
@SPI("logger")
public interface OuterAdapter {

    /**
     * 适配器初始化接口
     */
    void init(SmsChannelConfig configuration);

}
