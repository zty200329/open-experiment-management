package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.form.amount.AmountLimitForm;
import com.swpu.uchain.openexperiment.form.amount.AmountSearchForm;
import com.swpu.uchain.openexperiment.form.amount.AmountUpdateForm;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.AmountLimitService;
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
@RequestMapping("/amount")
@Api(tags = "项目数量限制模块")
public class AmountLimitController {

    private AmountLimitService amountLimitService;

    @Autowired
    public AmountLimitController(AmountLimitService amountLimitService) {
        this.amountLimitService = amountLimitService;
    }

    @PostMapping("/setAmount")
    @ApiOperation("设置项目数量")
    public Result setAmount(@Valid @RequestBody AmountLimitForm form){
        return amountLimitService.setAmount(form);
    }

    @GetMapping("getAmountLimitVOByCollegeAndProjectType")
    @ApiOperation("获取项目数量限制")
    public Result getAmountLimitVOByCollegeAndProjectType(AmountSearchForm form){
        return amountLimitService.getAmountLimitVOByCollegeAndProjectType(form);
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
