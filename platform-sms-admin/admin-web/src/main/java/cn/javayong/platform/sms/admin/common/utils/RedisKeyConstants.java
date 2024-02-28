package cn.javayong.platform.sms.admin.common.utils;

/**
 * Created by zhangyong on 2023/9/14.
 */
public class RedisKeyConstants {

    public static final String DELAY_SEND_ZSET = "sms:delay:send:zset";

    public static final String DELAY_SEND_LOCK = "sms:delay:send:lock";

    public static final String LOAD_NEXT_HOUR_LOCK = "sms:delay:load:lock";

    public static final String LOAD_NEXT_HOUR_RESULT = "sms:delay:load:result";

    public static final String LOGIN_USER = "sms:login:user";

    public static final String TEMPLATE_ID_ITEM = "sms:template:id:";

    public static final String RETRY_SEND_ZSET = "sms:retry:send:zset";

    public static final String RETRY_SEND_LOCK = "sms:retry:send:lock";


}
