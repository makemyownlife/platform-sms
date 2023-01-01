package com.courage.platform.sms.client;

public class SmsSenderResult {

    public static final int SUCCESS_CODE = 200;

    public static final int FAIL_CODE = 500;

    private int code;

    private String msg;

    private String smsId;

    public SmsSenderResult() {
        this.code = FAIL_CODE;
    }

    public SmsSenderResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public String getSmsId() {
        return smsId;
    }

    public void setSmsId(String smsId) {
        this.smsId = smsId;
    }

    public SmsSenderResult(String msg) {
        this.code = SUCCESS_CODE;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        StringBuffer sb = new StringBuffer();
        return sb.append("smsId:" + smsId).append(" code:" + code + " msg:" + msg).toString();
    }

}
