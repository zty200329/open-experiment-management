package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.domain.Achievement;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AchievementMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Achievement record);

    Achievement selectByPrimaryKey(Long id);

    List<Achievement> selectAll();

    List<Achievement> selectByProjectId(Long id);

    int updateByPrimaryKey(Achievement record);
}