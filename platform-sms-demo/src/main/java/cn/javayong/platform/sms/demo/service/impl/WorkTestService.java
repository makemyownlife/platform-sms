package cn.javayong.platform.sms.demo.service.impl;

import cn.javayong.platform.sms.demo.service.MyTestService;

public class WorkTestService implements MyTestService {

    public WorkTestService(){
        System.out.println("WorkTestService");
    }

    public void printMylife() {
        System.out.println("我的工作");
    }

}
