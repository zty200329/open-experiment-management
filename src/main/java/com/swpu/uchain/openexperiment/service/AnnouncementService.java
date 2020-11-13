package com.swpu.uchain.openexperiment.service;

import com.swpu.uchain.openexperiment.domain.Announcement;
import com.swpu.uchain.openexperiment.form.announcement.*;
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
     * 获取一条新闻详情
     * @param idForm
     * @return
     */
    Result getNewsById(IdForm idForm);

    /**
     * 更新状态为已发布
     * @param idForm
     * @return
     */
    Result updateToPublished(IdForm idForm);


    /**
     * 更新状态为已发布
     * @param idForm
     * @return
     */
    Result updateToSave(IdForm idForm);

    /**
     * 根据主键id删除
     * @param idForm
     * @return
     */
    Result deleteNewsById(IdForm idForm);

    /**
     * 获取所有新闻
     * @return
     */
    Result getAllNewsList();

    /**
     * 查看公告详情
     * @param announcementId
     * @return
     */
    Result readAnnouncementDetail(Long announcementId);


    /**
     * 修改文章内容
     * @param updateNewsContentForm
     * @return
     */
    Result updateNewsContent(UpdateNewsContentForm updateNewsContentForm);

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
