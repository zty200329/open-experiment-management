package com.swpu.uchain.openexperiment.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @Author: clf
 * @Date: 19-1-25
 * @Description:
 * 配置分页数量配置
 */
@Component
@ConfigurationProperties("page.count")
@Data
public class CountConfig {
    /**
     * 公告列表显示数目
     */
    private Integer announcement;
    /**
     * 项目列表显示数目
     */
    private Integer project;
    /**
     * 教师审核申请用户显示数目
     */
    private Integer checkUser;
    /**
     * 实验室主任审核项目显示数目
     */
    private Integer checkProject;
    /**
     * 管理员用户管理界面显示数目
     */
    private Integer manageUser;
    /**
     * 管理员角色管理显示数目
     */
    private Integer manageRole;
}
