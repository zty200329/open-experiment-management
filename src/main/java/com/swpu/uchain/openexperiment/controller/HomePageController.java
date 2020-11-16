package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.form.announcement.*;
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
    @PostMapping("/getNewsById")
    public Result getNewsById(@RequestBody @Valid IdForm idForm){
        return announcementService.getNewsById(idForm);
    }

    @ApiOperation("根据主键id删除新闻")
    @PostMapping("/deleteNewsById")
    public Result deleteNewsById(@RequestBody @Valid IdForm idForm){
        return announcementService.deleteNewsById(idForm);
    }

    @ApiOperation("修改新闻状态为发布")
    @PostMapping("/updateToPublished")
    public Result updateToPublished(@RequestBody @Valid IdForm idForm){
        return announcementService.updateToPublished(idForm);
    }

    @ApiOperation("修改新闻状态为保存未发布")
    @PostMapping("/updateToSave")
    public Result updateToSave(@RequestBody @Valid IdForm idForm){
        return announcementService.updateToSave(idForm);
    }

    @ApiOperation("修改新闻内容")
    @PostMapping("/updateNewsContent")
    public Result updateNewsContent(@RequestBody @Valid UpdateNewsContentForm updateNewsContentForm){
        return announcementService.updateNewsContent(updateNewsContentForm);
    }

    @ApiOperation("发布成果展示")
    @PostMapping("/publishAchievementShow")
    public Result publishAchievementShow(@RequestBody @Valid HomepageAchievementForm homepageAchievementForm){
        return announcementService.publishAchievementShow(homepageAchievementForm);
    }
    @ApiOperation("获取轮播的")
    @GetMapping("/getTopPublishedAchievementShowList")
    public Result getTopPublishedAchievementShowList(){
        return announcementService.getTopPublishedAchievementShowList();
    }

    @ApiOperation("获取所有已发布成果  不用展示状态")
    @GetMapping("/getPublishedAchievementShowList")
    public Result getPublishedAchievementShowList(){
        return announcementService.getPublishedAchievementShowList();
    }

    @ApiOperation("获取所有成果  展示状态")
    @GetMapping("/getAllAchievementShowList")
    public Result getAllAchievementShowList(){
        return announcementService.getAllAchievementShowList();
    }

    @ApiOperation("获取一条成果详情")
    @PostMapping("/getAchievementById")
    public Result getAchievementById(@RequestBody @Valid IdForm idForm){
        return announcementService.getAchievementById(idForm);
    }
    @ApiOperation("取消轮播")
    @PostMapping("/deleteAchievementTopById")
    public Result deleteAchievementTopById(@RequestBody @Valid IdForm idForm){
        return announcementService.deleteAchievementTopById(idForm);
    }


    @ApiOperation("根据主键id删除成果")
    @PostMapping("/deleteAchievementById")
    public Result deleteAchievementById(@RequestBody @Valid IdForm idForm){
        return announcementService.deleteAchievementById(idForm);
    }

    @ApiOperation("修改成果展示状态为发布")
    @PostMapping("/updateAchievementToPublished")
    public Result updateAchievementToPublished(@RequestBody @Valid IdForm idForm){
        return announcementService.updateAchievementToPublished(idForm);
    }

    @ApiOperation("修改成果展示状态为保存未发布")
    @PostMapping("/updateAchievementToSave")
    public Result updateAchievementToSave(@RequestBody @Valid IdForm idForm){
        return announcementService.updateAchievementToSave(idForm);
    }

    @ApiOperation("修改成果展示")
    @PostMapping("/updateAchievementContent")
    public Result updateAchievementContent(@RequestBody @Valid UpdateAchievementContentForm updateNewsContentForm){
        return announcementService.updateAchievementContent(updateNewsContentForm);
    }



    @ApiOperation("首页发布公告")
    @PostMapping("/homePagePublishAnnouncement")
    public Result homePagePublishAnnouncement(@RequestBody @Valid HomePageNewsPublishForm homePageNewsPublishForm){
        return announcementService.homePagePublishAnnouncement(homePageNewsPublishForm);
    }

    @ApiOperation("获取所有已发布公告  不用展示状态")
    @GetMapping("/getHomePageAnnouncementList")
    public Result getHomePageAnnouncementList(){
        return announcementService.getHomePageAnnouncementList();
    }

    @ApiOperation("获取所有公告 用于后台展示 要展示状态")
    @GetMapping("/getAllAnnouncementList")
    public Result getAllAnnouncementList(){
        return announcementService.getAllAnnouncementList();
    }

    @ApiOperation("获取一条公告详情")
    @PostMapping("/getAnnouncementById")
    public Result getAnnouncementById(@RequestBody @Valid IdForm idForm){
        return announcementService.getAnnouncementById(idForm);
    }

    @ApiOperation("根据主键id删除公告")
    @PostMapping("/deleteAnnouncementById")
    public Result deleteAnnouncementById(@RequestBody @Valid IdForm idForm){
        return announcementService.deleteAnnouncementById(idForm);
    }

    @ApiOperation("修改公告状态为发布")
    @PostMapping("/updateAnnouncementToPublished")
    public Result updateAnnouncementToPublished(@RequestBody @Valid IdForm idForm){
        return announcementService.updateAnnouncementToPublished(idForm);
    }

    @ApiOperation("修改公告状态为保存未发布")
    @PostMapping("/updateAnnouncementToSave")
    public Result updateAnnouncementToSave(@RequestBody @Valid IdForm idForm){
        return announcementService.updateAnnouncementToSave(idForm);
    }

    @ApiOperation("修改新闻内容")
    @PostMapping("/updateAnnouncementContent")
    public Result updateAnnouncementContent(@RequestBody @Valid UpdateNewsContentForm updateNewsContentForm){
        return announcementService.updateAnnouncementContent(updateNewsContentForm);
    }
}
