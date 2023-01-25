package com.courage.platform.sms.worker.loader;

import com.courage.platform.sms.adapter.core.OuterAdapter;
import com.courage.platform.sms.adapter.core.support.ExtensionLoader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class SmsAdapterLoader {

    private final static Logger logger = LoggerFactory.getLogger(SmsAdapterLoader.class);

    private ExtensionLoader<OuterAdapter> loader;

    public void init() {
        loader = ExtensionLoader.getExtensionLoader(OuterAdapter.class);
    }

}
