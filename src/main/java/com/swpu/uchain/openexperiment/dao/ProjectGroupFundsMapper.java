package com.swpu.uchain.openexperiment.dao;

import com.swpu.uchain.openexperiment.domain.ProjectGroupFunds;
import java.util.List;

public interface ProjectGroupFundsMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProjectGroupFunds record);

    ProjectGroupFunds selectByPrimaryKey(Long id);

    List<ProjectGroupFunds> selectAll();

    int updateByPrimaryKey(ProjectGroupFunds record);
}