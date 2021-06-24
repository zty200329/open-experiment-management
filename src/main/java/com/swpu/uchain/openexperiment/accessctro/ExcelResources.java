package com.swpu.uchain.openexperiment.accessctro;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @Author zty
 * @Date 2020/2/29 下午11:17
 * @Description: 用来在对象的get方法上加入的annotation，通过该annotation说明某个属性所对应的标题
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface ExcelResources {
    /**
     * 属性的标题名称
     * @return
     */
    String title();
    /**
     * 在excel的顺序
     * @return
     */
    int order() default 9999;
}