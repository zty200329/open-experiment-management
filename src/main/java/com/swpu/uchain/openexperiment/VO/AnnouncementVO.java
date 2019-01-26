package com.swpu.uchain.openexperiment.VO;

import com.swpu.uchain.openexperiment.domain.Announcement;
import lombok.Data;

/**
 * @Author: clf
 * @Date: 19-1-25
 * @Description:
 * 公告VO
 */
@Data
public class AnnouncementVO extends Announcement {
    /**
     * 点击次数
     */
    private Integer clickTimes;
}
