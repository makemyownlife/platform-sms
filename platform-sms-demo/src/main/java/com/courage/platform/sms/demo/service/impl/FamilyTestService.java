package com.courage.platform.sms.demo.service.impl;

import com.courage.platform.sms.demo.service.MyTestService;

public class FamilyTestService implements MyTestService {

    public FamilyTestService(){
        System.out.println("FamilyTestService");
    }

    public void printMylife() {
        System.out.println("我的家庭");
    }

}
