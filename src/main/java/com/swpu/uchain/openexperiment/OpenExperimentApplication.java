package com.swpu.uchain.openexperiment;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author clf
 * 启动类
 */
@EnableAsync
@EnableAspectJAutoProxy(proxyTargetClass = true)
@SpringBootApplication

@MapperScan("com.swpu.uchain.openexperiment.mapper")
public class OpenExperimentApplication {
    public static void main(String[] args) {
        SpringApplication.run(OpenExperimentApplication.class, args);
    }

}

