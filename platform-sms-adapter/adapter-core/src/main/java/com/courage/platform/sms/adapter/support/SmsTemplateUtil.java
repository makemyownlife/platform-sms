package com.courage.platform.sms.adapter.support;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

/**
 * 短信模版工具类
 */
public class SmsTemplateUtil {

    public static String renderContentWithSignName(String templateParam, String templateContent, String signName) {
        Map<String, String> templateParamMap = JSON.parseObject(templateParam, HashMap.class);
        return renderContentWithSignName(templateParamMap, templateContent, signName);
    }

    /**
     * 将模版渲染成短信内容
     *
     * @param templateParam   模版参数
     * @param templateContent 模版内容
     * @return 短信内容(不包含签名)
     */
    public static String renderContentWithSignName(Map<String, String> templateParam, String templateContent, String signName) {
        String result = templateContent;
        String prefix = StringUtils.isEmpty(signName) ? StringUtils.EMPTY : "【" + signName + "】";
        // 遍历模板中的参数占位符，并替换为对应的参数值
        for (Map.Entry<String, String> entry : templateParam.entrySet()) {
            String placeholder = "${" + entry.getKey() + "}";
            String paramValue = entry.getValue();
            result = result.replace(placeholder, paramValue);
        }
        return prefix + result;
    }

}
