package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.VO.project.SelectByKeywordProjectVO;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.form.member.MemberQueryCondition;
import com.swpu.uchain.openexperiment.form.project.SelectByKeywordForm;
import com.swpu.uchain.openexperiment.form.query.HistoryQueryProjectInfo;
import com.swpu.uchain.openexperiment.form.query.QueryConditionForm;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.ProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.apache.regexp.RE;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * 项目模块查询接口
 * @author panghu
 */
@CrossOrigin
@RestController
@Api(tags = "项目(普通)模块查询接口")
@RequestMapping("/api/project")
public class ProjectQueryController {

    private ProjectService projectService;

    @Autowired
    public ProjectQueryController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @ApiOperation("学生根据条件查询可加入的项目信息")
    @PostMapping("/getAllOpenTopicByStudentByCondition")
    public Result getAllOpenTopicByCondition(@RequestBody QueryConditionForm queryConditionForm){
        return projectService.getAllOpenTopicByCondition(queryConditionForm);
    }

    @ApiOperation("职能部门查看内定项目")
    @PostMapping("/getFunctionCreateCommonApply")
    public Result getFunctionCreateCommonApply(){
        return projectService.getFunctionCreateCommonApply();
    }

    @ApiOperation("学生获取可参与的开放性选题")
    @GetMapping("/getAllOpenTopicByStudent")
    public Result getAllOpenTopic(){
        return projectService.getAllOpenTopic();
    }

    @ApiOperation("获取项目的立项信息--可使用")
    @GetMapping(value = "/getApplyInfo2", name = "获取项目的立项信息")
    public Result getApplyInfo(Long projectGroupId){
        return projectService.getApplyForm(projectGroupId);
    }

    @GetMapping(value = "/getApplyingJoinInfo", name = "获取当前用户（限老师身份）指导项目的申请参加列表")
    @ApiOperation("获取当前用户（限老师身份）指导项目的申请参加列表--可使用")
    public Result getApplyingJoinInfo(){
        return Result.success(projectService.getJoinInfo());
    }

    @ApiOperation("根据条件查询项目成员申请信息")
    @PostMapping(value = "/getApplyingJoinInfoByCondition")
    public Result getApplyingJoinInfoByCondition(@RequestBody @Valid MemberQueryCondition condition){
        return projectService.getApplyingJoinInfoByCondition(condition);
    }

    @ApiOperation("获取当前用户参与的某状态的项目信息, 项目状态: 不传(所有), 0(申报), 1(立项), 2(驳回修改),3(已上报学院领导), 4(中期检查), 5(结项)   (接口正确)")
    @GetMapping(value = "/getOwnProjects", name = "获取自己相关的项目信息")
    public Result getOwnProjects(@RequestParam(required = false) Integer projectStatus,@RequestParam(required = false,value = "joinStatus") Integer joinStatus){
        return projectService.getCurrentUserProjects(projectStatus,joinStatus);
    }

    @ApiOperation("通过项目ID查看项目详情--项目进度信息")
    @GetMapping("/getProjectDetailById")
    public Result getProjectDetailById(Long projectId){
        return projectService.getProjectDetailById(projectId);
    }

    @ApiOperation("实验室获取待拟题的项目")
    @GetMapping(value = "getPendingApprovalProjectByLabAdministrator")
    public Result getPendingApprovalProjectByLabAdministrator (){
        return projectService.getPendingApprovalProjectByLabAdministrator();
    }

    @ApiOperation("二级单位获取待立项审核的项目")
    @GetMapping(value = "getPendingApprovalProjectBySecondaryUnit")
    public Result getPendingApprovalProjectBySecondaryUnit (){
        return projectService.getPendingApprovalProjectBySecondaryUnit();
    }

    @ApiOperation("职能部门获取待立项审核的项目")
    @GetMapping(value = "getPendingApprovalProjectByFunctionalDepartment")
    public Result getPendingApprovalProjectByFunctionalDepartment (){
        return projectService.getPendingApprovalProjectByFunctionalDepartment();
    }

    @ApiOperation("职能部门获取待中期检查的项目")
    @GetMapping(value = "getIntermediateInspectionProject")
    public Result getIntermediateInspectionProject (){
        return projectService.getIntermediateInspectionProject();
    }

    @ApiOperation("职能部门根据关键字查找普通项目")
    @PostMapping("/selectByKeyword")
    public Result selectByKeyword(@RequestBody SelectByKeywordForm Keyword){
        return projectService.selectByKeyword(Keyword.getKeyword());
    }

    @ApiOperation("职能部门根据关键字查找重点项目")
    @PostMapping("/selectKeyProjectByKeyword")
    public Result selectKeyProjectByKeyword(@RequestBody SelectByKeywordForm Keyword){
        return projectService.selectKeyProjectByKeyword(Keyword);
    }

    @ApiOperation("职能部门获取待结题检查的项目")
    @GetMapping(value = "getToBeConcludingProject")
    public Result getToBeConcludingProject (){
        return projectService.getToBeConcludingProject();
    }

    @ApiOperation("根据项目名模糊查询项目--可使用")
    @GetMapping(value = "/selectProject", name = "根据项目名模糊查询项目--可使用")
    public Result selectProject(String name){
        if (StringUtils.isEmpty(name)){
            return Result.error(CodeMsg.PARAM_CANT_BE_NULL);
        }
        return Result.success(projectService.selectByProjectName(name));
    }

    @ApiOperation("通过项目ID查看项目详情--成员和项目信息")
    @GetMapping("/getApplyInfo")
    public Result getProjectGroupDetailByProjectId(@RequestParam("id") Long projectId){
        return projectService.getProjectGroupDetailVOByProjectId(projectId);
    }

    @ApiOperation("二级单位查看待上报的项目")
    @GetMapping("/getToBeReportedProjectBySecondaryUnit")
    public Result getToBeReportedProjectBySecondaryUnit(){
        return projectService.getToBeReportedProjectBySecondaryUnit();
    }

    @ApiOperation("实验室主任查看待普通项目审批项目")
    @GetMapping("/getToBeReportedProjectByLabLeader")
    public Result getToBeReportedProjectByLabLeader(){
        return projectService.getToBeReportedProjectByLabLeader();
    }

    @ApiOperation("职能部门根据条件查询项目信息")
    @PostMapping("/conditionallyQueryOfProject")
    public Result conditionallyQueryOfProject(@RequestBody QueryConditionForm form){
        return projectService.conditionallyQueryOfProject(form);
    }

    @ApiOperation("所有的单位查看历史的操作--已上报，已驳回")
    @PostMapping("/getHistoricalProjectInfoByUnitAndOperation")
    public Result getMemberAmountOfProject(@Valid @RequestBody HistoryQueryProjectInfo info ){
        return projectService.getHistoricalProjectInfo(info);
    }

    @ApiOperation("查看中期打回的项目列表")
    @PostMapping("/getMidTermReturnProject")
    public Result getMidTermReturnProject(){
        return projectService.getMidTermReturnProject();
    }

}
