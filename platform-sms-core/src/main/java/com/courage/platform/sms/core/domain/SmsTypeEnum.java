package com.courage.platform.sms.core.domain;

public enum SmsTypeEnum {

    SINGLE(0, "单条"),
    MARKET(1, "营销");

    private int index;
    private String name;

    SmsTypeEnum(int index, String name) {
        this.name = name;
        this.index = index;
    }

    public String getName() {
        return name;
    }

    public int getIndex() {
        return index;
    }

}
