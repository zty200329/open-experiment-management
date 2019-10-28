package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import com.swpu.uchain.openexperiment.form.announcement.AnnouncementPublishForm;
import com.swpu.uchain.openexperiment.form.announcement.AnnouncementUpdateForm;
import com.swpu.uchain.openexperiment.form.announcement.QueryCondition;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.AnnouncementService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jdk.nashorn.internal.objects.annotations.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @Author: clf
 * @Date: 19-1-25
 * @Description:
 * 公告接口
 */
@CrossOrigin
@RestController
@RequestMapping("/announcement")
@Api(tags = "公告模块接口")
public class AnnouncementController {
    @Autowired
    private AnnouncementService announcementService;



    @ApiOperation("发布公告")
    @PostMapping(value = "/publish", name = "发布公告")
    public Result publish(@RequestBody @Valid AnnouncementPublishForm publishForm){
        return announcementService.publishAnnouncement(publishForm);
    }

    @ApiOperation("阅读公告详情")
    @GetMapping(value = "/readDetail", name = "阅读公告详情")
    public Result readDetail(Long announcementId){
        if (announcementId == null){
            return Result.error(CodeMsg.PARAM_CANT_BE_NULL);
        }
        return announcementService.readAnnouncementDetail(announcementId);
    }

    @ApiOperation("公告列表")
    @GetMapping(value = "/list", name = "公告列表")
    public Result list(){
        return announcementService.getList();
    }

    @ApiOperation("根据条件查询公告")
    @PostMapping(value = "/queryByCondition")
    public Result queryByCondition(@RequestBody @Valid QueryCondition condition){
        return announcementService.queryByCondition(condition);
    }

    @ApiOperation("删除公告")
    @PostMapping(value = "/delete", name = "删除公告")
    public Result<Long> delete(Long announcementId){
        announcementService.delete(announcementId);
        return Result.success(announcementId);
    }

    @ApiOperation("修改公告")
    @PostMapping(value = "/update", name = "修改公告")
    public Result update(@Valid @RequestBody AnnouncementUpdateForm updateForm){
        return announcementService.changeInfo(updateForm);
    }

    @ApiOperation("创建并保存公告")
    @PostMapping("/createAndSave")
    public Result createAndSave(@RequestBody @Valid AnnouncementPublishForm publishForm){
        return announcementService.createAndSave(publishForm);
    }


    @ApiOperation("发布未发布的公告")
    @GetMapping("/publishSavedAnnouncement")
    public Result publishSavedAnnouncement(Long announcementId){
        if (announcementId == null){
            throw new GlobalException(CodeMsg.PARAM_CANT_BE_NULL);
        }
        return announcementService.publishSavedAnnouncement(announcementId);
    }

    @ApiOperation("取消发布公告")
    @GetMapping("/cancelPublish")
    public Result cancelPublish(Long announcementId){
        if (announcementId == null){
            throw new GlobalException(CodeMsg.PARAM_CANT_BE_NULL);
        }
        return announcementService.cancelPublish(announcementId);
    }

}
