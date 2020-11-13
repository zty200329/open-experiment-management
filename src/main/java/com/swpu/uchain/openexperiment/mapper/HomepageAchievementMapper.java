package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.domain.HomepageAchievement;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HomepageAchievementMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(HomepageAchievement record);

    HomepageAchievement selectByPrimaryKey(Integer id);

    List<HomepageAchievement> selectAll();

    int updateByPrimaryKey(HomepageAchievement record);
}