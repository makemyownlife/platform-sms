package com.courage.platform.sms.core.domain;

import java.util.concurrent.ConcurrentMap;

public class ChannelSendResult {

    public static final int SUCCESS_CODE = 0;

    public static final int FAIL_CODE = 1;

    private int code;

    private String msg;

    private ConcurrentMap<String, String> msgIds; //value:手机号码  key:短信id

    public ChannelSendResult() {
        this.code = FAIL_CODE;
    }

    public ChannelSendResult(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public ChannelSendResult(String msg) {
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

    public ConcurrentMap<String, String> getMsgIds() {
        return msgIds;
    }

    public void setMsgIds(ConcurrentMap<String, String> msgIds) {
        this.msgIds = msgIds;
    }
}
