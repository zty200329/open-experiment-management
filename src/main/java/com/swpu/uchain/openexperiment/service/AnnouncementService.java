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

    /**
     * 发布成果展示
     * @param homepageAchievementForm
     * @return
     */
    Result publishAchievementShow(HomepageAchievementForm homepageAchievementForm);

    /**
     * 获取轮播
     * @return
     */
    Result getTopPublishedAchievementShowList();
    /**
     * 获取所有已发布的成果  不要展示状态
     * @return
     */
    Result getPublishedAchievementShowList();

    /**
     * 获取所有的 展示状态
     * @return
     */
    Result getAllAchievementShowList();

    /**
     * 获取成果详情
     * @param idForm
     * @return
     */
    Result getAchievementById(IdForm idForm);

    /**
     * 根据主键删除
     * @param idForm
     * @return
     */
    Result deleteAchievementById(IdForm idForm);

    /**
     * 取消轮播
     *
     * @param idForm
     * @return
     */
    Result deleteAchievementTopById(IdForm idForm);
    /**
     * 修改成果展示状态为发布
     * @param idForm
     * @return
     */
    Result updateAchievementToPublished(IdForm idForm);

    /**
     * 修改成果展示状态为保存未发布
     * @param idForm
     * @return
     */
    Result updateAchievementToSave(IdForm idForm);

    /**
     * 修改成果展示
     * @param updateNewsContentForm
     * @return
     */
    Result updateAchievementContent(UpdateAchievementContentForm updateNewsContentForm);



    /**
     * 发布公告
     * @param homePageNewsPublishForm
     * @return
     */
    Result homePagePublishAnnouncement(HomePageNewsPublishForm homePageNewsPublishForm);

    /**
     * 返回所有已发布首页公告
     * @return
     */
    Result getHomePageAnnouncementList();

    /**
     * 获取一条公告详情
     * @param idForm
     * @return
     */
    Result getAnnouncementById(IdForm idForm);

    /**
     * 更新状态为已发布
     * @param idForm
     * @return
     */
    Result updateAnnouncementToPublished(IdForm idForm);


    /**
     * 更新状态为已发布
     * @param idForm
     * @return
     */
    Result updateAnnouncementToSave(IdForm idForm);

    /**
     * 根据主键id删除
     * @param idForm
     * @return
     */
    Result deleteAnnouncementById(IdForm idForm);

    /**
     * 获取所有新闻
     * @return
     */
    Result getAllAnnouncementList();
    /**
     * 修改文章内容
     * @param updateNewsContentForm
     * @return
     */
    Result updateAnnouncementContent(UpdateNewsContentForm updateNewsContentForm);
}
