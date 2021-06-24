package com.swpu.uchain.openexperiment.mapper;

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

    int deleteByPrimaryKey(@Param("id") Long id);

    int insert(Announcement record);

    Announcement selectByPrimaryKeyAndStatus(@Param("id")Long id,@Param("status") Integer status);

    List<Announcement> selectAll();

    int updateByPrimaryKey(Announcement record);

    /**
     * 获取全校的
     * @param condition
     * @return
     */
    List<AnnouncementListVO> selectByConditionAndOrderByTime(QueryCondition condition);

    /**
     * 获取全校的
     * @param condition
     * @return
     */
    List<AnnouncementListVO> selectByConditionAndOrderByTime1(QueryCondition condition);

    /**
     * 学院的
     * @param condition
     * @return
     */
    List<AnnouncementListVO> selectByCollgeAndConditionAndOrderByTime(QueryCondition condition);

    /**
     * 学院的
     * @param condition
     * @return
     */
    List<AnnouncementListVO> selectByCollgeAndConditionAndOrderByTime1(QueryCondition condition);

    int updateAnnouncementStatusById(@Param("status")Integer status,@Param("id") Long id);
}