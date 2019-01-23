package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.dao.ProjectGroupMapper;
import com.swpu.uchain.openexperiment.domain.ProjectGroup;
import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.form.project.CreateProjectApplyForm;
import com.swpu.uchain.openexperiment.redis.RedisService;
import com.swpu.uchain.openexperiment.redis.key.ProjectGroupKey;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.ProjectService;
import com.swpu.uchain.openexperiment.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author: clf
 * @Date: 19-1-22
 * @Description:
 * 项目管理模块
 */
@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectGroupMapper projectGroupMapper;
    @Autowired
    private RedisService redisService;

    @Override
    public boolean insert(ProjectGroup projectGroup) {
        return false;
    }

    @Override
    public boolean update(ProjectGroup projectGroup) {
        return false;
    }

    @Override
    public void delete(Long projectGroupId) {

    }

    @Override
    public ProjectGroup selectByProjectGroupId(Long projectGroupId) {
        ProjectGroup projectGroup = redisService.get(ProjectGroupKey.getByProjectGroupId, projectGroupId + "", ProjectGroup.class);
        if (projectGroup == null){
            projectGroup = projectGroupMapper.selectByPrimaryKey(projectGroupId);
            if (projectGroup != null){
                redisService.set(ProjectGroupKey.getByProjectGroupId, projectGroupId + "", projectGroup);
            }
        }
        return projectGroup;
    }

    @Override
    public Result applyCreateProject(CreateProjectApplyForm createProjectApplyForm) {
        ProjectGroup projectGroup = projectGroupMapper.selectByName(createProjectApplyForm.getProjectName());
        if (projectGroup != null){
            return Result.error(CodeMsg.PROJECT_GROUP_HAD_EXIST);
        }
        projectGroup = new ProjectGroup();
        //TODO,创建项目组
        projectGroup.setAchievementCheck(createProjectApplyForm.getAchievementCheck());
        return null;
    }

    @Override
    public Result getCurrentUserProjects() {
        User currentUser = userService.getCurrentUser();
        if (userService == null){
            return Result.error(CodeMsg.AUTHENTICATION_ERROR);
        }

        return null;
    }
}
