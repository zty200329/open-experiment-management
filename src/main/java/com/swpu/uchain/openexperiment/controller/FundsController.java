package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.domain.Funds;
import com.swpu.uchain.openexperiment.form.funds.FundSetForm;
import com.swpu.uchain.openexperiment.form.funds.FundsForm;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.FundsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author dengg
 */
@RestController
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

    @ApiOperation("设置项目资金")
    @PostMapping("/setProjectSupportAmount")
    public Result setProjectSupportAmount(@Valid @RequestBody FundSetForm fundSetForm){
        return fundsService.setProjectSupportAmount(fundSetForm);
    }

}
