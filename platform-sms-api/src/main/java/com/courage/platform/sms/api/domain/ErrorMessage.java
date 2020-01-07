package com.courage.platform.sms.api.domain;

import java.io.Serializable;

/**
 * Created by zhangyong on 2017/7/27.
 */
public class ErrorMessage implements Serializable {

    private static final long serialVersionUID = -8519677473220335082L;

    private String code;

    private String message;

    private Object data;

    public ErrorMessage(String code, String message) {
        this(code, message, null);
    }

    public ErrorMessage(String code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

}
