package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.DTO.OperationRecord;
import com.swpu.uchain.openexperiment.domain.ProjectReviewResult;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author zty
 */
@Repository
public interface ProjectReviewResultMapper {
    int deleteByProjectId(Long id);

    int insert(ProjectReviewResult record);

    ProjectReviewResult selectByPrimaryKey(Integer id);

    List<ProjectReviewResult> selectAll();

    int multiInsert(List<ProjectReviewResult> list);

}