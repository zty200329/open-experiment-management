package com.swpu.uchain.openexperiment.controller;

import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.form.announcement.AnnouncementPublishForm;
import com.swpu.uchain.openexperiment.form.announcement.AnnouncementUpdateForm;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.AnnouncementService;
import io.swagger.annotations.Api;
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

    @PostMapping(value = "/publish", name = "发布公告")
    public Object publish(@Valid AnnouncementPublishForm publishForm){
        return announcementService.publishAnnouncement(publishForm);
    }
    @GetMapping(value = "/readDetail", name = "阅读公告详情")
    public Object readDetail(Long announcementId){
        if (announcementId == null){
            return Result.error(CodeMsg.PARAM_CANT_BE_NULL);
        }
        return announcementService.readAnnouncementDetail(announcementId);
    }

    @GetMapping(value = "/list", name = "公告列表")
    public Object list(Integer pageNum){
        if (pageNum == null){
            return Result.error(CodeMsg.PARAM_CANT_BE_NULL);
        }
        return announcementService.getList(pageNum);
    }

    @PostMapping(value = "/delete", name = "删除公告")
    public Object delete(Long announcementId){
        announcementService.delete(announcementId);
        return Result.success(announcementId);
    }

    @PostMapping(value = "/update", name = "修改公告")
    public Object update(@Valid AnnouncementUpdateForm updateForm){
        return announcementService.changeInfo(updateForm);
    }

}
