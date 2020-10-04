package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.form.reviews.NeedProjectReviewForm;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.NeedProjectReviewService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author zty200329
 * @date 2020/10/4 15:51
 * @describe:
 */
@CrossOrigin
@RestController
@RequestMapping("/api/collegeReview")
@Api(tags = "立项评审")
public class NeedProjectReviewController {

    @Autowired
    private NeedProjectReviewService needProjectReviewService;

    @ApiOperation("设置哪些学院需要立项审核")
    @PostMapping(value = "/collegeSetUpReview")
    public Result collegeSetUpReview(@Valid @RequestBody List<NeedProjectReviewForm> projectReviewForms){
        return needProjectReviewService.collegeSetUpReview(projectReviewForms);
    }

}
