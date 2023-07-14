package com.courage.platform.sms.admin.loader;

import com.courage.platform.sms.adapter.command.SmsRequestCommand;
import com.courage.platform.sms.adapter.command.SmsResponseCommand;

/**
 * 适配器处理器接口
 * Created by zhangyong on 2023/5/5.
 */
public interface SmsAdatperProcessor {

    SmsResponseCommand processRequest(SmsRequestCommand requestCommand);

}
