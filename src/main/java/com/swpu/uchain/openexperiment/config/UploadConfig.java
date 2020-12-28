package com.swpu.uchain.openexperiment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: clf
 * @Date: 19-1-29
 * @Description:
 */
@Component
@Data
@ConfigurationProperties("upload")
public class UploadConfig {
    /**
     * 上传重点项目立项申请书文件夹路径
     */
    private String applyDir;

    /**
     *  上传重点项目立项申请书文件夹路径--头部
     */
    private String applyDir2;

    /**
     * 中间PDF存放位置
     */
    private String pdfTempDir;

    /**
     * 上传立项申请正文文件名
     */
    private String applyFileName;
    /**
     * 上传结题报告文件文件名
     */
    private String concludingFileName;

    /**
     * 上传实验报告文件名
     */
    private String experimentReportFileName;

    /**
     * 证明材料上传位置doc
     */
    private String conclusionDir;

    /**
     * 结题异步转换pdf位置
     */
    private String conclusionPdf;

    /**
     * 结题附件位置
     */
    private String conclusionAnnex;

    /**
     * 成果材料
     */
    private String achievementAnnex;

    /**
     * 富文本图片
     */
    private String newsImages;

    /**
     * 批量下载临时文件夹
     */
    private String downloadTemp;

    /**
     * 下载压缩包的临时文件夹
     */
    private String downloadZipTemp;
}

