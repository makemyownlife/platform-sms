package cn.javayong.platform.sms.client;

import cn.javayong.platform.sms.client.util.ResponseCode;
import org.apache.commons.lang3.StringUtils;

public class SmsSenderResult {
    private String msg = StringUtils.EMPTY;

    private int code;

    private String smsId;

    public SmsSenderResult(String smsId) {
        this.code = ResponseCode.SUCCESS.getCode();
        this.smsId = smsId;
    }

    public SmsSenderResult(String smsId, int code, String msg) {
        this.code = code;
        this.smsId = smsId;
        this.msg = msg;
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
