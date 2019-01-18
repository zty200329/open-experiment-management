package com.swpu.uchain.openexperiment;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author clf
 * 启动类
 */
@SpringBootApplication
@MapperScan("com.swpu.uchain.openexperiment.dao")
public class OpenExperimentApplication {
    public static void main(String[] args) {
        SpringApplication.run(OpenExperimentApplication.class, args);
    }

}

