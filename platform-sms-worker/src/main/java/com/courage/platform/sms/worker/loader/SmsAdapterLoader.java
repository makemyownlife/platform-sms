package com.courage.platform.sms.worker.loader;

import com.courage.platform.sms.adapter.OuterAdapter;
import com.courage.platform.sms.adapter.support.ExtensionLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *  适配器加载器
 */
public class SmsAdapterLoader {

    private final static Logger logger = LoggerFactory.getLogger(SmsAdapterLoader.class);

    private ExtensionLoader<OuterAdapter> loader;

    public void init() throws Exception {
        this.loader = ExtensionLoader.getExtensionLoader(OuterAdapter.class);
    }

}
