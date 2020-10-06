package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.domain.ProjectReview;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
/**
 * @author zty
 */
@Repository
public interface ProjectReviewMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(ProjectReview record);

    ProjectReview selectByPrimaryKey(Integer id);

    List<ProjectReview> selectAll();

    int updateByPrimaryKey(ProjectReview record);

    ProjectReview selectByCollegeAndType(@Param("college")Integer college,@Param("projectType")Integer projectType);

    /**
     * 将所有状态为待上报的全部改为待评审
     * @param college
     * @param projectType
     * @return
     */
    int updateGeneralByCollegeAndType(@Param("college")Integer college,@Param("projectType")Integer projectType);

    int updateKeyByCollegeAndType(@Param("college")Integer college);
}