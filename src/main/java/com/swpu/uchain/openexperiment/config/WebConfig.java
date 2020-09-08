package com.swpu.uchain.openexperiment.config;

import com.swpu.uchain.openexperiment.security.AuthRoleInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description:
 * 注入对应的bean
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Autowired
    private AuthRoleInterceptor authRoleInterceptor;

    String str = "C:\\Users\\Administrator\\Desktop\\openexperiment\\";
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authRoleInterceptor);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/document/**").addResourceLocations("file:" + str);
        super.addResourceHandlers(registry);
    }
}
