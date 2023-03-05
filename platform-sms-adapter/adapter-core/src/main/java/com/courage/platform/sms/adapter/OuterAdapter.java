package com.courage.platform.sms.adapter;


import com.courage.platform.sms.adapter.send.SmsAdapterRequest;
import com.courage.platform.sms.adapter.send.SmsAdapterResponse;
import com.courage.platform.sms.adapter.support.SPI;
import com.courage.platform.sms.adapter.support.SmsChannelConfig;

/**
 * 外部适配器接口
 */
@SPI("aliyun")
public interface OuterAdapter {

    /**
     * 适配器初始化接口
     */
    void init(SmsChannelConfig smsChannelConfig) throws Exception;

    /**
     * 适配器初始化接口
     */
    SmsAdapterResponse sendSmsByTemplateId(SmsAdapterRequest smsSendRequest);

    /**
     * 外部适配器销毁接口
     */
    void destroy();

}
