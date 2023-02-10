package com.courage.platform.sms.worker.loader;

import com.courage.platform.sms.adapter.OuterAdapter;
import com.courage.platform.sms.adapter.support.ExtensionLoader;
import com.courage.platform.sms.adapter.support.SmsChannelConfig;
import com.courage.platform.sms.worker.config.SmsAdapterConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

/**
 * 适配器加载器
 */
public class SmsAdapterLoader {

    private final static Logger logger = LoggerFactory.getLogger(SmsAdapterLoader.class);

    private ExtensionLoader<OuterAdapter> extensionLoader;

    private SmsAdapterConfig smsAdapterConfig;

    public SmsAdapterLoader(SmsAdapterConfig smsAdapterConfig) {
        this.smsAdapterConfig = smsAdapterConfig;
    }

    public void init() throws Exception {
        extensionLoader = ExtensionLoader.getExtensionLoader(OuterAdapter.class);
        List<SmsChannelConfig> channelConfigs = smsAdapterConfig.getChannelConfigs();
        for (SmsChannelConfig channelConfig : channelConfigs) {
            loadAdapter(channelConfig.getChannelType(), channelConfig);
        }
    }

    private void loadAdapter(String adapterName, SmsChannelConfig smsChannelConfig) {
        try {
            OuterAdapter adapter = extensionLoader.getExtension(adapterName);
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

    public void destroy() {

    }

}
