package com.swpu.uchain.openexperiment.VO.announcement;

import lombok.Data;

import java.util.Date;

/**
 * @author zty200329
 * @version 1.0
 * @date 2020/11/14 1:06 上午
 */
@Data
public class AchievementShowVO {
    private Integer id;

    private String imgUrl;

    private String projectName;

    private String subordinateCollege;

    private Integer projectType;

    private Integer experimentType;

    private Date publishTime;

    private Integer status;
}
