package com.swpu.uchain.openexperiment;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;

/**
 * @author clf
 * 启动类
 */
@EnableAsync
@SpringBootApplication
@MapperScan("com.swpu.uchain.openexperiment.dao")
public class OpenExperimentApplication {
    public static void main(String[] args) {
        SpringApplication.run(OpenExperimentApplication.class, args);
    }

}

