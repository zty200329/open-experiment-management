package com.swpu.uchain.openexperiment.dao;

import com.swpu.uchain.openexperiment.domain.ProjectPolemic;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectPolemicMapper {
    int deleteByPrimaryKey(Long id);

    int insert(ProjectPolemic record);

    ProjectPolemic selectByPrimaryKey(Long id);

    List<ProjectPolemic> selectAll();

    int updateByPrimaryKey(ProjectPolemic record);
}