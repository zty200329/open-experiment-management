package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.enums.ProjectStatus;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.ProjectService;
import com.swpu.uchain.openexperiment.service.UserProjectService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 项目模块查询接口
 * @author panghu
 */
@CrossOrigin
@RestController
@RequestMapping("/project")
public class ProjectQueryController {

    private ProjectService projectService;

    private UserProjectService userProjectService;

    @Autowired
    public ProjectQueryController(ProjectService projectService, UserProjectService userProjectService) {
        this.projectService = projectService;
        this.userProjectService = userProjectService;
    }

    @ApiOperation("获取项目的立项信息--可使用")
    @PostMapping(value = "/getApplyInfo", name = "获取项目的立项信息")
    public Result getApplyInfo(Long projectGroupId){
        return projectService.getApplyForm(projectGroupId);
    }

    @GetMapping(value = "/getApplyingJoinInfo", name = "获取当前用户（限老师身份）指导项目的申请参加列表")
    @ApiOperation("获取当前用户（限老师身份）指导项目的申请参加列表--可使用")
    public Result getApplyingJoinInfo(){
        return Result.success(projectService.getJoinInfo());
    }

    @ApiOperation("实验室获取待审核的项目（1）--可使用")
    @GetMapping(value = "/getDeclareProjectList", name = "审批项目展示接口")
    public Result getDeclareProjectList (Integer pageNum){
        if (pageNum == null || pageNum <= 0){
            return Result.error(CodeMsg.PAGE_NUM_ERROR);
        }
        return projectService.getCheckInfo(pageNum, ProjectStatus.DECLARE.getValue());
    }

    @ApiOperation("二级单位获取待审核的项目（2）--可使用")
    @GetMapping(value = "/getLabAllowedProjectList", name = "审批项目展示接口")
    public Result getLabAllowedProjectList (Integer pageNum){
        if (pageNum == null || pageNum <= 0){
            return Result.error(CodeMsg.PAGE_NUM_ERROR);
        }
        return projectService.getCheckInfo(pageNum, ProjectStatus.LAB_ALLOWED.getValue());
    }

    @ApiOperation("获取当前用户参与的某状态的项目信息, 项目状态: -1(所有), 0(申报), 1(立项), 2(驳回修改),3(已上报学院领导), 4(中期检查), 5(结项)")
    @GetMapping(value = "/getOwnProjects", name = "获取自己相关的项目信息")
    public Result getOwnProjects(int projectStatus){
        return projectService.getCurrentUserProjects(projectStatus);
    }

    @ApiOperation("职能部门获取待立项审核的项目（3）--可使用")
    @GetMapping(value = "/getSecondaryUnitAllowedProjectList", name = "审批项目展示接口")
    public Result getSecondaryUnitAllowedProjectList (Integer pageNum){
        if (pageNum == null || pageNum <= 0){
            return Result.error(CodeMsg.PAGE_NUM_ERROR);
        }
        return projectService.getCheckInfo(pageNum, ProjectStatus.SECONDARY_UNIT_ALLOWED.getValue());
    }

}
