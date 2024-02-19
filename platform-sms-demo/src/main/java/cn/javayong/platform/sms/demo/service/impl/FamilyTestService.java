package cn.javayong.platform.sms.demo.service.impl;

import cn.javayong.platform.sms.demo.service.MyTestService;

public class FamilyTestService implements MyTestService {

    public FamilyTestService(){
        System.out.println("FamilyTestService");
    }

    public void printMylife() {
        System.out.println("我的家庭");
    }

}
