package cn.javayong.platform.sms.admin.common.utils;

import org.apache.commons.lang3.time.DateFormatUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletResponse;

public class UtilsAll {

    public static void responseJSONToClient(HttpServletResponse httpServletResponse, String result) throws IOException {
        httpServletResponse.setCharacterEncoding("utf-8");
        httpServletResponse.setContentType("application/json; charset=utf-8");
        PrintWriter writer = httpServletResponse.getWriter();
        writer.write(result);
    }

    public static Long getNextHourLastSecondTimestamp() {
        // 获取当前时间
        LocalDateTime dateTime = LocalDateTime.now();
        // 将小时、分钟和秒设置为0
        dateTime = dateTime.plusHours(2).withMinute(0).withSecond(0).withNano(0);
        long milliseconds = dateTime.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
        return milliseconds;
    }

    public static Long getNextHouFirstSecondTimestamp() {
        // 获取当前时间
        LocalDateTime dateTime = LocalDateTime.now();
        // 将小时、分钟和秒设置为0
        dateTime = dateTime.plusHours(1).withMinute(0).withSecond(0).withNano(0);
        long milliseconds = dateTime.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
        return milliseconds;
    }

    public static void main(String[] args) {
        System.out.println("下一个小时的最后一秒的毫秒数：" +getNextHourLastSecondTimestamp());
        LocalDateTime currentDateTime = LocalDateTime.now();

        // 下一个小时的时间
        LocalDateTime nextHourDateTime = currentDateTime.plusHours(1).withMinute(0).withSecond(0).withNano(0);

        // 下一个小时的最后一秒
        LocalDateTime nextHourLastSecond = nextHourDateTime.minusSeconds(1);

        // 转换成毫秒
        long milliseconds = nextHourLastSecond.atZone(java.time.ZoneId.systemDefault()).toInstant().toEpochMilli();
        Date d = new Date(milliseconds);
        System.out.println(DateFormatUtils.format(d , "yyyy-MM-dd HH:mm:ss"));


        System.out.println("下一个小时的最后一秒的毫秒数：" + milliseconds);
    }

}
