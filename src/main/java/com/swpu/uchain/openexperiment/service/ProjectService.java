package com.swpu.uchain.openexperiment.service;

import com.swpu.uchain.openexperiment.VO.project.SelectProjectVO;
import com.swpu.uchain.openexperiment.domain.ProjectGroup;
import com.swpu.uchain.openexperiment.form.project.*;
import com.swpu.uchain.openexperiment.result.Result;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-21
 * @Description:
 * 项目管理模块
 */
public interface ProjectService {
    /**
     * 新增项目组
     * @param projectGroup
     * @return
     */
    boolean insert(ProjectGroup projectGroup);

    /**
     * 更新项目组信息
     * @param projectGroup
     * @return
     */
    boolean update(ProjectGroup projectGroup);

    /**
     * 删除项目组
     * @param projectGroupId
     */
    void delete(Long projectGroupId);



    /**
     * 根据项目组id进行查找
     * @param projectGroupId
     * @return
     */
    ProjectGroup selectByProjectGroupId(Long projectGroupId);

    /**
     * 获取某用户的某状态的项目组的信息
     * @param userId
     * @param projectStatus
     * @return
     */
    List<ProjectGroup> selectByUserIdAndProjectStatus(Long userId, Integer projectStatus);

    /**
     * 立项申请接口
     * @param createProjectApplyForm 申请立项表单
     * @return 接口调用返回结果
     */
    Result applyCreateProject(CreateProjectApplyForm createProjectApplyForm);

    /**
     * 更新项目组信息
     * @param updateProjectApplyForm
     * @return
     */
    Result applyUpdateProject(UpdateProjectApplyForm updateProjectApplyForm);

    /**
     * 获取当前用户的所有参与的项目
     * @param projectStatus
     * @return
     */
    Result getCurrentUserProjects(Integer projectStatus);

    /**
     * 同意用户加入项目
     * @param joinForm
     * @return
     */
    Result agreeJoin(JoinForm[] joinForm);

    /**
     * 同意立项
     * @param projectGroupIdList 同意项目ID列表
     * @return
     */
    Result agreeEstablish(List<ProjectCheckForm> projectGroupIdList);

    /**
     * 获取项目的立项申请信息
     * @param projectGroupId
     * @return
     */
    Result getApplyForm(Long projectGroupId);

    /**
     * 项目组组长追加重点项目申请信息
     * @param appendApplyForm
     * @return
     */
    Result appendCreateApply(AppendApplyForm appendApplyForm);

    /**
     * 获取所有待审核立项项目信息
     * @param pageNum
     * @return
     */
    Result getCheckInfo(Integer pageNum);

    /**
     * 上报学院领导
     * @param projectGroupId
     * @return
     */
    Result reportToCollegeLeader(Long projectGroupId);

    /**
     * 生成立项总览表
     */
    void generateEstablishExcel();

    /**
     * 生成结题总览表
     */
    void generateConclusionExcel();

    /**
     * 获取当前指导老师的项目成员审批列表
     * @return
     */
    List getJoinInfo();

    /**
     * 拒绝某用户申请加入项目组
     * @param joinForm
     * @return
     */
    Result rejectJoin(JoinForm[] joinForm);

    /**
     * 根据项目名搜索项目基本信息
     * @param name
     * @return
     */
    List<SelectProjectVO> selectByProjectName(String name);

    /**
     * 驳回,修改立项申请项目信息
     * @param formList 项目拒绝信息集合
     * @return
     */
    Result rejectProjectApply(List<ProjectCheckForm> formList);

    /**
     * 学院领导上报到职能部门
     * @param projectGroupId 项目组ID
     * @return 上报结果
     */
    Result reportToFunctionalDepartment(Long projectGroupId);

    /**
     * 指导老师确认职能部门的修改
     * @return
     */
    Result ensureOrNotModify(ConfirmForm confirmForm);

    /**
     * 获取当前用户的项目具体信息
     * @param projectId 项目ID
     * @return
     */
    Result getProjectDetailById(Long projectId);

    /**
     * 实验室主任/二级单位 审核操作通过
     * @param list
     * @return
     */
    Result checkProjectApply(List<ProjectCheckForm> list);
}
