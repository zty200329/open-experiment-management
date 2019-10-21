package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.KeyProjectService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @ApiOperation("实验室主任获取项目待审核的重点项目信息")
    public Result getKeyProjectApplyingListBySecondaryUnit(){
        return keyProjectService.getKeyProjectApplyingListBySecondaryUnit();
    }

    @GetMapping(value = "/getKeyProjectApplyingListByFunctionalDepartment")
    @ApiOperation("实验室主任获取项目待审核的重点项目信息")
    public Result getKeyProjectApplyingListByFunctionalDepartment(){
        return keyProjectService.getKeyProjectApplyingListByFunctionalDepartment();
    }

}
