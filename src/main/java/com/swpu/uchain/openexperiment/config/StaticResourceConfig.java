package com.swpu.uchain.openexperiment.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author dengg
 */
@Configuration
public class StaticResourceConfig implements WebMvcConfigurer {

    /**
     * 注册静态文件的自定义映射路径
     */
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        //定义到新文件夹
        registry.addResourceHandler("/apply/**")
                .addResourceLocations("classpath:/apply_dir/");
        registry.addResourceHandler("/material/**")
                .addResourceLocations("classpath:/material_sample/");
//        //定义到硬盘
//        registry.addResourceHandler("/picture/**")
//                .addResourceLocations("file:D:/picture/");
//        super.addResourceHandlers(registry);
    }
}