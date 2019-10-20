package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.form.project.KeyProjectApplyForm;
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
@RequestMapping("/project")
@Api(tags = "重点项目模块执行接口")
public class KeyProjectInvokeController {

    private KeyProjectService keyProjectService;


    @Autowired
    public KeyProjectInvokeController(KeyProjectService keyProjectService) {
        this.keyProjectService = keyProjectService;
    }

    @ApiOperation("重点项目申请接口")
    @PostMapping(value = "/createKeyApply")
    public Result createKeyApply(@Valid @RequestBody KeyProjectApplyForm form){
        return keyProjectService.createKeyApply(form);
    }

}
