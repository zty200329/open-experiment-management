package com.swpu.uchain.openexperiment.DTO;

import lombok.Data;

import java.util.Date;

/**
 * @author dengg
 */
@Data
public class KeyProjectDTO {

    /**
     * 项目编号
     */
    private Long id;


    /**
     * 重点项目创建时间
     */
    private Date createTime;

    /**
     * 创建者的学号
     */
    private Long userId;

    /**
     * 创建者名字
     */
    private String creatorName;

    /**
     * 所需经费支持
     */
    private Float applyFunds;

    /**
     * 主要内容
     */
    private String mainContent;

    /**
     * 实验地点
     */
    private String address;

    /**
     * 实验室名称
     */
    private String labName;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 重点项目审核进度
     */
    private Integer status;


}
