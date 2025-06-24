package cn.javayong.platform.sms.adapter.command.resp;

import org.apache.commons.lang3.StringUtils;

/**
 * 短信发送结果(适配器)
 * Created by zhangyong on 2023/3/1.
 */
public class SmsRespCommand<T> {

    public static final int SUCCESS_CODE = 200;

    public static final int FAIL_CODE = 500;

    private int code;

    private T data;

    private String message = StringUtils.EMPTY;

    public SmsRespCommand(int code) {
        this.code = code;
    }

    public SmsRespCommand(int code, T result) {
        this.data = result;
        this.code = code;
    }

    public SmsRespCommand(int code, T result, String message) {
        this.code = code;
        this.data = result;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}
