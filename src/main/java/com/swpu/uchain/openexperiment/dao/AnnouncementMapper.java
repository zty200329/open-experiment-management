package com.swpu.uchain.openexperiment.dao;

import com.swpu.uchain.openexperiment.VO.announcement.AnnouncementListVO;
import com.swpu.uchain.openexperiment.domain.Announcement;
import java.util.List;

public interface AnnouncementMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Announcement record);

    Announcement selectByPrimaryKey(Long id);

    List<Announcement> selectAll();

    int updateByPrimaryKey(Announcement record);

    List<AnnouncementListVO> selectOrderByTime();
}