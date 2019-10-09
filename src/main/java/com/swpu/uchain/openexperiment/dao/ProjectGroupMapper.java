package com.swpu.uchain.openexperiment.dao;

import com.swpu.uchain.openexperiment.VO.project.CheckProjectVO;
import com.swpu.uchain.openexperiment.VO.project.OpenTopicInfo;
import com.swpu.uchain.openexperiment.VO.project.SelectProjectVO;
import com.swpu.uchain.openexperiment.domain.ProjectGroup;
import com.swpu.uchain.openexperiment.form.project.ProjectGroupDetailVO;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author panghu
 */
@Repository
public interface ProjectGroupMapper {

    int deleteByPrimaryKey(Long id);

    int insert(ProjectGroup record);

    ProjectGroup selectByPrimaryKey(Long id);

    List<ProjectGroup> selectAll();

    int updateByPrimaryKey(ProjectGroup record);

    ProjectGroup selectByName(String projectName);

    List<ProjectGroup> selectByUserIdAndStatus(Long userId, Integer projectStatus);

    List<ProjectGroup> selectByCollegeIdAndStatus(@Param("college") String college,@Param("projectStatus") Integer projectStatus);

    List<CheckProjectVO> selectApplyOrderByTime(int projectStatus);

    List<SelectProjectVO> selectByFuzzyName(@Param("name") String name);

    /**
     * 查询所有的公开选题的项目
     * @return
     */
    List<OpenTopicInfo> getAllOpenTopic();

    ProjectGroupDetailVO getProjectGroupDetailVOByProjectId(@Param("projectId")Long projectId );
}