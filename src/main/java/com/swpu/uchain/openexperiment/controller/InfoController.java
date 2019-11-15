package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.result.Result;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dengg
 */
@Api(tags = "统计信息接口")
@RestController
@CrossOrigin
@RequestMapping("/info")
public class InfoController {

    @GetMapping("/get")
    public Result getStatisticsInfo() {
        return null;
    }

}
