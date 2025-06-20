package cn.javayong.platform.sms.admin.domain.vo;

import cn.javayong.platform.sms.admin.domain.TSmsRecordDetail;

public class RecordVO extends TSmsRecordDetail {

    private String channelName;

    private String appName;

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }
}
