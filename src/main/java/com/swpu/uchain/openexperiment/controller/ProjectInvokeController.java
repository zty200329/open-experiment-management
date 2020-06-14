package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.enums.RoleType;
import com.swpu.uchain.openexperiment.form.check.KeyProjectCheck;
import com.swpu.uchain.openexperiment.form.funds.FundsUpdateForm;
import com.swpu.uchain.openexperiment.form.project.*;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.FundsService;
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
import java.net.NetworkInterface;
import java.util.List;


/**
 * @author dengg
 *
 */
@CrossOrigin
@RestController
@RequestMapping("/api/project")
@Api(tags = "项目(普通)模块执行接口")
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

    @ApiOperation("职能部门内定")
    @PostMapping("/FunctionCreateCommonApply")
    public Result FunctionCreateCommonApply(@Valid @RequestBody FunctionCreateProjectApplyForm functionCreateProjectApplyForm){
        return projectService.FunctionCreateCommonApply(functionCreateProjectApplyForm);
    }

    @ApiOperation("修改立项申请")
    @PostMapping(value = "/updateApply", name = "修改立项申请")
    public Result updateApply(@Valid @RequestBody UpdateProjectApplyForm updateProjectApplyForm){
        System.err.println(updateProjectApplyForm.toString());
        return projectService.applyUpdateProject(updateProjectApplyForm);
    }

    @ApiIgnore
    @PostMapping(value = "/ensureOrNotModify")
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

    @ApiOperation("拒绝某用户加入项目组")
    @PostMapping(value = "/rejectJoin", name = "拒绝某用户加入项目组")
    public Result rejectJoin(@Valid @RequestBody JoinForm[] joinForm){
        return projectService.rejectJoin(joinForm);
    }

    @ApiOperation("在指定项目组中添加学生")
    @PostMapping("/addStudentToProject")
    public Result addStudentToProject(@Valid @RequestBody JoinForm joinForm){
        return projectService.addStudentToProject(joinForm);
    }

    @ApiOperation("在指定项目组中移除学生")
    @PostMapping("/removeStudentFromProject")
    public Result removeStudentFromProject(@Valid @RequestBody JoinForm joinForm){
        return projectService.removeStudentFromProject(joinForm);
    }

    @ApiOperation("修改项目组成员身份  1.指导教师2.项目组长3.普通成员--可使用")
    @PostMapping(value = "/aimMemberLeader", name = "修改项目组成员身份-")
    public Result aimMemberLeader(@Valid @RequestBody AimForm aimForm){
        return userProjectService.aimUserMemberRole(aimForm);
    }

    @ApiOperation("职能部门批准立项操作")
    @PostMapping(value = "/agreeEstablish", name = "同意立项")
    public Result agreeEstablish(@Valid @RequestBody List<ProjectCheckForm> projectGroupIdList){
        return projectService.agreeEstablish(projectGroupIdList);
    }

    @ApiOperation("职能部门同意中期检查项目")
    @PostMapping(value = "/agreeIntermediateInspectionProject")
    public Result agreeIntermediateInspectionProject(@Valid @RequestBody List<ProjectCheckForm> list){
        return projectService.agreeIntermediateInspectionProject(list);
    }

    @ApiOperation("职能部门不同意中期检查项目")
    @PostMapping(value = "/rejectIntermediateInspectionProject")
    public Result rejectIntermediateInspectionProject(@Valid @RequestBody List<ProjectCheckForm> list){
        return projectService.rejectIntermediateInspectionProject(list);
    }


    @ApiOperation("职能部门同意结题")
    @PostMapping(value = "/agreeToBeConcludingProject")
    public Result agreeToBeConcludingProject(@Valid @RequestBody List<ProjectCheckForm> list){
        return projectService.agreeToBeConcludingProject(list);
    }

    @ApiOperation("实验室拟题批准操作")
    @PostMapping(value = "/approveProjectApplyByLabAdministrator")
    public Result approveProjectApplyByLabAdministrator(@RequestBody List<ProjectCheckForm> list){
        return projectService.approveProjectApplyByLabAdministrator(list);
    }

    @ApiOperation("二级单位（学院）批准操作")
    @PostMapping(value = "/approveProjectApplyBySecondaryUnit")
    public Result approveProjectApplyBySecondaryUnit(@RequestBody List<ProjectCheckForm> list){
        return projectService.approveProjectApplyBySecondaryUnit(list);
    }

    @ApiOperation("中期复核通过")
    @PostMapping("/midTermReviewPassed")
    public Result midTermReviewPassed(@RequestBody List<ProjectCheckForm> list){
        return projectService.midTermReviewPassed(list);
    }

//    @ApiOperation("资金报账(职能部门使用)")
//    @PostMapping(value = "/appendCreateApply", name = "追加立项申请内容")
//    public Result appendCreateApply(@Valid @RequestBody AppendApplyForm appendApplyForm){
//        return projectService.appendCreateApply(appendApplyForm);
//    }


    @ApiOperation("普通项目审批")
    @PostMapping(value = "/reportToCollegeLeader", name = "上报学院领导")
    public Result reportToCollegeLeader(@Valid @RequestBody List<ProjectCheckForm> formList){
        if (formList == null){
            return Result.error(CodeMsg.PARAM_CANT_BE_NULL);
        }
        return projectService.reportToCollegeLeader(formList);
    }


    @ApiOperation("上报职能部门--(二级单位)可使用")
    @PostMapping(value = "/reportToFunctionalDepartment")
    public Result reportToFunctionalDepartment(@RequestBody List<Long> projectGroupIdList){
        if (projectGroupIdList == null){
            return Result.error(CodeMsg.PARAM_CANT_BE_NULL);
        }
        return projectService.reportToFunctionalDepartment(projectGroupIdList);
    }

    @ApiOperation(" 实验室驳回项目拟题申请")
    @PostMapping(value = "/rejectProjectApplyByLabAdministrator")
    public Result rejectProjectApplyByLabAdministrator(@Valid @RequestBody List<ProjectCheckForm> formList){
        return projectService.rejectProjectApplyByLabAdministrator(formList);
    }

    @ApiOperation("实验室普通项目上报驳回")
    @PostMapping(value = "/rejectProjectReportByLabAdministrator")
    public Result rejectProjectReportByLabAdministrator(@Valid @RequestBody List<ProjectCheckForm> formList){
        return projectService.rejectProjectReportByLabAdministrator(formList);
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

    @ApiOperation("删除成员")
    @PostMapping("/deleteMemberFromProject")
    public Result deleteMemberFromProject(Long projectId,Long userId){
        return projectService.deleteMemberFromProject(projectId,userId);
    }

    @ApiOperation("将重点项目转换为普通项目")
    @PostMapping("changeKeyProjectToGeneral")
    public Result changeKeyProjectToGeneral(@Valid @RequestBody List<ProjectCheckForm> formList) {
        return projectService.changeKeyProjectToGeneral(formList);
    }

//    @ApiOperation("中期检查重点项目不合格")
//    @PostMapping("/midTermKeyProjectsFail")
//    public Result  midTermKeyProjectsFail(@Valid @RequestBody List<ProjectCheckForm> list){
//        return projectService.midTermKeyProjectsFail(list);
//    }

    @ApiOperation("中期检查项目退回")
    @PostMapping("/midTermKeyProjectHitBack")
    public Result  midTermKeyProjectHitBack(@Valid @RequestBody List<ProjectCheckForm> list){
        return projectService.midTermKeyProjectHitBack(list);
    }

    @ApiOperation("学院初审对普通项目给出评级")
    @PostMapping("/CollegeGivesRating")
    public Result CollegeGivesRating(@RequestBody @Valid List<ProjectGrade> projectGradeList){
        return projectService.CollegeGivesRating(projectGradeList);
    }

    @ApiOperation("学院初审打回")
    @PostMapping("/CollegeHitBack")
    public Result CollegeHitBack(@Valid @RequestBody List<ProjectCheckForm> list){
        return projectService.CollegeHitBack(list);
    }

    @ApiOperation("学院初审复核通过")
    @PostMapping("/CollegeReviewPassed")
    public Result CollegeReviewPassed(){
        return null;
    }

    @ApiOperation("学院初审不通过")
    @PostMapping("/CollegerejectToBeConcludingProject")
    public Result CollegerejectToBeConcludingProject(){
        return null;
    }

    @ApiIgnore("学院结题审核通过")
    @PostMapping("/collegePassedTheExamination")
    public Result agreeCollegePassedTheExamination(@Valid @RequestBody List<ProjectCheckForm> list){
        return projectService.agreeCollegePassedTheExamination(list);
    }
    @ApiOperation("职能部门不同意结题")
    @PostMapping(value = "/rejectToBeConcludingProject")
    public Result rejectToBeConcludingProject(@Valid @RequestBody List<ProjectCheckForm> list){
        return projectService.rejectToBeConcludingProject(list);
    }

}
