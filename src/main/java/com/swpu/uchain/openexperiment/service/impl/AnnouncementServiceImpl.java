package com.swpu.uchain.openexperiment.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.swpu.uchain.openexperiment.VO.AnnouncementListVO;
import com.swpu.uchain.openexperiment.VO.AnnouncementVO;
import com.swpu.uchain.openexperiment.config.CountConfig;
import com.swpu.uchain.openexperiment.dao.AnnouncementMapper;
import com.swpu.uchain.openexperiment.domain.Announcement;
import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.form.announcement.AnnouncementPublishForm;
import com.swpu.uchain.openexperiment.form.announcement.AnnouncementUpdateForm;
import com.swpu.uchain.openexperiment.redis.RedisService;
import com.swpu.uchain.openexperiment.redis.key.AnnouncementKey;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.AnnouncementService;
import com.swpu.uchain.openexperiment.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    private UserService userService;
    @Autowired
    private CountConfig countConfig;
    @Override
    public boolean insert(Announcement announcement) {
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
            redisService.set(AnnouncementKey.getById, announcement.getId() + "", announcement);
            return true;
        }
        return false;
    }

    @Override
    public void delete(Long id) {
        redisService.delete(AnnouncementKey.getById, id + "");
        redisService.delete(AnnouncementKey.getClickTimesById, id + "");
        announcementMapper.deleteByPrimaryKey(id);
    }

    @Override
    public Announcement selectById(Long announcementId) {
        Announcement announcement = redisService.get(AnnouncementKey.getById, announcementId + "", Announcement.class);
        if (announcement == null){
            announcement = announcementMapper.selectByPrimaryKey(announcementId);
            if (announcement != null){
                redisService.set(AnnouncementKey.getById, announcementId + "", announcement);
            }
        }
        return announcement;
    }

    @Override
    public Result publishAnnouncement(AnnouncementPublishForm publishForm) {
        User user = userService.getCurrentUser();
        Announcement announcement = new Announcement();
        announcement.setTitle(publishForm.getTitle());
        announcement.setContent(publishForm.getContent());
        announcement.setPublisherId(user.getId());
        announcement.setPublishTime(new Date());
        announcement.setUpdateTime(new Date());
        if (insert(announcement)){
            redisService.set(AnnouncementKey.getClickTimesById, announcement.getId() + "", 0);
            return Result.success();
        }
        return Result.error(CodeMsg.ADD_ERROR);
    }

    @Override
    public Result readAnnouncementDetail(Long announcementId) {
        Announcement announcement = selectById(announcementId);
        if (announcement != null){
            AnnouncementVO announcementVO = new AnnouncementVO();
            BeanUtils.copyProperties(announcement, announcementVO);
            redisService.incr(AnnouncementKey.getClickTimesById, announcementId + "");
            Integer clickTimes = redisService.get(AnnouncementKey.getClickTimesById, announcementId + "", Integer.class);
            announcementVO.setClickTimes(clickTimes);
            return Result.success(announcementVO);
        }
        return Result.error(CodeMsg.ANNOUNCEMENT_NOT_EXIST);
    }

    @Override
    public Result getList(Integer pageNum) {
        PageHelper.startPage(pageNum, countConfig.getAnnouncement());
        List<AnnouncementListVO> listVOS = announcementMapper.selectOrderByTime();
        PageInfo<AnnouncementListVO> pageInfo = new PageInfo<>(listVOS);
        return Result.success(pageInfo);
    }

    @Override
    public Result changeInfo(AnnouncementUpdateForm updateForm) {
        Announcement announcement = selectById(updateForm.getAnnouncementId());
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
}
