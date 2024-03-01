package cn.javayong.platform.sms.client.util;

/**
 * 处理器响应码
 * Created by zhangyong on 2023/8/13.
 */
public enum ResponseCode {

    SUCCESS(200, "成功"),

    ERROR(500, "异常"),

    SIGN_ERROR(403, "验签失败"),

    APP_NOT_EXIST(10000, "应用不存在"),

    TEMPLATE_NOT_EXIST(10001, "模版不存在");

    private final int code;
    private final String value;

    ResponseCode(int code, String value) {
        this.code = code;
        this.value = value;
    }

    public int getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

}
