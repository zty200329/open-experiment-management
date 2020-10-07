package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.domain.ProjectReviewResult;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface ProjectReviewResultMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProjectReviewResult record);

    ProjectReviewResult selectByPrimaryKey(Integer id);

    List<ProjectReviewResult> selectAll();

    int updateByPrimaryKey(ProjectReviewResult record);
}