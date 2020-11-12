package com.swpu.uchain.openexperiment.service;

import com.swpu.uchain.openexperiment.domain.Announcement;
import com.swpu.uchain.openexperiment.form.announcement.AnnouncementPublishForm;
import com.swpu.uchain.openexperiment.form.announcement.AnnouncementUpdateForm;
import com.swpu.uchain.openexperiment.form.announcement.HomePageNewsPublishForm;
import com.swpu.uchain.openexperiment.form.announcement.QueryCondition;
import com.swpu.uchain.openexperiment.result.Result;
import io.swagger.models.auth.In;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;

/**
 * @Author: clf
 * @Date: 19-1-25
 * @Description:
 * 公告模块
 */
public interface AnnouncementService {
    /**
     * 添加新的公告
     * @param announcement
     * @return
     */
    boolean insertAndPublish(Announcement announcement);

    /**
     * 更新公告信息
     * @param announcement
     * @return
     */
    boolean update(Announcement announcement);

    /**
     * 删除公告
     * @param id
     */
    Integer delete(Long id);

    /**
     * 发布公告
     * @param publishForm
     * @return
     */
    Result publishAnnouncement(AnnouncementPublishForm publishForm);

    /**
     * 发布新闻
     * @param homePageNewsPublishForm
     * @return
     */
    Result homePagePublishNews(HomePageNewsPublishForm homePageNewsPublishForm);

    /**
     * 返回所有已发布首页新闻
     * @return
     */
    Result getHomePageNewsList();

    /**
     * 查看公告详情
     * @param announcementId
     * @return
     */
    Result readAnnouncementDetail(Long announcementId);


    /**
     * 获取公告指定分页数据
     * @param
     * @return
     */
    Result getList();

    Result changeInfo(AnnouncementUpdateForm updateForm);

    Result createAndSave(AnnouncementPublishForm publishForm);

    Result publishSavedAnnouncement(Long announcementId);

    Result cancelPublish(Long announcementId);

    Result queryByCondition(QueryCondition condition);
}
