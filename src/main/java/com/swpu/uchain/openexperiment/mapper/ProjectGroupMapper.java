package com.swpu.uchain.openexperiment.mapper;

import com.swpu.uchain.openexperiment.DTO.ConclusionDTO;
import com.swpu.uchain.openexperiment.VO.project.*;
import com.swpu.uchain.openexperiment.VO.user.UserVO;
import com.swpu.uchain.openexperiment.domain.ProjectGroup;
import com.swpu.uchain.openexperiment.form.query.QueryConditionForm;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * @author panghu
 */
@Repository
public interface ProjectGroupMapper {

    /**
     * 获取所有普通项目信息
     * @param college
     * @return
     */
    List<CheckProjectVO> getAllGeneralByCollege(@Param("college") Integer college);

    /**
     * 根据项目id查询指导教师
     * @param projectGroupId
     * @return
     */
    List<UserVO> selectGuideTeacherByGroupId(Long projectGroupId);
    /**
     * 关键字模糊查询
     * @param keyword
     * @return
     */
    List<SelectByKeywordProjectVO> selectByKeyword(@Param("keyword") String keyword,@Param("status") Integer status);


    List<SelectByKeywordProjectVO> keyProjectSelectByKeyword(@Param("keyword") String keyword,@Param("status") Integer status);

    /**
     * 更新项目上传编号
     * @param projectId
     * @param serialNumber
     * @return
     */
    int updateProjectSerialNumber(@Param("id") Long projectId,@Param("serialNumber")String serialNumber);

    /**
     * 通过学院获取最大的编号 -- 获取用于计算下一个编号
     * @param college
     * @return
     */
    String getMaxSerialNumberByCollege(@Param("college")Integer college);

    /**
     * 通过学院获取最大的临时编号（即创建编号） -- 获取用于计算下一个编号
     * @param college
     * @return
     */
    String getMaxTempSerialNumberByCollege(@Param("college")Integer college,@Param("projectType") Integer projectType);

    /**
     * 更新项目编号
     * @param projectId
     * @param serialNumber
     * @return
     */
    int updateProjectTempSerialNumber(@Param("id") Long projectId,@Param("tempSerialNumber")String serialNumber);


    int deleteByPrimaryKey(Long id);

    int insert(ProjectGroup record);

    ProjectGroup selectByPrimaryKey(@Param("id") Long id);

    int selectSubordinateCollege(Long id);

    ProjectGroup selectByPrimaryProjectName(@Param("projectName") String projectName);

    /**
     * 项目通过条件查询返回的结果
     * @param list 满足筛选条件的id集合
     * @return
     */
    List<ProjectGroup> selectAllByList(List<Long> list);

    int updateByPrimaryKey(ProjectGroup record);

    ProjectGroup selectByName(String projectName);

    /**
     * 通过用于ID和项目状态获取所有的项目 （用于查询个人所在的项目信息）
     * @param userId
     * @param projectStatus
     * @param joinStatus
     * @return
     */
    List<ProjectGroup>  selectByUserIdAndStatus(@Param("userId") Long userId,@Param("projectStatus") Integer projectStatus,@Param("status")Integer joinStatus);

    List<CheckProjectVO> selectFunctionCreateCommonApply();

    List<ProjectGroup> selectByCollegeIdAndStatus(@Param("college") String college,@Param("projectStatus") Integer projectStatus);


    /**
     * 有改动 temp_
     * @param projectStatus
     * @param projectType
     * @param college
     * @return
     */
    List<CheckProjectVO> selectApplyOrderByTime(@Param("projectStatus") int projectStatus,@Param("projectType") Integer projectType,@Param("college") Integer college);

    /**
     * 查询已经完成立项评审的项目
     * @param college
     * @return
     */
    List<ProjectReviewVO> selectHasReview(@Param("college") Integer college,@Param("status") Integer status);

    List<NewCheckProjectVO> selectNewApplyOrderByTime(@Param("projectStatus") int projectStatus,@Param("projectType") Integer projectType,@Param("college") Integer college);

    List<NewCheckProjectVO> selectNewApplyOrderByTime2(@Param("projectStatus") int projectStatus,@Param("projectType") Integer projectType,@Param("college") Integer college);

    List<SelectProjectVO> selectByFuzzyName(@Param("name") String name);

    /**
     * 用于Excel导出，结题的Excel
     * @param college
     * @return
     */
    List<ConclusionDTO> selectConclusionDTOs(@Param("college")Integer college);

    /**
     * 查询所有的公开选题的项目
     * @return
     */
    List<OpenTopicInfo> getAllOpenTopic(List<Long> list);

    /**
     * 通过ID获取项目详情
     * @param projectId
     * @return
     */
    ProjectGroupDetailVO getProjectGroupDetailVOByProjectId(@Param("projectId")Long projectId);

    /**
     * 总览表项目信息
     * @param college
     * @return
     */
    List<ProjectTableInfo> getProjectTableInfoListByCollegeAndList(@Param("college") Integer college,@Param("status")Integer status);

    /**
     * 总览表项目信息 大于
     * @param college
     * @return
     */
    List<ProjectTableInfo> getProjectTableInfoListByCollegeAndList1(@Param("college") Integer college,@Param("status")Integer status);

    int updateProjectStatus(@Param("id") Long projectId,@Param("status")Integer status);


    int updateProjectStatusOfList(@Param("list") List<Long> projectIdList,@Param("status")Integer status);

    int selectSpecifiedProjectList(@Param("list") List<Long> projectIdList,@Param("status")Integer status);

    /**
     * 条件筛选  与 selectAllByList配合使用
     * @param form
     * @return
     */
    List<Long> conditionQuery(QueryConditionForm form);


    List<Long> conditionQueryOfKeyProject(QueryConditionForm form);

    /**
     * 重点项目
     * @param form
     * @return
     */
    List<Long> conditionKeyQueryOfKeyProject(QueryConditionForm form);

    /**
     * 用于查询已经驳回的项目 --舍弃
     * @param operationUnit
     * @param operationType
     * @param college
     * @param type
     * @return
     */
    List<ProjectGroup> selectHistoricalInfoByUnitCollegeAndOperation(@Param("unit") Integer operationUnit,@Param("type") Integer operationType,
                                                                     @Param("college")Integer college,@Param("projectType")Integer type);

       /**
     * 获取普通的已通过的项目
     * @param college
     * @return
     */
    List<ProjectGroup> selectGeneralPassedProjectList(@Param("college")Integer college,@Param("status")Integer projectStatus);


    /**
     * 获取普通的已驳回的项目
     * @param college
     * @return
     */
    List<ProjectGroup> selectGeneralRejectedProjectList(@Param("college")Integer college);



    /**
     * 获取重点已通过的项目
     */
    List<ProjectGroup> selectKeyPassedProjectList(@Param("college")Integer college,@Param("status")Integer projectStatus);

    /**
     * 获取被驳回的重点项目 --舍弃
     * @param operationUnit
     * @param operationType
     * @param college
     * @return
     */
    List<ProjectGroup> selectKeyHistoricalInfoByUnitAndOperation(@Param("unit") Integer operationUnit,@Param("type") Integer operationType,
                                                                 @Param("college")Integer college);
    /**
     * 获取普通的已驳回的项目
     * @param college
     * @return
     */
    List<ProjectGroup> selectKeyRejectedProjectList(@Param("college")Integer college);


    /**
     * 获取指定项目状态的项目数量 -- 用于上报操作的限制
     * @param status
     * @param college
     * @return
     */
    Integer getCountOfSpecifiedStatusAndProjectProject(@Param("status") Integer status,@Param("college")Integer college);

    /**
     * 更新项目类型，用于将重点项目转化为普通项目
     * @param projectId
     * @param type
     */
    void updateProjectType(@Param("id") Long projectId,@Param("type") Integer type,@Param("value")Float value);

}