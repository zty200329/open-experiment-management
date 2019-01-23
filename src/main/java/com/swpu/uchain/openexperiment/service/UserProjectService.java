package com.swpu.uchain.openexperiment.service;

import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.domain.UserProjectGroup;
import com.swpu.uchain.openexperiment.form.project.JoinProjectApplyForm;
import com.swpu.uchain.openexperiment.result.Result;

import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-23
 * @Description:
 * 用户项目关系模块
 */
public interface UserProjectService {
    /**
     * 插入新的用户项目关系
     * @param userProjectGroup
     * @return
     */
    boolean insert(UserProjectGroup userProjectGroup);

    /**
     * 更新用户与项目组关系
     * @param userProjectGroup
     * @return
     */
    boolean update(UserProjectGroup userProjectGroup);

    /**
     * 移除某用户和项目的关系
     * @param id
     */
    void delete(Long id);

    /**
     * 按项目组id和用户id进行查找
     * @param projectGroupId
     * @param userId
     * @return
     */
    UserProjectGroup selectByProjectGroupIdAndUserId(Long projectGroupId, Long userId);

    /**
     * 申请加入项目
     * @param joinProjectApplyForm
     * @return
     */
    Result applyJoinProject(JoinProjectApplyForm joinProjectApplyForm);
}
