package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.DTO.KeyProjectDTO;
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

    List<KeyProjectDTO> getKeyProjectDTOListByStatusAndCollege(@Param("status")Integer status, @Param("college")Integer college);

    List<KeyProjectDTO> getKeyProjectListByUserId(@Param("userId") Long userId,@Param("status")Integer status);

    Integer getCountOfSpecifiedStatusAndProjectProject(@Param("status") Integer status,@Param("college")Integer college);
}

