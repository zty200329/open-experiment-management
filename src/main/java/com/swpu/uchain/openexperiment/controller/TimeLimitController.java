package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.form.time.TimeLimitForm;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.TimeLimitService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author dengg
 */
@RestController
@CrossOrigin
@RequestMapping("/api/timeLimit")
@Api(tags = "时间限制设置接口")
public class TimeLimitController {

    private TimeLimitService timeLimitService;

    @Autowired
    public TimeLimitController(TimeLimitService timeLimitService) {
        this.timeLimitService = timeLimitService;
    }

    @PostMapping("/setTimeLimit")
    @ApiOperation("设置时间限制")
    public Result setTimeLimit(@RequestBody @Valid TimeLimitForm timeLimitForm){
        return timeLimitService.insert(timeLimitForm);
    }

    @PostMapping("/updateTimeLimit")
    @ApiOperation("更新时间限制")
    public Result updateTimeLimit(@RequestBody @Valid TimeLimitForm timeLimitForm){
        return timeLimitService.update(timeLimitForm);
    }

    @GetMapping("/deleteTimeLimit")
    @ApiOperation("删除时间限制")
    public Result deleteTimeLimit(Integer type){
        return timeLimitService.delete(type);
    }

    @GetMapping("/getTimeLimit")
    @ApiOperation("获取时间限制")
    public Result getTimeLimit(){
        return timeLimitService.getTimeLimitList();
    }

}
