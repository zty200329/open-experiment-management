package com.swpu.uchain.openexperiment.VO;

import lombok.Data;

import java.util.Date;

/**
 * @Author: clf
 * @Date: 19-1-25
 * @Description:
 * 公告列表元素
 */
@Data
public class AnnouncementListVO {
    /**
     * 公告id
     */
    private Long announcementId;
    /**
     * 标题
     */
    private String title;
    /**
     * 发布时间
     */
    private Date publishTime;
}
