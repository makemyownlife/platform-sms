package com.courage.platform.sms.api.domain;

import java.io.Serializable;

/**
 * Created by zhangyong on 2017/7/23.
 */
public class JsonResult implements Serializable {

    private static final long serialVersionUID = 7553249056983455065L;

    private int success;

    private Object data;

    private ErrorMessage error;

    public JsonResult(Object data) {
        this(ResultStatus.SUCCESS, data, null, null, null);
    }

    public JsonResult(int success, Object data, String code, String message) {
        this(success, data, code, message, null);
    }

    public JsonResult(String code, String message) {
        this(ResultStatus.FAILURE, null, code, message, null);
    }

    public JsonResult(int success, Object data, String code, String message, Object errorData) {
        if ((success != ResultStatus.FAILURE && success != ResultStatus.SUCCESS)) {
            throw new IllegalArgumentException("success is not valid");
        }
        this.data = data;
        this.success = success;
        if (success == ResultStatus.FAILURE) {
            this.error = new ErrorMessage(code, message, errorData);
        }
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(Integer success) {
        this.success = success;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public ErrorMessage getError() {
        return error;
    }

    public void setError(ErrorMessage error) {
        this.error = error;
    }

}
