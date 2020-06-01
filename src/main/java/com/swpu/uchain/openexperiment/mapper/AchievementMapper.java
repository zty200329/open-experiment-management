package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.domain.Achievement;
import java.util.List;

public interface AchievementMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Achievement record);

    Achievement selectByPrimaryKey(Long id);

    List<Achievement> selectAll();

    int updateByPrimaryKey(Achievement record);
}