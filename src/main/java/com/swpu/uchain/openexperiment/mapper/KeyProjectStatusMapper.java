package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.DTO.KeyProjectDTO;
import com.swpu.uchain.openexperiment.VO.project.ProjectReviewVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author dengg
 */
@Repository
public interface KeyProjectStatusMapper {

    int insert(@Param("projectId") Long projectId,@Param("status") Integer status,
               @Param("college") Integer College,@Param("creatorId") Long creatorId);

    int update(@Param("projectId") Long projectId,@Param("status") Integer status);

    int updateList(@Param("list")List<Long> projectId, @Param("status")Integer status);

    Integer getStatusByProjectId(@Param("projectId") Long projectId);

    Integer getCollegeByProjectId(@Param("projectId") Long projectId);

    List<KeyProjectDTO> getKeyProjectDTOListByStatusAndCollege(@Param("status")Integer status, @Param("college")Integer college);

    List<ProjectReviewVO> selectKeyHasReview(@Param("college")Integer college,@Param("status") Integer status);

    List<KeyProjectDTO> getKeyProjectDTOListByStatusAndCollege2(@Param("college")Integer college);

    List<KeyProjectDTO> getKeyProjectListByUserIdAndProjectStatus(@Param("userId") Long userId,@Param("status")Integer status);

    Integer getCountOfSpecifiedStatusAndProjectProject(@Param("status") Integer status,@Param("college")Integer college);

    int deleteByProjectId(@Param("projectId") Long projectId);
}

