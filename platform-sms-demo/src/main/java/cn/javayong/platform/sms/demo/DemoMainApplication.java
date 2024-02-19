package cn.javayong.platform.sms.demo;

import cn.javayong.platform.sms.demo.service.MyTestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.support.SpringFactoriesLoader;

import java.util.List;

@SpringBootApplication
public class DemoMainApplication {

    private final static Logger logger = LoggerFactory.getLogger(DemoMainApplication.class);

    public static void main(String[] args) {
        logger.info("开始启动短信测试服务");
        SpringApplication application = new SpringApplication(DemoMainApplication.class);
        application.run(args);
        // 测试 Spring SPI start

        List<MyTestService> myTestServices = SpringFactoriesLoader.loadFactories(
                MyTestService.class,
                Thread.currentThread().getContextClassLoader()
        );

        for (MyTestService testService : myTestServices) {
            testService.printMylife();
        }
        // 测试 Spring SPI end
        logger.info("结束启动短信短信测试服务");
    }

}
