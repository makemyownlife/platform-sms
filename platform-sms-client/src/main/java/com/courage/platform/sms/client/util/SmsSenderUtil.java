package com.courage.platform.sms.client.util;

import org.apache.commons.codec.digest.DigestUtils;

import java.util.ArrayList;
import java.util.Random;


public class SmsSenderUtil {

    public static boolean isNotEmpty(String s) {
        if (s == null || s.isEmpty()) {
            return false;
        }
        return true;
    }

    public static long getCurrentTime() {
        return System.currentTimeMillis();
    }

    public static long getRandom() {
        return (new Random(SmsSenderUtil.getCurrentTime())).nextLong() % 900000 + 100000;
    }

    public static String calculateSignature(String appSecret, long random, long time,
                                            String q) {
        StringBuffer buffer = new StringBuffer("appSecret=")
                .append(appSecret)
                .append("&random=")
                .append(random)
                .append("&time=")
                .append(time)
                .append("&q=")
                .append(q);

        return sha256(buffer.toString());
    }

    public static String calculateSignature(String appSecret, long random, long time,
                                            String[] phoneNumbers) {
        StringBuffer buffer = new StringBuffer("appSecret=")
                .append(appSecret)
                .append("&random=")
                .append(random)
                .append("&time=")
                .append(time)
                .append("&q=");
        if (phoneNumbers.length > 0) {
            buffer.append(phoneNumbers[0]);
            for (int i = 1; i < phoneNumbers.length; i++) {
                buffer.append(",");
                buffer.append(phoneNumbers[i]);
            }
        }
        return sha256(buffer.toString());
    }

    public static String calculateSignature(String appSecret, long random, long time,
                                            ArrayList<String> phoneNumbers) {
        return calculateSignature(appSecret, random, time, phoneNumbers.toArray(new String[0]));
    }

    public static String calculateSignature(String appSecret, long random, long time) {
        StringBuffer buffer = new StringBuffer("appSecret=")
                .append(appSecret)
                .append("&random=")
                .append(random)
                .append("&time=")
                .append(time);
        return sha256(buffer.toString());
    }

    private static String sha256(String rawString) {
        return DigestUtils.sha256Hex(rawString);
    }

}
