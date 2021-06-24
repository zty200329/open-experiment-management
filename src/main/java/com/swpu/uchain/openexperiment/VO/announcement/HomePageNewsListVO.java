package com.swpu.uchain.openexperiment.VO.announcement;

import lombok.Data;

import java.util.Date;

/**
 * @author zty200329
 * @version 1.0
 * @date 2020/11/12 9:29 下午
 */
@Data
public class HomePageNewsListVO {
    private Integer id;
    /**
     * 文章标题
     */
    private String title;
    /**
     * 发布时间
     */
    private Date publishTime;

    /**
     * 状态 1发布 2保存
     */
    private Short status;

}
