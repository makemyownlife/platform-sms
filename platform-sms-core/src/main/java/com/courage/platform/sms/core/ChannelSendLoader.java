package com.courage.platform.sms.core;


import com.courage.platform.sms.core.domain.SmsTypeEnum;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * 加载短信发送渠道
 */
public class ChannelSendLoader {

    private final static Logger logger = LoggerFactory.getLogger(ChannelSendLoader.class);

    //普通短信通道
    private List<ChannelSend> singleChannelSendList = new ArrayList<ChannelSend>();

    //营销短信通道
    private List<ChannelSend> marketChannelSendList = new ArrayList<ChannelSend>();

    public ChannelSendLoader() {
        init();
    }

    public synchronized void init() {
        try {
            InputStream in = ChannelSendLoader.class.getClassLoader().getResourceAsStream("channels.xml");
            SAXReader saxReader = new SAXReader();
            Document document = saxReader.read(in);
            Element root = document.getRootElement();
            List<Element> channels = root.elements("channel");
            for (Element channel : channels) {
                try {
                    String id = channel.attributeValue("id");
                    String name = channel.attributeValue("name");
                    String className = channel.attributeValue("className");
                    String type = channel.attributeValue("type");
                    Class<?> clazz = Class.forName(className.trim());
                    Constructor<?> c = clazz.getDeclaredConstructor();
                    c.setAccessible(true);

                    ChannelSend channelSend = (ChannelSend) c.newInstance();
                    channelSend.setChannelId(id);
                    channelSend.setChannelName(name);
                    SmsTypeEnum smsTypeEnum = null;
                    if (SmsTypeEnum.SINGLE.getIndex() == Integer.valueOf(type)) {
                        smsTypeEnum = SmsTypeEnum.SINGLE;
                        channelSend.setSmsTypeEnum(smsTypeEnum);
                        singleChannelSendList.add(channelSend);
                    }
                    if (SmsTypeEnum.MARKET.getIndex() == Integer.valueOf(type)) {
                        smsTypeEnum = SmsTypeEnum.MARKET;
                        channelSend.setSmsTypeEnum(smsTypeEnum);
                        marketChannelSendList.add(channelSend);
                    }
                } catch (Exception e) {
                    logger.error("create channelsend error:", e);
                }
            }
        } catch (Exception e) {
            logger.error("channels.xml读取失败:", e);
        }
    }

    public List<ChannelSend> getSingleChannelSendList() {
        return singleChannelSendList;
    }

    public List<ChannelSend> getMarketChannelSendList() {
        return marketChannelSendList;
    }

    public static void main(String[] args) {
        ChannelSendLoader channelSendLoader = new ChannelSendLoader();
    }

}
