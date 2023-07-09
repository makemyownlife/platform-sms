package com.courage.platform.sms.admin.loader;

import com.courage.platform.sms.adapter.OuterAdapter;
import com.courage.platform.sms.adapter.support.ExtensionLoader;
import com.courage.platform.sms.adapter.support.SmsChannelConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 适配器加载器
 */
@Component
public class SmsAdapterLoader {

    private final static Logger logger = LoggerFactory.getLogger(SmsAdapterLoader.class);

    private static ExtensionLoader<OuterAdapter> EXTENSION_LOADER = ExtensionLoader.getExtensionLoader(OuterAdapter.class);

    private void loadAdapter(String adapterName, SmsChannelConfig smsChannelConfig) {
        try {
            OuterAdapter adapter = EXTENSION_LOADER.getExtension(adapterName);
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            // 替换ClassLoader
            Thread.currentThread().setContextClassLoader(adapter.getClass().getClassLoader());
            adapter.init(smsChannelConfig);
            Thread.currentThread().setContextClassLoader(cl);
            logger.info("Load sms adapter: {} succeed", adapterName);
        } catch (Exception e) {
            logger.error("Load canal adapter: {} failed", adapterName, e);
        }
    }

}