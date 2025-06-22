package cn.javayong.platform.sms.adapter.emay;

import cn.emay.sdk.client.SmsSDKClient;
import cn.emay.sdk.core.dto.sms.common.ResultModel;
import cn.emay.sdk.core.dto.sms.request.SmsSingleRequest;
import cn.emay.sdk.core.dto.sms.response.SmsResponse;
import cn.emay.sdk.util.HttpUtil;
import cn.emay.sdk.util.exception.SDKParamsException;
import cn.javayong.platform.sms.adapter.support.SmsChannelConfig;
import cn.javayong.platform.sms.adapter.support.SmsTemplateUtil;
import com.alibaba.fastjson.JSON;
import cn.javayong.platform.sms.adapter.OuterAdapter;
import cn.javayong.platform.sms.adapter.command.request.AddSmsTemplateCommand;
import cn.javayong.platform.sms.adapter.command.request.QuerySmsTemplateCommand;
import cn.javayong.platform.sms.adapter.command.request.SendSmsCommand;
import cn.javayong.platform.sms.adapter.command.response.SmsResponseCommand;
import cn.javayong.platform.sms.adapter.support.SPI;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SPI("emay")
public class EmayOuterAdapter implements OuterAdapter {

    private final static Logger logger = LoggerFactory.getLogger(EmayOuterAdapter.class);

    private final static String extendedCode = "01";

    private SmsChannelConfig smsChannelConfig;

    private String instanceId = UUID.randomUUID().toString().replaceAll("-", "");

    private SmsSDKClient client;

    @Override
    public void init(SmsChannelConfig smsChannelConfig) throws SDKParamsException {
        logger.info("初始化亿美短信客户端 渠道编号:[" + smsChannelConfig.getId() + "] appkey:[" + smsChannelConfig.getChannelAppkey() + "] 实例Id:" + instanceId);
        this.smsChannelConfig = smsChannelConfig;
        String channelDomain = smsChannelConfig.getChannelDomain();
        String appId = smsChannelConfig.getChannelAppkey();
        String appSecret = smsChannelConfig.getChannelAppsecret();
        this.client = new SmsSDKClient(channelDomain, -1, appId, appSecret);
    }

    @Override
    public SmsResponseCommand sendSmsByTemplateId(SendSmsCommand smsSendRequest) {
        String mobile = smsSendRequest.getPhoneNumbers();
        String signName = smsSendRequest.getSignName();
        String customSmsId = UUID.randomUUID().toString().replaceAll("-", "");
        String content = SmsTemplateUtil.renderContentWithSignName(smsSendRequest.getTemplateParam(), smsSendRequest.getTemplateContent(), signName);
        SmsSingleRequest request = new SmsSingleRequest(mobile, content, customSmsId, extendedCode, "");
        ResultModel<SmsResponse> result = client.sendSingleSms(request);
        logger.info("emay send result:" + JSON.toJSONString(result));
        if ("SUCCESS".equals(result.getCode())) {
            return new SmsResponseCommand(SmsResponseCommand.SUCCESS_CODE, result.getResult().getSmsId());
        }
        return new SmsResponseCommand(SmsResponseCommand.FAIL_CODE);
    }

    @Override
    public SmsResponseCommand addSmsTemplate(AddSmsTemplateCommand addSmsTemplateCommand) {
        // 将标准模版转换成 亿美模版 信息
        String templateContent = addSmsTemplateCommand.getTemplateContent();
        Map<String, String> templateMap = new HashMap<String, String>();
        // 标准模版： 你好，你的信息是：${code}
        // 亿美模版: "templateContent":"【亿美软通】尊敬的{#name#},您好！您已成功注册，您的初始秘钥为{#password#},
        //  					登录后可在个人中心修改，欢迎加入。",
        String emayTemplateContent = "【" + addSmsTemplateCommand.getSignName() + "】" +
                templateContent.replaceAll("\\$", "").replaceAll("\\{", "\\{#").replaceAll("\\}", "#\\}");
        templateMap.put("templateContent", emayTemplateContent);
        templateMap.put("requestTime", String.valueOf(System.currentTimeMillis()));
        templateMap.put("requestValidPeriod", "60");
        logger.info("emay addSmsTemplate templateMap:" + templateMap);
        ResultModel<HashMap> result = HttpUtil.request(
                smsChannelConfig.getChannelAppkey(),
                smsChannelConfig.getChannelAppsecret(),
                smsChannelConfig.getChannelDomain() + "/inter/createTemplateSMS",
                templateMap,
                HashMap.class);
        logger.info("result:" + result.getResult());
        if ("SUCCESS".equals(result.getCode())) {
            // {"templateId":"168984611698400172"}
            Map<String, String> bodyMap = new HashMap<>();
            bodyMap.put("templateCode", (String) result.getResult().get("templateId"));
            bodyMap.put("templateContent", emayTemplateContent);
            // 0 待提交:  1：待审核  2：审核成功 3：审核失败 亿美提交申请默认成功
            bodyMap.put("status", "2");
            return new SmsResponseCommand(SmsResponseCommand.SUCCESS_CODE, bodyMap);
        }
        return new SmsResponseCommand(SmsResponseCommand.FAIL_CODE);
    }

    @Override
    public SmsResponseCommand<Integer> querySmsTemplateStatus(QuerySmsTemplateCommand querySmsTemplateCommand) {
        // 亿美默认模版申请成功
        return new SmsResponseCommand(SmsResponseCommand.SUCCESS_CODE, 2);
    }

    @Override
    public void destroy() {
        logger.warn("销毁亿美短信客户端 渠道编号:[" + smsChannelConfig.getId() + "] appkey:[" + smsChannelConfig.getChannelAppkey() + "] 实例Id:" + instanceId);
    }

}
