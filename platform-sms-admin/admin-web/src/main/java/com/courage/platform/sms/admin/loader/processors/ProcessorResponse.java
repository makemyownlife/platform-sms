package com.courage.platform.sms.admin.loader.processors;

/**
 * Created by zhangyong on 2023/7/14.
 */
public class ProcessorResponse<T> {

    public static final int SUCCESS_CODE = 200;

    public static final int FAIL_CODE = 500;

    private int code;

    private T data;

    public ProcessorResponse(int code, T result) {
        this.data = result;
        this.code = code;
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

}
