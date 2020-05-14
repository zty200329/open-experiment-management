package com.swpu.uchain.openexperiment.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author dengg
 */
@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    @Autowired
    private UploadConfig uploadConfig;

    /**
     * 注册静态文件的自定义映射路径
     */

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //定义到硬盘
        registry.addResourceHandler("/apply/**")
                .addResourceLocations("file:"+uploadConfig.getApplyDir()+"/").resourceChain(false);
        //定义到相对路径
        registry.addResourceHandler("/material/**")
                .addResourceLocations("classpath:/material_sample/").resourceChain(false);
    }
}