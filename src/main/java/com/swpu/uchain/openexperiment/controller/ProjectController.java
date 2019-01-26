package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.form.project.*;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.ProjectService;
import com.swpu.uchain.openexperiment.service.UserProjectService;
import io.swagger.annotations.Api;
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
@Api(tags = "项目模块接口")
public class ProjectController {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private UserProjectService userProjectService;

    @PostMapping(value = "/createApply", name = "申请立项接口")
    public Object createApply(@Valid CreateProjectApplyForm createProjectApplyForm){
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

    @GetMapping(value = "/checkApplyInfo", name = "审批项目展示接口")
    public Object getCheckApplyInfo(Integer pageNum){
        if (pageNum == null || pageNum <= 0){
            return Result.error(CodeMsg.PAGE_NUM_ERROR);
        }
        return projectService.checkApplyInfo(pageNum);
    }

    @PostMapping(value = "/agreeEstablish", name = "同意立项")
    public Object agreeEstablish(Long projectGroupId){
        if (projectGroupId == null){
            return Result.error(CodeMsg.PARAM_CANT_BE_NULL);
        }
        return projectService.agreeEstablish(projectGroupId);
    }

    @PostMapping(value = "/aimMemberLeader", name = "修改项目组成员身份")
    public Object aimMemberLeader(@Valid AimForm aimForm){
        return userProjectService.aimUserMemberRole(aimForm);
    }

    @PostMapping(value = "/appendCreateApply", name = "追加立项申请内容")
    public Object appendCreateApply(@Valid AppendApplyForm appendApplyForm){
        return projectService.appendCreateApply(appendApplyForm);
    }
}
