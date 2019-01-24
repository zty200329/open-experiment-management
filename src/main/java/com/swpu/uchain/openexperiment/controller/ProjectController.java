package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.form.project.JoinForm;
import com.swpu.uchain.openexperiment.form.project.CreateProjectApplyForm;
import com.swpu.uchain.openexperiment.form.project.JoinProjectApplyForm;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.ProjectService;
import com.swpu.uchain.openexperiment.service.UserProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Author: clf
 * @Date: 19-1-21
 * @Description:
 * 项目控制接口
 */
@CrossOrigin
@RestController
@RequestMapping("/project")
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserProjectService userProjectService;

    @PostMapping(value = "/createApply", name = "申请立项接口")
    private Object createApply(@Valid CreateProjectApplyForm createProjectApplyForm){
        return projectService.applyCreateProject(createProjectApplyForm);
    }

    @PostMapping(value = "/joinApply", name = "申请参与项目接口")
    public Object joinApply(@Valid JoinProjectApplyForm joinProjectApplyForm){
        return userProjectService.applyJoinProject(joinProjectApplyForm);
    }

    @GetMapping(value = "/getOwnProjects", name = "获取自己相关的项目信息")
    public Object getOwnProjects(){
        return projectService.getCurrentUserProjects();
    }

    @PostMapping(value = "/getApplyInfo", name = "获取项目的立项信息")
    public Object getApplyInfo(Long projectGroupId){
        return projectService.getApplyForm(projectGroupId);
    }

    @PostMapping(value = "/agreeJoin", name = "同意加入项目")
    public Object agreeJoin(@Valid JoinForm joinForm){
        return projectService.agreeJoin(joinForm);
    }

    @PostMapping(value = "/agreeEstablish", name = "同意立项")
    public Object agreeEstablish(Long projectGroupId){
        if (projectGroupId == null){
            return Result.error(CodeMsg.PARAM_CANT_BE_NULL);
        }
        return projectService.agreeEstablish(projectGroupId);
    }

}
