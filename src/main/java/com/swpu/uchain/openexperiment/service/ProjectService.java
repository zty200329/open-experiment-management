package com.swpu.uchain.openexperiment.service;

import com.swpu.uchain.openexperiment.domain.ProjectGroup;
import com.swpu.uchain.openexperiment.form.project.JoinForm;
import com.swpu.uchain.openexperiment.form.project.CreateProjectApplyForm;
import com.swpu.uchain.openexperiment.result.Result;

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
     * 添加项目组
     * @param projectGroup
     * @return
     */
    Result addProjectGroup(ProjectGroup projectGroup);

    /**
     * 根据项目组id进行查找
     * @param projectGroupId
     * @return
     */
    ProjectGroup selectByProjectGroupId(Long projectGroupId);

    /**
     * 立项申请接口
     * @param createProjectApplyForm
     * @return
     */
    Result applyCreateProject(CreateProjectApplyForm createProjectApplyForm);

    /**
     * 获取当前用户的所有参与的项目
     * @return
     */
    Result getCurrentUserProjects();

    /**
     * 同意用户加入项目
     * @param joinForm
     * @return
     */
    Result agreeJoin(JoinForm joinForm);

    /**
     * 同意立项
     * @param projectGroupId
     * @return
     */
    Result agreeEstablish(Long projectGroupId);

    /**
     * 获取项目的立项申请信息
     * @param projectGroupId
     * @return
     */
    Result getApplyForm(Long projectGroupId);
}
