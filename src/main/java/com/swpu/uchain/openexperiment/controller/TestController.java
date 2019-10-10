package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.dao.ProjectGroupMapper;
import com.swpu.uchain.openexperiment.domain.ProjectGroup;
import com.swpu.uchain.openexperiment.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author panghu
 */
@Api
@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private ProjectGroupMapper projectGroupMapper;

    @GetMapping("/getProjectGroupDetailVOByProjectId")
    public Object getProjectGroupDetailVOByProjectId(){
        return Result.success(projectGroupMapper.getProjectGroupDetailVOByProjectId(1L));
    }

}
