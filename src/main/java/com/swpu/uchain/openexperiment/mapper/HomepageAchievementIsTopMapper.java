package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.VO.announcement.TopHomepageAchievement;
import com.swpu.uchain.openexperiment.domain.HomepageAchievementIsTop;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface HomepageAchievementIsTopMapper {
    int deleteByPrimaryKey(Integer id);

    int deleteByAchievementKey(Integer id);

    int insert(HomepageAchievementIsTop record);

    HomepageAchievementIsTop selectByPrimaryKey(Integer id);

    List<TopHomepageAchievement> selectAll();

    List<Integer> selectAllAchieveId();

    int updateByPrimaryKey(HomepageAchievementIsTop record);
}