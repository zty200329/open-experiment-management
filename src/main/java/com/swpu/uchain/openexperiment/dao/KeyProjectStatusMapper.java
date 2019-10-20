package com.swpu.uchain.openexperiment.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author dengg
 */
@Repository
public interface KeyProjectStatusMapper {

    int insert(@Param("projectId") Long projectId,@Param("status") Integer status,@Param("college") Integer College);

    int update(@Param("projectId") Long projectId,@Param("status") Integer status);

    Integer getStatusByProjectId(@Param("projectId") Long projectId);

    List<Long> getProjectIdListByStatusAndCollege(@Param("status") Integer status,@Param("college") Integer college);
}

