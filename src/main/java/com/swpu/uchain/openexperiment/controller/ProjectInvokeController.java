package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.enums.RoleType;
import com.swpu.uchain.openexperiment.form.project.*;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.ProjectService;
import com.swpu.uchain.openexperiment.service.UserProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import java.util.List;


/**
 * @author dengg
 *
 */
@CrossOrigin
@RestController
@RequestMapping("/project")
@Api(tags = "普通项目模块执行接口")
public class ProjectInvokeController {

    private ProjectService projectService;

    private UserProjectService userProjectService;

    @Autowired
    public ProjectInvokeController(ProjectService projectService, UserProjectService userProjectService) {
        this.projectService = projectService;
        this.userProjectService = userProjectService;
    }

    @ApiOperation("申请立项接口--可使用")
    @PostMapping(value = "/createApply", name = "申请立项接口")
    public Result createApply(@Valid @RequestBody CreateProjectApplyForm form) {
        return projectService.applyCreateProject(form);
    }

    @ApiOperation("职能部门修改立项申请")
    @PostMapping(value = "/updateApply", name = "修改立项申请")
    public Result updateApply(@Valid @RequestBody UpdateProjectApplyForm updateProjectApplyForm){
        return projectService.applyUpdateProject(updateProjectApplyForm);
    }

    @ApiIgnore("指导教师确认或者否认职能部门修改立项申请的结果(指导老师进行确认)1,确认(立项成功)  2.否认(立项失败)")
    @PostMapping(value = "/ensureOrNotModify", name = "修改立项申请")
    public Result ensureOrNotModify(@Valid @RequestBody ConfirmForm confirmForm){
        return projectService.ensureOrNotModify(confirmForm);
    }

    @ApiOperation("学生申请参与项目接口")
    @PostMapping(value = "/joinApply", name = "申请参与项目接口")
    public Result joinApply(@Valid @RequestBody JoinProjectApplyForm joinProjectApplyForm){
        return userProjectService.applyJoinProject(joinProjectApplyForm);
    }

    @ApiOperation("同意学生加入项目")
    @PostMapping(value = "/agreeJoin")
    public Result agreeJoin(@Valid @RequestBody JoinForm[] joinForm){
        return projectService.agreeJoin(joinForm);
    }

    @ApiOperation("拒绝某用户加入项目组--可使用")
    @PostMapping(value = "/rejectJoin", name = "拒绝某用户加入项目组")
    public Result rejectJoin(@Valid @RequestBody JoinForm[] joinForm){
        return projectService.rejectJoin(joinForm);
    }

    @ApiOperation("职能部门批准操作")
    @PostMapping(value = "/agreeEstablish", name = "同意立项")
    public Result agreeEstablish(@Valid @RequestBody List<ProjectCheckForm> projectGroupIdList){
        return projectService.agreeEstablish(projectGroupIdList);
    }

    @ApiOperation("实验室批准操作")
    @PostMapping(value = "/approveProjectApplyByLabAdministrator")
    public Result approveProjectApplyByLabAdministrator(@RequestBody List<ProjectCheckForm> list){
        return projectService.approveProjectApplyByLabAdministrator(list);
    }

    @ApiOperation("二级单位（学院）批准操作")
    @PostMapping(value = "/approveProjectApplyBySecondaryUnit")
    public Result approveProjectApplyBySecondaryUnit(@RequestBody List<ProjectCheckForm> list){
        return projectService.approveProjectApplyBySecondaryUnit(list);
    }

    @ApiOperation("修改项目组成员身份  1.指导教师2.项目组长3.普通成员--可使用")
    @PostMapping(value = "/aimMemberLeader", name = "修改项目组成员身份-")
    public Result aimMemberLeader(@Valid @RequestBody AimForm aimForm){
        return userProjectService.aimUserMemberRole(aimForm);
    }


//    @ApiOperation("资金报账(职能部门使用)")
//    @PostMapping(value = "/appendCreateApply", name = "追加立项申请内容")
//    public Result appendCreateApply(@Valid @RequestBody AppendApplyForm appendApplyForm){
//        return projectService.appendCreateApply(appendApplyForm);
//    }


    @ApiOperation("上报学院领导(二级单位)--(实验室主任)可使用")
    @PostMapping(value = "/reportToCollegeLeader", name = "上报学院领导")
    public Result reportToCollegeLeader(Long projectGroupId){
        if (projectGroupId == null){
            return Result.error(CodeMsg.PARAM_CANT_BE_NULL);
        }
        return projectService.reportToCollegeLeader(projectGroupId);
    }


    @ApiOperation("上报职能部门--(二级单位)可使用")
    @PostMapping(value = "/reportToFunctionalDepartment")
    public Result reportToFunctionalDepartment(Long projectGroupId){
        if (projectGroupId == null){
            return Result.error(CodeMsg.PARAM_CANT_BE_NULL);
        }
        return projectService.reportToFunctionalDepartment(projectGroupId);
    }


    @ApiOperation(" 实验室驳回项目立项申请")
    @PostMapping(value = "/rejectProjectApplyByLabAdministrator")
    public Result rejectProjectApplyByLabAdministrator(@Valid @RequestBody List<ProjectCheckForm> formList){
        return projectService.rejectProjectApplyByLabAdministrator(formList);
    }

    @ApiOperation(" 二级单位驳回项目立项申请")
    @PostMapping(value = "/rejectProjectApplyBySecondaryUnit")
    public Result rejectProjectApplyBySecondaryUnit(@Valid @RequestBody List<ProjectCheckForm> formList){
        return projectService.rejectProjectApplyBySecondaryUnit(formList);
    }

    @ApiOperation(" 职能部门驳回项目立项申请")
    @PostMapping(value = "/rejectProjectApplyByFunctionalDepartment")
    public Result rejectProjectApplyByFunctionalDepartment(@Valid @RequestBody List<ProjectCheckForm> formList){
        return projectService.rejectProjectApplyByFunctionalDepartment(formList);
    }


}
