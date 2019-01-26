package com.swpu.uchain.openexperiment.dao;

import com.swpu.uchain.openexperiment.domain.ProjectGroup;
import java.util.List;

public interface ProjectGroupMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProjectGroup record);

    ProjectGroup selectByPrimaryKey(Long id);

    List<ProjectGroup> selectAll();

    int updateByPrimaryKey(ProjectGroup record);

    ProjectGroup selectByName(String projectName);

    List<ProjectGroup> selectByUserId(Long userId);

    List<ProjectGroup> selectApplyByPageOrderByTime(Integer startNum, Integer count);
}