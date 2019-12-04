package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.domain.Funds;
import com.swpu.uchain.openexperiment.form.funds.FundSetForm;
import com.swpu.uchain.openexperiment.form.funds.FundsForm;
import com.swpu.uchain.openexperiment.form.funds.FundsUpdateForm;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.FundsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author dengg
 */
@Api(tags = "资金管理接口")
@RestController
@CrossOrigin
@RequestMapping("/api/funds")
public class FundsController {

    private FundsService fundsService;

    @Autowired
    public FundsController(FundsService fundsService) {
        this.fundsService = fundsService;
    }

    @ApiOperation("资金报账(职能部门使用)")
    @PostMapping("/cashReimbursement")
    public Result cashReimbursement(@Valid @RequestBody FundsForm form){
        return fundsService.cashReimbursement(form);
    }


    @ApiOperation("二级单位修改项目金额")
    @PostMapping(value = "/updateProjectApplyFundsBySecondaryUnit")
    public Result updateProjectApplyFundsBySecondaryUnit(@Valid @RequestBody FundsUpdateForm form){
        return fundsService.updateProjectApplyFundsBySecondaryUnit(form);
    }


}
