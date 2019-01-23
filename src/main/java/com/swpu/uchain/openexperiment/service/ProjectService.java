package com.swpu.uchain.openexperiment.service;

import com.swpu.uchain.openexperiment.domain.ProjectGroup;
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
     * 根据项目组id进行查找
     * @param id
     * @return
     */
    ProjectGroup selectByProjectGroupId(Long id);

    /**
     * 立项申请接口
     * @param createProjectApplyForm
     * @return
     */
    Result applyCreateProject(CreateProjectApplyForm createProjectApplyForm);

}
