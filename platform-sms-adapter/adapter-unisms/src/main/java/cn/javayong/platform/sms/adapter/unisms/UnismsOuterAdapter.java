package cn.javayong.platform.sms.adapter.unisms;

import cn.javayong.platform.sms.adapter.OuterAdapter;
import cn.javayong.platform.sms.adapter.command.req.AddSmsTemplateReqCommand;
import cn.javayong.platform.sms.adapter.command.req.QuerySmsTemplateReqCommand;
import cn.javayong.platform.sms.adapter.command.req.SendSmsReqCommand;
import cn.javayong.platform.sms.adapter.command.resp.SmsRespCommand;
import cn.javayong.platform.sms.adapter.support.SPI;
import cn.javayong.platform.sms.adapter.support.SmsChannelConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.UUID;

@SPI("unisms")
public class UnismsOuterAdapter implements OuterAdapter {

    private final static Logger logger = LoggerFactory.getLogger(UnismsOuterAdapter.class);

    private String instanceId = UUID.randomUUID().toString().replaceAll("-", "");

    private SmsChannelConfig smsChannelConfig;

    @Override
    public void init(SmsChannelConfig smsChannelConfig) {
        logger.info("初始化合一短信客户端 渠道编号:[" + smsChannelConfig.getId() + "] appkey:[" + smsChannelConfig.getChannelAppkey() + "] 实例Id:" + instanceId);
        this.smsChannelConfig = smsChannelConfig;
    }

    @Override
    public SmsRespCommand<String> sendSmsByTemplateId(SendSmsReqCommand sendSmsReqCommand) {
        return null;
    }

    @Override
    public SmsRespCommand<Map<String, String>> addSmsTemplate(AddSmsTemplateReqCommand addSmsTemplateReqCommand) {
        return new SmsRespCommand(SmsRespCommand.NOT_SUPPORT_CODE, "不支持");
    }

    @Override
    public SmsRespCommand<Integer> querySmsTemplateStatus(QuerySmsTemplateReqCommand querySmsTemplateReqCommand) {
        return new SmsRespCommand(SmsRespCommand.NOT_SUPPORT_CODE, "不支持");
    }

    @Override
    public void destroy() {
        logger.warn("销毁合一短信客户端 渠道编号:[" + smsChannelConfig.getId() + "] appkey:[" + smsChannelConfig.getChannelAppkey() + "] 实例Id:" + instanceId);
    }

}
