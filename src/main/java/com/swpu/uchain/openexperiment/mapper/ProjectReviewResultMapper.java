package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.domain.ProjectReviewResult;
import java.util.List;

public interface ProjectReviewResultMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProjectReviewResult record);

    ProjectReviewResult selectByPrimaryKey(Integer id);

    List<ProjectReviewResult> selectAll();

    int updateByPrimaryKey(ProjectReviewResult record);
}