package com.courage.platform.sms.admin.dispatcher.processor;

/**
 * 处理器响应码
 * Created by zhangyong on 2023/8/13.
 */
public enum ProcessorResponseCode {

    SUCCESS(200, "成功"),

    FAIL(400, "失败"),

    ERROR(500, "异常"),

    TEMPLATE_NOT_EXIST(10001, "模版不存在");

    private final int code;
    private final String value;

    ProcessorResponseCode(int code, String value) {
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