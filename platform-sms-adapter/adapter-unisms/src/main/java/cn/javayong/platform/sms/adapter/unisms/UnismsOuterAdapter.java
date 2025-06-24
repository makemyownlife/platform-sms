package cn.javayong.platform.sms.adapter.unisms;

import cn.javayong.platform.sms.adapter.OuterAdapter;
import cn.javayong.platform.sms.adapter.command.req.AddSmsTemplateReqCommand;
import cn.javayong.platform.sms.adapter.command.req.QuerySmsTemplateReqCommand;
import cn.javayong.platform.sms.adapter.command.req.SendSmsReqCommand;
import cn.javayong.platform.sms.adapter.command.resp.SmsRespCommand;
import cn.javayong.platform.sms.adapter.support.SPI;
import cn.javayong.platform.sms.adapter.support.SmsChannelConfig;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SPI("unisms")
public class UnismsOuterAdapter implements OuterAdapter {

    private static final String BASE_URL = "https://uni.apistd.com/";

    private static final String ACTION = "sms.message.send";

    private final static Logger logger = LoggerFactory.getLogger(UnismsOuterAdapter.class);

    private String instanceId = UUID.randomUUID().toString().replaceAll("-", "");

    private SmsChannelConfig smsChannelConfig;

    private CloseableHttpClient httpClient;

    @Override
    public void init(SmsChannelConfig smsChannelConfig) {
        logger.info("初始化合一短信客户端 渠道编号:[" + smsChannelConfig.getId() + "] appkey:[" + smsChannelConfig.getChannelAppkey() + "] 实例Id:" + instanceId);
        this.smsChannelConfig = smsChannelConfig;
        this.httpClient = HttpClients.createDefault();
    }

    @Override
    public SmsRespCommand<String> sendSmsByTemplateId(SendSmsReqCommand sendSmsReqCommand) {
        String url = BASE_URL + "?action=" + ACTION + "&accessKeyId=" + smsChannelConfig.getChannelAppkey();

        Gson gson = new Gson();
        try {
            // 构建HTTP请求
            HttpPost httpPost = new HttpPost(url);
            httpPost.addHeader("Content-Type", "application/json");
            // 构建请求体
            Map<String, Object> requestMap = new HashMap<>();
            requestMap.put("to", sendSmsReqCommand.getPhoneNumbers());
            requestMap.put("signature", sendSmsReqCommand.getSignName());
            requestMap.put("templateId", sendSmsReqCommand.getTemplateCode());
            requestMap.put("templateData", sendSmsReqCommand.getTemplateParam());
            // 设置请求体
            String requestBody = gson.toJson(requestMap);
            logger.info("发送合一短信请求：" + requestBody);
            httpPost.setEntity(new StringEntity(requestBody, "UTF-8"));
            // 执行请求
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            // 处理响应
            if (entity != null) {
                // 检查HTTP状态码
                int statusCode = response.getStatusLine().getStatusCode();
                String responseBody = EntityUtils.toString(entity);
                logger.info("发送合一短信 手机号" + sendSmsReqCommand.getPhoneNumbers() + " 结果 statusCode：" + statusCode + " responseBody:" + responseBody);
                if (statusCode == 200) {
                    JsonObject jsonResponse = gson.fromJson(responseBody, JsonObject.class);
                    // 检查业务状态码
                    String code = jsonResponse.get("code").getAsString();
                    if ("0".equals(code)) {
                        // 提取第一条消息ID
                        JsonObject data = jsonResponse.getAsJsonObject("data");
                        JsonArray messages = data.getAsJsonArray("messages");
                        if (messages != null && messages.size() > 0) {
                            String firstMessageId = messages.get(0).getAsJsonObject().get("id").getAsString();
                            return new SmsRespCommand<>(SmsRespCommand.SUCCESS_CODE, firstMessageId);
                        }
                        return new SmsRespCommand<>(SmsRespCommand.FAIL_CODE, "No message details in response");
                    } else {
                        // 业务错误
                        return new SmsRespCommand<>(SmsRespCommand.FAIL_CODE, responseBody);
                    }
                } else {
                    return new SmsRespCommand(SmsRespCommand.FAIL_CODE, responseBody);
                }
            }
            return new SmsRespCommand(SmsRespCommand.FAIL_CODE, "Empty response from server");
        } catch (Exception e) {
            logger.error("unisms sendSmsByTemplateId error:", e);
            return new SmsRespCommand(SmsRespCommand.FAIL_CODE, "Empty response from server");
        }
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
