package com.swpu.uchain.openexperiment.VO.announcement;

import com.swpu.uchain.openexperiment.domain.Announcement;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @Author: clf
 * @Date: 19-1-25
 * @Description:
 * 公告VO
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class AnnouncementVO extends Announcement implements Serializable {
    /**
     * 点击次数
     */
    private Integer clickTimes;
}
