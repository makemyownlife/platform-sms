package cn.javayong.platform.sms.adapter.test.javaspi;

import org.junit.Test;

import java.util.Iterator;
import java.util.ServiceLoader;

public class JavaSPIMain {

    @Test
    public void sayHello() throws Exception {
        ServiceLoader<Robot> serviceLoader = ServiceLoader.load(Robot.class);
        System.out.println("Java SPI");
        // 写法1： serviceLoader.forEach(Robot::sayHello);
        // 写法2 ：Iterator<Driver> driversIterator = loadedDrivers.iterator();
        Iterator<Robot> iterator = serviceLoader.iterator();
        while (iterator.hasNext()) {
            Robot robot = iterator.next();
            System.out.println(robot);
            robot.sayHello();
        }
    }

}
