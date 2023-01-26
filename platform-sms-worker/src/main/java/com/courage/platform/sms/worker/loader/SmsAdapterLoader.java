package com.courage.platform.sms.worker.loader;

import com.courage.platform.sms.adapter.core.OuterAdapter;
import com.courage.platform.sms.adapter.core.support.ExtensionLoader;
import com.courage.platform.sms.adapter.core.support.SmsChannelConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmsAdapterLoader {

    private final static Logger logger = LoggerFactory.getLogger(SmsAdapterLoader.class);

    private ExtensionLoader<OuterAdapter> loader;

    public void init() throws Exception {
        loader = ExtensionLoader.getExtensionLoader(OuterAdapter.class);
        OuterAdapter aliyunAdapter = loader.getExtension("emay");
        SmsChannelConfig smsChannelConfig = new SmsChannelConfig();
        aliyunAdapter.init(smsChannelConfig);
        System.out.println(aliyunAdapter);
    }

    public static void main(String[] args) {
        ExtensionLoader<OuterAdapter> loader = ExtensionLoader.getExtensionLoader(OuterAdapter.class);
        OuterAdapter outerAdapter = loader.getExtension("emay");
        System.out.println(outerAdapter);
    }

}
