package com.courage.platform.sms.admin.loader;

import com.courage.platform.sms.adapter.support.SmsChannelConfig;
import com.courage.platform.sms.admin.dao.TSmsChannelDAO;
import com.courage.platform.sms.admin.domain.TSmsChannel;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;

@Component(value = "smsAdapterService")
public class SmsAdapterService {

    private final static Logger logger = LoggerFactory.getLogger(SmsAdapterService.class);

    private volatile boolean running = false;

    @Autowired
    private TSmsChannelDAO smsChannelDAO;

    @Autowired
    private SmsAdapterLoader smsAdapterLoader;

    @PostConstruct
    public synchronized void init() {
        long start = System.currentTimeMillis();
        logger.info("开始初始化短信适配器服务");
        //1.加载所有的渠道
        List<TSmsChannel> channelList = smsChannelDAO.queryChannels(MapUtils.EMPTY_MAP);
        for (TSmsChannel tSmsChannel : channelList) {
            SmsChannelConfig channelConfig = new SmsChannelConfig();
            BeanUtils.copyProperties(tSmsChannel, channelConfig);
            smsAdapterLoader.loadAdapter(channelConfig);
        }
        //2.注册命令处理器
        logger.info("结束初始化短信适配器服务, 耗时：" + (System.currentTimeMillis() - start));
    }

    @PreDestroy
    public synchronized void destroy() {
        //1.卸载所有的渠道
        //2.关闭所有的命令处理器
    }

}
