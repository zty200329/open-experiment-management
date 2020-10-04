package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.form.amount.AmountLimitForm;
import com.swpu.uchain.openexperiment.form.amount.AmountSearchForm;
import com.swpu.uchain.openexperiment.form.amount.AmountUpdateForm;
import com.swpu.uchain.openexperiment.form.amount.ProjectApplyAmountLimitForm;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.AmountLimitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author zty
 */
@CrossOrigin
@RestController
@RequestMapping("/api/amount")
@Api(tags = "项目数量限制模块")
public class AmountLimitController {

    private AmountLimitService amountLimitService;

    @Autowired
    public AmountLimitController(AmountLimitService amountLimitService) {
        this.amountLimitService = amountLimitService;
    }

    @ApiOperation("设置项目申请数量限制")
    @PostMapping("/setApplyLimitAmount")
    public Result setApplyLimitAmount(@RequestBody @Valid ProjectApplyAmountLimitForm form) {
        return amountLimitService.setApplyLimitAmount(form);
    }

    @ApiOperation("更新教师项目申请数量限制")
    @PostMapping("/updateApplyLimitAmount")
    public Result updateApplyLimitAmount(@RequestBody @Valid ProjectApplyAmountLimitForm form) {
        return amountLimitService.updateApplyLimitAmount(form);
    }

    @ApiOperation("获取教师项目申请数量限制")
    @GetMapping("/getProjectApplyAmountLimit")
    public Result getProjectApplyAmountLimit() {
        return amountLimitService.getProjectApplyAmountLimit();
    }


    @PostMapping("/setAmount")
    @ApiOperation("设置项目数量")
    public Result setAmount(@Valid @RequestBody List<AmountLimitForm> limitForms){
        return amountLimitService.setAmount(limitForms);
    }

    @GetMapping("getAmountLimitVOListByCollegeAndProjectType")
    @ApiOperation("获取项目数量限制")
    public Result getAmountLimitVOListByCollegeAndProjectType(AmountSearchForm form){
        return amountLimitService.getAmountLimitVOListByCollegeAndProjectType(form);
    }

    @ApiOperation("更新项目数量限制")
    @PostMapping("/updateAmountLimit")
    public Result updateAmountLimit(@Valid @RequestBody AmountUpdateForm form){
        return amountLimitService.updateAmountLimit(form);
    }


    @ApiOperation("获取所有学院的项目限制信息")
    @GetMapping("/getAmountLimitList")
    public Result getAmountLimitList(){
        return amountLimitService.getAmountLimitList();
    }

}
