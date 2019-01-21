package com.swpu.uchain.openexperiment.service;

import com.swpu.uchain.openexperiment.form.project.CreateProjectApplyForm;
import com.swpu.uchain.openexperiment.form.project.JoinProjectApplyForm;
import com.swpu.uchain.openexperiment.result.Result;

/**
 * @Author: clf
 * @Date: 19-1-21
 * @Description:
 * 项目管理模块
 */
public interface ProjectService {
    /**
     * 立项申请接口
     * @param createProjectApplyForm
     * @return
     */
    Result applyCreateProject(CreateProjectApplyForm createProjectApplyForm);

    /**
     * 申请加入项目
     * @param joinProjectApplyForm
     * @return
     */
    Result applyJoinProject(JoinProjectApplyForm joinProjectApplyForm);


}
