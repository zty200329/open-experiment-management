package com.swpu.uchain.openexperiment.util.excel;

import org.springframework.util.ResourceUtils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

/**
 * @Author zty
 * @Date 2020/2/29 下午11:24
 * @Description: 获取模板文件的工具类
 * 所有的Excel模板文件都放在resources/excel-templates/目录下
 */
public class TemplateFileUtil {

    public static FileInputStream getTemplates(String tempName) throws FileNotFoundException {
        return new FileInputStream(ResourceUtils.getFile("/usr/local/uchain/excel-temp/"+tempName));
    }
}