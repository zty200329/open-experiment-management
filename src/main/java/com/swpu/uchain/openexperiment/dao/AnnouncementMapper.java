package com.swpu.uchain.openexperiment.dao;

import com.swpu.uchain.openexperiment.VO.announcement.AnnouncementListVO;
import com.swpu.uchain.openexperiment.domain.Announcement;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnnouncementMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Announcement record);

    Announcement selectByPrimaryKey(Long id);

    List<Announcement> selectAll();

    int updateByPrimaryKey(Announcement record);

    List<AnnouncementListVO> selectOrderByTime();
}