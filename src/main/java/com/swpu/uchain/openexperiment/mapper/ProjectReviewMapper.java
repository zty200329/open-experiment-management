package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.domain.ProjectReview;
import java.util.List;

public interface ProjectReviewMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProjectReview record);

    ProjectReview selectByPrimaryKey(Integer id);

    List<ProjectReview> selectAll();

    int updateByPrimaryKey(ProjectReview record);
}