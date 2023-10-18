package com.courage.platform.sms.adapter.support;

import com.alibaba.fastjson.JSON;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static String[] renderTemplateParamArray(String templateParam, String templateContent) {
        Map<String, String> templateParamMap = JSON.parseObject(templateParam, HashMap.class);
        String[] placeholders = extractPlaceholders(templateContent);
        String[] result = new String[placeholders.length];
        for (int i = 0; i < placeholders.length; i++) {
            result[i] = templateParamMap.get(placeholders[i]);
        }
        return result;
    }

    public static String[] extractPlaceholders(String template) {
        List<String> placeholders = new ArrayList<>();
        // 使用正则表达式查找模板中的占位符
        Pattern pattern = Pattern.compile("\\$\\{([a-zA-Z0-9]+)\\}");
        Matcher matcher = pattern.matcher(template);
        while (matcher.find()) {
            placeholders.add(matcher.group(1));
        }
        return placeholders.toArray(new String[0]);
    }

    public static String replaceStandardToTencent(String templateContent) {
        String pattern = "\\$\\{([a-zA-Z0-9]+)\\}";
        // 编译正则表达式模式
        Pattern p = Pattern.compile(pattern);
        // 创建Matcher对象
        Matcher matcher = p.matcher(templateContent);
        // 定义一个替换计数器
        int count = 1;
        // 开始替换
        // 迭代匹配并替换
        StringBuffer output = new StringBuffer();
        while (matcher.find()) {
            // 获取匹配到的内容，即 code 和 time
            String match = matcher.group(1);
            // 根据匹配的内容替换为对应的计数器
            String replacement = "{" + count + "}";
            matcher.appendReplacement(output, replacement);
            // 增加计数器
            count++;
        }
        matcher.appendTail(output);
        String result = output.toString();
        return result;
    }

    public static void main(String[] args) {
        String template = "模版 ${code} , ${idsnb}  ${name} 123 ${number123}";
        String[] extractedPlaceholders = extractPlaceholders(template);
        for (String placeholder : extractedPlaceholders) {
            System.out.println(placeholder);
        }

        // 转换
        String input = "${code}为您的登录验证码，请于${time}分钟内填写，如非本人操作，请忽略本短信.";
        System.out.println(replaceStandardToTencent(input));
    }

}
