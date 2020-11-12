package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.VO.announcement.AnnouncementListVO;
import com.swpu.uchain.openexperiment.VO.announcement.AnnouncementVO;
import com.swpu.uchain.openexperiment.domain.NewsRelease;
import com.swpu.uchain.openexperiment.form.announcement.HomePageNewsPublishForm;
import com.swpu.uchain.openexperiment.mapper.AnnouncementMapper;
import com.swpu.uchain.openexperiment.mapper.NewsReleaseMapper;
import com.swpu.uchain.openexperiment.mapper.UserMapper;
import com.swpu.uchain.openexperiment.domain.Announcement;
import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.enums.AnnouncementStatus;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import com.swpu.uchain.openexperiment.form.announcement.AnnouncementPublishForm;
import com.swpu.uchain.openexperiment.form.announcement.AnnouncementUpdateForm;
import com.swpu.uchain.openexperiment.form.announcement.QueryCondition;
import com.swpu.uchain.openexperiment.redis.RedisService;
import com.swpu.uchain.openexperiment.redis.key.AnnouncementKey;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.AnnouncementService;
import com.swpu.uchain.openexperiment.service.GetUserService;
import io.swagger.models.auth.In;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import sun.security.smartcardio.SunPCSC;

import java.util.Date;
import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-25
 * @Description:
 * 实现公告模块
 */
@Service
public class AnnouncementServiceImpl implements AnnouncementService {
    @Autowired
    private AnnouncementMapper announcementMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private GetUserService getUserService;
    @Autowired
    private NewsReleaseMapper newsReleaseMapper;


    @Override
    public boolean insertAndPublish(Announcement announcement) {
        if (announcementMapper.insert(announcement) == 1){
            redisService.set(AnnouncementKey.getById, announcement.getId() + "", announcement);
            return true;
        }
        return false;
    }

    @Override
    public boolean update(Announcement announcement) {
        announcement.setUpdateTime(new Date());
        if (announcementMapper.updateByPrimaryKey(announcement) == 1){
            if (announcement.getStatus().equals(AnnouncementStatus.PUBLISHED.getValue())){
                redisService.set(AnnouncementKey.getById, announcement.getId() + "", announcement);
            }
            return true;
        }
        return false;
    }

    @Override
    public Integer delete(Long id) {
        redisService.delete(AnnouncementKey.getById, id + "");
        redisService.delete(AnnouncementKey.getClickTimesById, id + "");
        return announcementMapper.deleteByPrimaryKey(id);
    }

    private Announcement selectByIdAndStatus(Long announcementId,Integer status) {
        Announcement announcement = redisService.get(AnnouncementKey.getById, announcementId + "", Announcement.class);
        if (announcement == null){
            announcement = announcementMapper.selectByPrimaryKeyAndStatus(announcementId,status);
            if (announcement != null && announcement.getStatus().equals(AnnouncementStatus.PUBLISHED.getValue())){
                redisService.set(AnnouncementKey.getById, announcementId + "", announcement);
            }
        }
        return announcement;
    }

    @Override
    public Result publishAnnouncement(AnnouncementPublishForm publishForm) {
        Announcement announcement = convertFormToModel(publishForm,AnnouncementStatus.PUBLISHED);
        if (insertAndPublish(announcement)){
            //设置阅读次数
            redisService.set(AnnouncementKey.getClickTimesById, announcement.getId() + "", 0);
            return Result.success();
        }
        return Result.error(CodeMsg.ADD_ERROR);
    }

    @Override
    public Result homePagePublishNews(HomePageNewsPublishForm homePageNewsPublishForm) {
        User currentUser = getUserService.getCurrentUser();
        if (currentUser == null){
            throw new GlobalException(CodeMsg.AUTHENTICATION_ERROR);
        }
        NewsRelease newsRelease = new NewsRelease();
        BeanUtils.copyProperties(homePageNewsPublishForm,newsRelease);
        newsRelease.setPublishTime(new Date());
        newsRelease.setUpdateTime(new Date());
        newsRelease.setRealName(currentUser.getRealName());
        newsReleaseMapper.insert(newsRelease);
        return Result.success();
    }

    @Override
    public Result readAnnouncementDetail(Long announcementId) {
        Announcement announcement = selectByIdAndStatus(announcementId,null);
        if (announcement != null){
            AnnouncementVO announcementVO = new AnnouncementVO();
            BeanUtils.copyProperties(announcement, announcementVO);
            if (announcement.getStatus().equals(AnnouncementStatus.PUBLISHED.getValue())) {
                redisService.incr(AnnouncementKey.getClickTimesById, announcementId + "");
                Integer clickTimes = redisService.get(AnnouncementKey.getClickTimesById, announcementId + "", Integer.class);
                announcementVO.setClickTimes(clickTimes);
            }
            return Result.success(announcementVO);
        }
        return Result.error(CodeMsg.ANNOUNCEMENT_NOT_EXIST);
    }

    @Override
    public Result getList() {
        //使用无条件查询
        List<AnnouncementListVO> listVOS = announcementMapper.selectByConditionAndOrderByTime(null);
        return Result.success(listVOS);
    }

    @Override
    public Result changeInfo(AnnouncementUpdateForm updateForm) {
        Announcement announcement = selectByIdAndStatus(updateForm.getAnnouncementId(),null);
        if (announcement == null){
            return Result.error(CodeMsg.ANNOUNCEMENT_NOT_EXIST);
        }
        announcement.setTitle(updateForm.getTitle());
        announcement.setContent(updateForm.getContent());
        if (update(announcement)) {
            return Result.success();
        }
        return Result.error(CodeMsg.UPDATE_ERROR);
    }

    @Override
    public Result createAndSave(AnnouncementPublishForm publishForm) {
        Announcement announcement = convertFormToModel(publishForm,AnnouncementStatus.SAVE);
        announcementMapper.insert(announcement);
        return Result.success();
    }

    @Override
    public Result publishSavedAnnouncement(Long announcementId) {
        Announcement announcement = announcementMapper.selectByPrimaryKeyAndStatus(announcementId,AnnouncementStatus.SAVE.getValue());
        if (announcement == null){
            throw new GlobalException(CodeMsg.ANNOUNCEMENT_NOT_EXIST);
        }
        //先更新缓存状态
        announcement.setStatus(AnnouncementStatus.PUBLISHED.getValue());
        redisService.set(AnnouncementKey.getById, announcement.getId() + "", announcement);
        //设置阅读次数
        redisService.set(AnnouncementKey.getClickTimesById, announcement.getId() + "", 0);
        announcementMapper.updateAnnouncementStatusById(AnnouncementStatus.PUBLISHED.getValue(),announcementId);
        return Result.success();
    }

    @Override
    public Result cancelPublish(Long announcementId) {
        redisService.delete(AnnouncementKey.getById, announcementId + "");
        announcementMapper.updateAnnouncementStatusById(AnnouncementStatus.SAVE.getValue(),announcementId);
        return Result.success();
    }

    @Override
    public Result queryByCondition(QueryCondition condition) {
        return Result.success(announcementMapper.selectByConditionAndOrderByTime(condition));
    }

    private Announcement convertFormToModel(AnnouncementPublishForm publishForm,AnnouncementStatus status){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Long id  = Long.valueOf(authentication.getName());
        Announcement announcement = new Announcement();
        announcement.setTitle(publishForm.getTitle());
        announcement.setContent(publishForm.getContent());
        announcement.setPublisherId(id);
        announcement.setPublishTime(new Date());
        announcement.setUpdateTime(new Date());
        announcement.setStatus(status.getValue());
        return announcement;
    }
}
