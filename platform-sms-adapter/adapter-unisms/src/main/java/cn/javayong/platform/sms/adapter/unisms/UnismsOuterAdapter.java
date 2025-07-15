package cn.javayong.platform.sms.adapter.unisms;

import cn.javayong.platform.sms.adapter.OuterAdapter;
import cn.javayong.platform.sms.adapter.command.req.AddSmsTemplateReqCommand;
import cn.javayong.platform.sms.adapter.command.req.QuerySmsTemplateReqCommand;
import cn.javayong.platform.sms.adapter.command.req.SendSmsReqCommand;
import cn.javayong.platform.sms.adapter.command.resp.SmsRespCommand;
import cn.javayong.platform.sms.adapter.support.SPI;
import cn.javayong.platform.sms.adapter.support.SmsChannelConfig;
import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

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
        String url = BASE_URL;

        Gson gson = new Gson();
        try {
            // 构建请求体
            Map<String, Object> bodyMap = new HashMap<>();
            bodyMap.put("to", sendSmsReqCommand.getPhoneNumbers());
            bodyMap.put("signature", sendSmsReqCommand.getSignName());
            bodyMap.put("templateId", sendSmsReqCommand.getTemplateCode());
            Map<String, String> templateParamMap = JSON.parseObject(sendSmsReqCommand.getTemplateParam(), HashMap.class);
            bodyMap.put("templateData", templateParamMap);

            Map<String, Object> queryMap = new HashMap<>();
            queryMap.put("action", ACTION);
            queryMap.put("accessKeyId", smsChannelConfig.getChannelAppkey());
            // 设置请求体
            if (StringUtils.isNotEmpty(smsChannelConfig.getChannelAppsecret()) && !"-".equals(smsChannelConfig.getChannelAppsecret())) {
                queryMap.put("algorithm", "hmac-sha256");
                queryMap.put("timestamp", new Date().getTime());
                queryMap.put("nonce", UUID.randomUUID().toString().replaceAll("-", ""));
                String strToSign = queryStringify(queryMap);
                queryMap.put("signature", getSignature(strToSign, smsChannelConfig.getChannelAppsecret()));
            }

            URIBuilder uriBuilder = new URIBuilder(smsChannelConfig.getChannelDomain());
            for (Map.Entry<String, Object> entry : queryMap.entrySet()) {
                uriBuilder.addParameter(entry.getKey(), entry.getValue().toString());
            }

            URI uri = null;
            try {
                uri = uriBuilder.build();
            } catch (URISyntaxException e) {
                logger.error("构建 URI 失败", e);
            }

            HttpPost httpPost = new HttpPost(uri);
            httpPost.setHeader("Content-Type", "application/json"); // 可选，如果服务器需要
            CloseableHttpClient httpClient = HttpClients.createDefault();

            String requestBody = gson.toJson(bodyMap);
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
                        return new SmsRespCommand<>(SmsRespCommand.FAIL_CODE, null, "No message details in response");
                    } else {
                        // 业务错误
                        return new SmsRespCommand<>(SmsRespCommand.FAIL_CODE, null, responseBody);
                    }
                } else {
                    return new SmsRespCommand(SmsRespCommand.FAIL_CODE, null, responseBody);
                }
            }
            return new SmsRespCommand(SmsRespCommand.FAIL_CODE, null, "Empty response from server");
        } catch (Exception e) {
            logger.error("unisms sendSmsByTemplateId error:", e);
            return new SmsRespCommand(SmsRespCommand.FAIL_CODE, null, "Empty response from server");
        }
    }

    @Override
    public SmsRespCommand<Map<String, String>> addSmsTemplate(AddSmsTemplateReqCommand addSmsTemplateReqCommand) {
        return new SmsRespCommand(SmsRespCommand.NOT_SUPPORT_CODE, null, "不支持");
    }

    @Override
    public SmsRespCommand<Integer> querySmsTemplateStatus(QuerySmsTemplateReqCommand querySmsTemplateReqCommand) {
        return new SmsRespCommand(SmsRespCommand.NOT_SUPPORT_CODE, null, "不支持");
    }

    @Override
    public void destroy() {
        logger.warn("销毁合一短信客户端 渠道编号:[" + smsChannelConfig.getId() + "] appkey:[" + smsChannelConfig.getChannelAppkey() + "] 实例Id:" + instanceId);
    }

    private static String getSignature(final String message, final String secretKey) {
        try {
            Mac hmac = Mac.getInstance("HmacSHA256");
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(), "HmacSHA256");
            hmac.init(secretKeySpec);

            byte[] bytes = hmac.doFinal(message.getBytes());
            return Base64.getEncoder().encodeToString(bytes);
        } catch (Exception e) {
            return null;
        }
    }

    private static String queryStringify(final Map<String, Object> params) {
        Map<String, Object> sortedMap = new TreeMap<>(new MapKeyComparator());
        sortedMap.putAll(params);
        StringBuilder sb = new StringBuilder();
        Iterator<?> iter = sortedMap.entrySet().iterator();

        while (iter.hasNext()) {
            if (sb.length() > 0) {
                sb.append('&');
            }
            Map.Entry<?, ?> entry = (Map.Entry<?, ?>) iter.next();
            sb.append(entry.getKey()).append("=").append(entry.getValue());
        }

        return sb.toString();
    }

    static class MapKeyComparator implements Comparator<String> {
        @Override
        public int compare(String str1, String str2) {
            return str1.compareTo(str2);
        }
    }

}
