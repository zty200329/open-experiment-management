package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.enums.ProjectStatus;
import com.swpu.uchain.openexperiment.enums.RoleType;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.ProjectService;
import com.swpu.uchain.openexperiment.service.UserProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

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


    @Autowired
    public ProjectQueryController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @ApiOperation("学生获取可参与的开放性选题")
    @GetMapping("/getAllOpenTopicByStudent")
    public Result getAllOpenTopic(){
        return projectService.getAllOpenTopic();
    }

    @ApiOperation("获取项目的立项信息--可使用")
    @GetMapping(value = "/getApplyInfo", name = "获取项目的立项信息")
    public Result getApplyInfo(Long projectGroupId){
        return projectService.getApplyForm(projectGroupId);
    }

    @GetMapping(value = "/getApplyingJoinInfo", name = "获取当前用户（限老师身份）指导项目的申请参加列表")
    @ApiOperation("获取当前用户（限老师身份）指导项目的申请参加列表--可使用")
    public Result getApplyingJoinInfo(){
        return Result.success(projectService.getJoinInfo());
    }


    @ApiIgnore("获取当前用户参与的某状态的项目信息, 项目状态: -1(所有), 0(申报), 1(立项), 2(驳回修改),3(已上报学院领导), 4(中期检查), 5(结项)")
    @GetMapping(value = "/getOwnProjects", name = "获取自己相关的项目信息")
    public Result getOwnProjects(int projectStatus){
        return projectService.getCurrentUserProjects(projectStatus);
    }

    @ApiOperation("获取当前用户的项目具体信息--可使用")
    @GetMapping("/getProjectDetailById")
    public Result getProjectDetailById(Long projectId){
        return projectService.getProjectDetailById(projectId);
    }

    @ApiOperation("实验室获取待立项审核的项目")
    @GetMapping(value = "getPendingApprovalProjectByLabAdministrator")
    public Result getPendingApprovalProjectByLabAdministrator (Integer pageNum){
        if (pageNum == null || pageNum <= 0){
            return Result.error(CodeMsg.PAGE_NUM_ERROR);
        }
        return projectService.getPendingApprovalProjectByLabAdministrator(pageNum);
    }

    @ApiOperation("二级单位获取待立项审核的项目")
    @GetMapping(value = "getPendingApprovalProjectBySecondaryUnit")
    public Result getPendingApprovalProjectBySecondaryUnit (Integer pageNum){
        if (pageNum == null || pageNum <= 0){
            return Result.error(CodeMsg.PAGE_NUM_ERROR);
        }
        return projectService.getPendingApprovalProjectBySecondaryUnit(pageNum);
    }

    @ApiOperation("职能部门获取待立项审核的项目")
    @GetMapping(value = "getPendingApprovalProjectByFunctionalDepartment")
    public Result getPendingApprovalProjectByFunctionalDepartment (Integer pageNum){
        if (pageNum == null || pageNum <= 0){
            return Result.error(CodeMsg.PAGE_NUM_ERROR);
        }
        return projectService.getPendingApprovalProjectByFunctionalDepartment(pageNum);
    }

    @ApiOperation("根据项目名模糊查询项目--可使用")
    @GetMapping(value = "/selectProject", name = "根据项目名模糊查询项目--可使用")
    public Result selectProject(String name){
        if (StringUtils.isEmpty(name)){
            return Result.error(CodeMsg.PARAM_CANT_BE_NULL);
        }
        return Result.success(projectService.selectByProjectName(name));
    }

    @ApiIgnore("实验室主任查看待审核项目")
    @GetMapping("/getPendingReviewByLabLeader")
    public Result getPendingReviewByLabLeader(){
        return projectService.getPendingReviewByLabLeader();
    }

    @ApiOperation("通过项目ID查看项目详情")
    @GetMapping("/getProjectGroupDetailVOByProjectId")
    public Result getProjectGroupDetailByProjectId(Long projectId){
        return projectService.getProjectGroupDetailVOByProjectId(projectId);
    }

}
