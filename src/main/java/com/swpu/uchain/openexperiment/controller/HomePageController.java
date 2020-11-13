package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.form.announcement.HomePageNewsPublishForm;
import com.swpu.uchain.openexperiment.form.announcement.IdForm;
import com.swpu.uchain.openexperiment.form.announcement.UpdateNewsContentForm;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.AnnouncementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author zty200329
 * @version 1.0
 * @date 2020/11/13 5:08 下午
 */
@CrossOrigin
@RestController
@RequestMapping("/api/homePage")
@Api(tags = "首页模块接口")
public class HomePageController {
    @Autowired
    private AnnouncementService announcementService;

    @ApiOperation("首页发布新闻")
    @PostMapping("/homePagePublishNews")
    public Result homePagePublishNews(@RequestBody @Valid HomePageNewsPublishForm homePageNewsPublishForm){
        return announcementService.homePagePublishNews(homePageNewsPublishForm);
    }

    @ApiOperation("获取所有已发布新闻  不用展示状态")
    @GetMapping("/getHomePageNewsList")
    public Result getHomePageNewsList(){
        return announcementService.getHomePageNewsList();
    }

    @ApiOperation("获取所有新闻 用于后台展示 要展示状态")
    @GetMapping("/getAllNewsList")
    public Result getAllNewsList(){
        return announcementService.getAllNewsList();
    }

    @ApiOperation("获取一条新闻详情")
    @GetMapping("/getNewsById")
    public Result getNewsById(@RequestBody @Valid IdForm idForm){
        return announcementService.getNewsById(idForm);
    }

    @ApiOperation("根据主键id删除")
    @GetMapping("/deleteNewsById")
    public Result deleteNewsById(@RequestBody @Valid IdForm idForm){
        return announcementService.deleteNewsById(idForm);
    }

    @ApiOperation("修改新闻状态为发布")
    @GetMapping("/updateToPublished")
    public Result updateToPublished(@RequestBody @Valid IdForm idForm){
        return announcementService.updateToPublished(idForm);
    }

    @ApiOperation("修改新闻状态为保存未发布")
    @GetMapping("/updateToSave")
    public Result updateToSave(@RequestBody @Valid IdForm idForm){
        return announcementService.updateToSave(idForm);
    }

    @ApiOperation("修改新闻内容")
    @PostMapping("/updateNewsContent")
    public Result updateNewsContent(@RequestBody @Valid UpdateNewsContentForm updateNewsContentForm){
        return announcementService.updateNewsContent(updateNewsContentForm);
    }

}
