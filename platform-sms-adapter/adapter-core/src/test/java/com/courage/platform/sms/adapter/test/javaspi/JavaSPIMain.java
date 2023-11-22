package com.courage.platform.sms.adapter.test.javaspi;

import org.junit.Test;

import java.util.ServiceLoader;

public class JavaSPIMain {

    @Test
    public void sayHello() throws Exception {
        ServiceLoader<Robot> serviceLoader = ServiceLoader.load(Robot.class);
        System.out.println("Java SPI");
        serviceLoader.forEach(Robot::sayHello);
    }

}
