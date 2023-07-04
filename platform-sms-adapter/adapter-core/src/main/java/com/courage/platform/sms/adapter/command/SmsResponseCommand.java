package com.courage.platform.sms.adapter.command;

/**
 * 短信发送结果(适配器)
 * Created by zhangyong on 2023/3/1.
 */
public class SmsResponseCommand {

    public static final int SUCCESS_CODE = 200;

    public static final int FAIL_CODE = 500;

    private int code;

    private String result;

    public SmsResponseCommand(int code) {
        this.code = code;
    }

    public SmsResponseCommand(int code, String result) {
        this.code = code;
        this.result = result;
    }

    public int getCode() {
        return code;
    }

    public String getResult() {
        return result;
    }

}
