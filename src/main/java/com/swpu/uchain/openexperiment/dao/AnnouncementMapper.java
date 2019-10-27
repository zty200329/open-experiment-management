package com.swpu.uchain.openexperiment.dao;

import com.swpu.uchain.openexperiment.VO.announcement.AnnouncementListVO;
import com.swpu.uchain.openexperiment.domain.Announcement;
import com.swpu.uchain.openexperiment.form.announcement.QueryCondition;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author dengg
 */
@Repository
public interface AnnouncementMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Announcement record);

    Announcement selectByPrimaryKeyAndStatus(@Param("id")Long id,@Param("status") Integer status);

    List<Announcement> selectAll();

    int updateByPrimaryKey(Announcement record);

    List<AnnouncementListVO> selectByConditionAndOrderByTime(QueryCondition condition);

    int updateAnnouncementStatusById(@Param("status")Integer status,@Param("id") Long id);
}