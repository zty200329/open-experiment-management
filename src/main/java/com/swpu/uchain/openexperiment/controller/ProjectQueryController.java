package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.enums.ProjectStatus;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.ProjectService;
import com.swpu.uchain.openexperiment.service.UserProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

/**
 * 项目模块查询接口
 * @author panghu
 */
@CrossOrigin
@RestController
@Api(tags = "项目模块查询接口")
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


    @ApiOperation("获取当前用户参与的某状态的项目信息, 项目状态: -1(所有), 0(申报), 1(立项), 2(驳回修改),3(已上报学院领导), 4(中期检查), 5(结项)")
    @GetMapping(value = "/getOwnProjects", name = "获取自己相关的项目信息")
    public Result getOwnProjects(int projectStatus){
        return projectService.getCurrentUserProjects(projectStatus);
    }

    @ApiOperation("获取当前用户的项目具体信息(仅指导老师可用)--可使用")
    @GetMapping("/getProjectDetailById")
    public Result getProjectDetailById(Long projectId){
        return projectService.getProjectDetailById(projectId);
    }

    @ApiOperation("职能部门,二级单位,实验室获取待立项审核的项目--可使用")
    @GetMapping(value = "/getSecondaryUnitAllowedProjectList", name = "审批项目展示接口")
    public Result getSecondaryUnitAllowedProjectList (Integer pageNum){
        if (pageNum == null || pageNum <= 0){
            return Result.error(CodeMsg.PAGE_NUM_ERROR);
        }
        return projectService.getCheckInfo(pageNum);
    }

    @ApiOperation("根据项目名模糊查询项目--可使用")
    @GetMapping(value = "/selectProject", name = "根据项目名模糊查询项目--可使用")
    public Result selectProject(String name){
        if (StringUtils.isEmpty(name)){
            return Result.error(CodeMsg.PARAM_CANT_BE_NULL);
        }
        return Result.success(projectService.selectByProjectName(name));
    }

}
