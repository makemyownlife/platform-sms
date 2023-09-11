package com.courage.platform.sms.admin.dispatcher;

import com.courage.platform.sms.adapter.OuterAdapter;
import com.courage.platform.sms.adapter.support.ExtensionLoader;
import com.courage.platform.sms.adapter.support.SmsChannelConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;

/**
 * 适配器加载器
 */
@Component
public class AdapterLoader {

    private final static Logger logger = LoggerFactory.getLogger(AdapterLoader.class);

    private static ExtensionLoader<OuterAdapter> EXTENSION_LOADER = ExtensionLoader.getExtensionLoader(OuterAdapter.class);

    private static ConcurrentHashMap<Integer, OuterAdapter> ADAPTER_MAP = new ConcurrentHashMap<>(16);

    public void loadAdapter(SmsChannelConfig smsChannelConfig) {
        String adapterName = smsChannelConfig.getChannelType();
        try {
            OuterAdapter adapter = EXTENSION_LOADER.getExtension(adapterName);
            ClassLoader cl = Thread.currentThread().getContextClassLoader();
            // 替换 ClassLoader
            Thread.currentThread().setContextClassLoader(adapter.getClass().getClassLoader());
            adapter.init(smsChannelConfig);
            Thread.currentThread().setContextClassLoader(cl);
            // 卸载之前的适配器
            OuterAdapter preAdapter = ADAPTER_MAP.get(smsChannelConfig.getId());
            if (preAdapter != null) {
                preAdapter.destroy();
            }
            // 加入到容器里
            ADAPTER_MAP.put(smsChannelConfig.getId(), adapter);
            logger.info("Load sms adapter: {} succeed", adapterName);
        } catch (Exception e) {
            logger.error("Load sms adapter: {} failed", adapterName, e);
        }
    }

    public OuterAdapter getAdapterByChannelId(Integer id) {
        return ADAPTER_MAP.get(id);
    }

}