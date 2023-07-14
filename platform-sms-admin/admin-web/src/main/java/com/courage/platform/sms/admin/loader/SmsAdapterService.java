package com.courage.platform.sms.admin.loader;

import com.courage.platform.sms.adapter.command.SmsRequestCode;
import com.courage.platform.sms.adapter.command.SmsRequestCommand;
import com.courage.platform.sms.adapter.command.SmsResponseCommand;
import com.courage.platform.sms.adapter.support.SmsChannelConfig;
import com.courage.platform.sms.admin.dao.TSmsChannelDAO;
import com.courage.platform.sms.admin.domain.TSmsChannel;
import com.courage.platform.sms.admin.loader.processors.AddTemplateRequestProcessor;
import com.courage.platform.sms.admin.loader.processors.SendMessageRequestProcessor;
import org.apache.commons.collections.MapUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

@Component(value = "smsAdapterService")
public class SmsAdapterService {

    private final static Logger logger = LoggerFactory.getLogger(SmsAdapterService.class);

    private volatile boolean running = false;

    @Autowired
    private TSmsChannelDAO smsChannelDAO;

    @Autowired
    private SmsAdapterLoader smsAdapterLoader;

    @Autowired
    private SendMessageRequestProcessor sendMessageRequestProcessor;

    @Autowired
    private AddTemplateRequestProcessor addTemplateRequestProcessor;

    // 处理器命令映射
    private static final ConcurrentHashMap<Integer, SmsAdatperProcessor> processorMapping = new ConcurrentHashMap<Integer, SmsAdatperProcessor>();

    @PostConstruct
    public synchronized void init() {
        if (running) {
            return;
        }
        long start = System.currentTimeMillis();
        logger.info("开始初始化短信适配器服务");
        //===================================1.加载所有的渠道 =====================================
        List<TSmsChannel> channelList = smsChannelDAO.queryChannels(MapUtils.EMPTY_MAP);
        for (TSmsChannel tSmsChannel : channelList) {
            SmsChannelConfig channelConfig = new SmsChannelConfig();
            BeanUtils.copyProperties(tSmsChannel, channelConfig);
            smsAdapterLoader.loadAdapter(channelConfig);
        }
        //===================================2.注册命令处理器 =====================================
        processorMapping.put(SmsRequestCode.SEND_MESSAGE, sendMessageRequestProcessor);
        processorMapping.put(SmsRequestCode.ADD_TEMPLATE_MESSAGE, addTemplateRequestProcessor);
        logger.info("结束初始化短信适配器服务, 耗时：" + (System.currentTimeMillis() - start));
    }

    // 处理短信网关请求
    public SmsResponseCommand processSmsGatewayRequest(int requestCode, SmsRequestCommand requestCommand) {
        SmsAdatperProcessor smsAdatperProcessor = processorMapping.get(requestCode);
        SmsResponseCommand responseCommand = smsAdatperProcessor.processRequest(requestCommand);
        return responseCommand;
    }

    @PreDestroy
    public synchronized void destroy() {
        if (running) {
            long start = System.currentTimeMillis();
            logger.info("开始销毁短信适配器服务");
            //1.卸载所有的渠道
            //2.关闭所有的命令处理器
            running = false;
            logger.info("结束销毁短信适配器服务, 耗时：" + (System.currentTimeMillis() - start));
        }
    }

}
