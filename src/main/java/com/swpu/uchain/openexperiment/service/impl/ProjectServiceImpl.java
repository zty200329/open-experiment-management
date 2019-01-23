package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.domain.ProjectGroup;
import com.swpu.uchain.openexperiment.form.project.CreateProjectApplyForm;
import com.swpu.uchain.openexperiment.form.project.JoinProjectApplyForm;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.ProjectService;
import org.springframework.stereotype.Service;

/**
 * @Author: clf
 * @Date: 19-1-22
 * @Description:
 * 项目管理模块
 */
@Service
public class ProjectServiceImpl implements ProjectService {
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
    public ProjectGroup selectByProjectGroupId(Long id) {

        return null;
    }

    @Override
    public Result applyCreateProject(CreateProjectApplyForm createProjectApplyForm) {
        return null;
    }
}
