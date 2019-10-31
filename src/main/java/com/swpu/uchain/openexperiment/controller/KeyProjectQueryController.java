package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.form.query.HistoryQueryKeyProjectInfo;
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
@RequestMapping("/project")
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

    @GetMapping(value = "/getKeyProjectApplyingListByFunctionalDepartment")
    @ApiOperation("职能部门获取项目待审核的重点项目信息")
    public Result getKeyProjectApplyingListByFunctionalDepartment(){
        return keyProjectService.getKeyProjectApplyingListByFunctionalDepartment();
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


}
