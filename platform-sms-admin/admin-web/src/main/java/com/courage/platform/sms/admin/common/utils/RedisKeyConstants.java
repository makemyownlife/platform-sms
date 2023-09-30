package com.courage.platform.sms.admin.common.utils;

/**
 * Created by zhangyong on 2023/9/14.
 */
public class RedisKeyConstants {

    public static final String WAITING_SEND_ZSET = "sms:waiting:send:zset";

    public static final String WAITING_SEND_LOCK = "sms:waiting:send:lock";

    public static final String LOGIN_USER = "sms:login:user";

}
