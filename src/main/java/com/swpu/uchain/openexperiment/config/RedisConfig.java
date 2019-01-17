package com.swpu.uchain.openexperiment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description:
 * 读取yml文件中的redis配置
 */
@Component
@ConfigurationProperties("redis")
@Data
public class RedisConfig {
    private String host;
    private int port;
    private int timeout;
    private int poolMaxTotal;
    private int poolMaxIdle;
    private int poolMaxWait;

}
