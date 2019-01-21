package com.swpu.uchain.openexperiment.dao;

import com.swpu.uchain.openexperiment.domain.ProjectPolemic;
import java.util.List;

public interface ProjectPolemicMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProjectPolemic record);

    ProjectPolemic selectByPrimaryKey(Long id);

    List<ProjectPolemic> selectAll();

    int updateByPrimaryKey(ProjectPolemic record);
}