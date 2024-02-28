package cn.javayong.platform.sms.admin.dispatcher.processor.requeset;

/**
 * 请求命令枚举
 * Created by zhangyong on 2023/7/14.
 */
public class RequestCode {

    public static final int SEND_MESSAGE = 10;

    public static final int APPLY_TEMPLATE = 11;

    // 立即发送短信
    public static final int NOW_CREATE_RECORD_DETAIL = 12;

    // 延迟短信发送
    public static final int DELAY_CREATE_RECORD_DETAIL = 13;

}
