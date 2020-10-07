package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.form.query.HistoryQueryKeyProjectInfo;
import com.swpu.uchain.openexperiment.form.query.QueryConditionForm;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.KeyProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author dengg
 */
@CrossOrigin
@RestController
@Api(tags = "项目（重点）模块查询接口")
@RequestMapping("/api/project")
public class KeyProjectQueryController {

    private KeyProjectService keyProjectService;

    @Autowired
    public KeyProjectQueryController(KeyProjectService keyProjectService) {
        this.keyProjectService = keyProjectService;
    }

    @GetMapping(value = "/getKeyProjectApplyingListByGuideTeacher", name = "获取当前用户（限老师身份）指导项目的申请参加列表")
    @ApiOperation("指导教师获取待审核的重点项目")
    public Result getKeyProjectApplyingListByGuideTeacher(){
        return keyProjectService.getKeyProjectApplyingListByGuideTeacher();
    }

    @GetMapping(value = "/getKeyProjectApplyingListByLabAdmin")
    @ApiOperation("实验室主任获取项目待审核的重点项目信息")
    public Result getKeyProjectApplyingListByLabAdmin(){
        return keyProjectService.getKeyProjectApplyingListByLabAdmin();
    }

    @GetMapping(value = "/getKeyProjectApplyingListBySecondaryUnit")
    @ApiOperation("二级单位获取项目待审核的重点项目信息")
    public Result getKeyProjectApplyingListBySecondaryUnit(){
        return keyProjectService.getKeyProjectApplyingListBySecondaryUnit();
    }

    @GetMapping(value = "/getToBeReviewedProject")
    @ApiOperation("获取待立项评审的项目")
    public Result getToBeReviewedProject(){
        return keyProjectService.getToBeReviewedProject();
    }

    @GetMapping(value = "/getKeyProjectApplyingListByFunctionalDepartment")
    @ApiOperation("职能部门获取项目待审核的重点项目信息")
    public Result getKeyProjectApplyingListByFunctionalDepartment(){
        return keyProjectService.getKeyProjectApplyingListByFunctionalDepartment();
    }

    @ApiOperation("职能部门获取待中期检查的项目 -- 参数可不传 (接口正确)")
    @GetMapping(value = "getIntermediateInspectionKeyProject")
    public Result getIntermediateInspectionKeyProject (Integer college){
        return keyProjectService.getIntermediateInspectionKeyProject(college);
    }


    @GetMapping(value = "/getToBeReportedKeyProjectByLabAdmin")
    @ApiOperation("实验室主任获取待上报的项目")
    public Result getToBeReportedProjectByLabAdmin(){
        return keyProjectService.getToBeReportedProjectByLabAdmin();
    }

    @GetMapping(value = "/getToBeReportedKeyProjectBySecondaryUnit")
    @ApiOperation("二级单位获取待上报的项目")
    public Result getToBeReportedProjectBySecondaryUnit(){
        return keyProjectService.getToBeReportedProjectBySecondaryUnit ();
    }

    @GetMapping(value = "/getToReviewKeyProject")
    @ApiOperation("获取待立项评审的重点项目")
    public Result getToReviewKeyProject(){
        return keyProjectService.getToReviewKeyProject ();
    }

    @ApiOperation("重点项目历史查询")
    @PostMapping(value = "/getHistoricalKeyProjectInfo")
    public Result getHistoricalKeyProjectInfo(@Valid @RequestBody HistoryQueryKeyProjectInfo info){
        return keyProjectService.getHistoricalKeyProjectInfo(info);
    }

    @ApiOperation("通过项目ID查看项目进度信息")
    @GetMapping("/getKeyProjectDetailById")
    public Result getKeyProjectDetailById(Long projectId){
        return keyProjectService.getKeyProjectDetailById(projectId);
    }

    @ApiOperation("重点项目条件查询")
    @PostMapping("/conditionallyQueryOfKeyProject")
    public Result conditionallyQueryOfKeyProject(@RequestBody @Valid QueryConditionForm form){
        return keyProjectService.conditionallyQueryOfKeyProject(form);
    }

    @ApiOperation("查看中期打回的重点项目列表")
    @PostMapping("/getKeyProjectMidTermReturnProject")
    public Result getMidTermReturnProject(){
        return keyProjectService.getMidTermReturnProject();
    }

    @ApiOperation("学院获取待检查的项目")
    @GetMapping(value = "/getCollegeKeyProject")
    public Result getCollegeKeyProject (){
        return keyProjectService.getCollegeKeyProject();
    }

    @ApiOperation("学院获取学院结题通过了的")
    @GetMapping("/getTheCollegeHasCompletedKeyProject")
    public Result getTheCollegeHasCompletedKeyProject(){
        return keyProjectService.getTheCollegeHasCompletedKeyProject();
    }

    @ApiOperation("学院获取职能部门结题通过了的")
    @GetMapping("/getTheSchoolHasCompletedKeyProject")
    public Result getTheSchoolHasCompletedKeyProject(){
        return keyProjectService.getTheSchoolHasCompletedKeyProject();
    }

    @ApiOperation("职能部门获取待结题检查的项目 -- 参数可不传")
    @GetMapping(value = "getToBeConcludingKeyProject")
    public Result getToBeConcludingKeyProject (Integer college){
        return keyProjectService.getToBeConcludingKeyProject(college);
    }

    @ApiOperation("职能部门获取通过的项目 -- 参数可不传")
    @GetMapping(value = "getCompleteKeyProject")
    public Result getCompleteKeyProject (Integer college){
        return keyProjectService.getCompleteKeyProject(college);
    }

    @ApiOperation("学院获取结题打回的重点项目列表")
    @GetMapping(value = "/getCollegeReturnKeyProject")
    public Result getCollegeReturnKeyProject(){
        return keyProjectService.getCollegeReturnKeyProject();
    }

    @ApiOperation("职能部门结题打回的重点项目列表 ----- 参数可不传")
    @GetMapping(value = "/getFunctionReturnKeyProject")
    public Result getFunctionReturnKeyProject (Integer college){
        return keyProjectService.getFunctionReturnKeyProject(college);
    }

}
