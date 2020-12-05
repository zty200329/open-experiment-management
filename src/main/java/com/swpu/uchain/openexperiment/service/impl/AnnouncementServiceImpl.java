package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.VO.announcement.*;
import com.swpu.uchain.openexperiment.domain.*;
import com.swpu.uchain.openexperiment.enums.RoleType;
import com.swpu.uchain.openexperiment.form.announcement.*;
import com.swpu.uchain.openexperiment.mapper.*;
import com.swpu.uchain.openexperiment.enums.AnnouncementStatus;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import com.swpu.uchain.openexperiment.redis.RedisService;
import com.swpu.uchain.openexperiment.redis.key.AnnouncementKey;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.AnnouncementService;
import com.swpu.uchain.openexperiment.service.GetUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-25
 * @Description:
 * 实现公告模块
 */
@Service
@Slf4j
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
    @Autowired
    private HomepageAchievementMapper homepageAchievementMapper;
    @Autowired
    private HomepageAnnouncementMapper homepageAnnouncementMapper;
    @Autowired
    private HomepageAchievementIsTopMapper achievementIsTopMapper;


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
    public Result collegePublish(AnnouncementPublishForm publishForm) {
        User currentUser = getUserService.getCurrentUser();
        if (currentUser == null) {
            throw new GlobalException(CodeMsg.AUTHENTICATION_ERROR);
        }
        Announcement announcement = convertFormToModel(publishForm,AnnouncementStatus.PUBLISHED);
        announcement.setCollege(currentUser.getInstitute());
        if (insertAndPublish(announcement)){
            //设置阅读次数
            redisService.set(AnnouncementKey.getClickTimesById, announcement.getId() + "", 0);
            return Result.success();
        }
        return Result.error(CodeMsg.ADD_ERROR);
    }

    @Override
    public Result getNewsById(IdForm idForm) {
        NewsRelease newsRelease = newsReleaseMapper.selectByPrimaryKey(idForm.getId());
        return Result.success(newsRelease);
    }

    /**
     * 保存状态改为发布状态
     * @param idForm
     * @return
     */
    @Override
    public Result updateToPublished(IdForm idForm) {
        newsReleaseMapper.updateStatusByPrimaryKey1(idForm.getId());
        return Result.success();
    }

    /**
     * 改为保存
     * @param idForm
     * @return
     */
    @Override
    public Result updateToSave(IdForm idForm) {
        newsReleaseMapper.updateStatusByPrimaryKey2(idForm.getId());
        return Result.success();
    }

    @Override
    public Result deleteNewsById(IdForm idForm) {
        newsReleaseMapper.deleteByPrimaryKey(idForm.getId());
        return Result.success();
    }

    @Override
    public Result getAllNewsList() {
        List<NewsRelease> newsReleaseList = newsReleaseMapper.selectAll();
        List<HomePageNewsListVO> homePageNewsListVOS = new LinkedList<>();
        for (NewsRelease newsRelease : newsReleaseList) {
            HomePageNewsListVO homePageNewsListVO = new HomePageNewsListVO();
            BeanUtils.copyProperties(newsRelease,homePageNewsListVO);
            homePageNewsListVOS.add(homePageNewsListVO);
        }
        return Result.success(homePageNewsListVOS);
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
    public Result getHomePageNewsList() {
        List<NewsRelease> newsReleaseList = newsReleaseMapper.selectAllByPublished();
        List<HomePageNewsListVO> homePageNewsListVOS = new LinkedList<>();
        for (NewsRelease newsRelease : newsReleaseList) {
            HomePageNewsListVO homePageNewsListVO = new HomePageNewsListVO();
            BeanUtils.copyProperties(newsRelease,homePageNewsListVO);
            homePageNewsListVOS.add(homePageNewsListVO);
        }
        return Result.success(homePageNewsListVOS);
    }

    /**
     * 修改文章内容
     * @param updateNewsContentForm
     * @return
     */
    @Override
    public Result updateNewsContent(UpdateNewsContentForm updateNewsContentForm) {
        newsReleaseMapper.updateByPrimaryKey(updateNewsContentForm);

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
    public Result getList1() {
        //使用无条件查询
        List<AnnouncementListVO> listVOS = announcementMapper.selectByConditionAndOrderByTime1(null);
        return Result.success(listVOS);
    }
    @Override
    public Result getCollegeList() {
        User currentUser = getUserService.getCurrentUser();
        //检测用户是不是老师--后期可省略
        if (currentUser == null) {
            throw new GlobalException(CodeMsg.AUTHENTICATION_ERROR);
        }
        QueryCondition queryCondition = new QueryCondition();
        queryCondition.setCollege(currentUser.getInstitute());
        List<AnnouncementListVO> listVOS = announcementMapper.selectByCollgeAndConditionAndOrderByTime(queryCondition);
        return Result.success(listVOS);
    }

    @Override
    public Result getCollegeList1() {
        User currentUser = getUserService.getCurrentUser();
        if (currentUser == null) {
            throw new GlobalException(CodeMsg.AUTHENTICATION_ERROR);
        }
        QueryCondition queryCondition = new QueryCondition();
        queryCondition.setStatus(1);
        queryCondition.setCollege(currentUser.getInstitute());
        List<AnnouncementListVO> listVOS = announcementMapper.selectByCollgeAndConditionAndOrderByTime1(queryCondition);
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
    public Result createCollegeAndSave(AnnouncementPublishForm publishForm) {
        User currentUser = getUserService.getCurrentUser();
        //检测用户是不是老师--后期可省略
        if (currentUser == null) {
            throw new GlobalException(CodeMsg.AUTHENTICATION_ERROR);
        }
        Announcement announcement = convertFormToModel(publishForm,AnnouncementStatus.SAVE);
        announcement.setCollege(currentUser.getInstitute());
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

    /**
     * 发布成果展示
     * @param homepageAchievementForm
     * @return
     */
    @Override
    @Transactional(rollbackFor = Throwable.class)
    public Result publishAchievementShow(HomepageAchievementForm homepageAchievementForm) {
        User currentUser = getUserService.getCurrentUser();
        if (currentUser == null){
            throw new GlobalException(CodeMsg.AUTHENTICATION_ERROR);
        }
        if("".equals(homepageAchievementForm.getProjectName())){
            return Result.error(CodeMsg.PROJECT_NAME_IS_NULL);
        }
        if(homepageAchievementForm.getIsTop() == 1 && "".equals(homepageAchievementForm.getImgUrl())){
            return Result.error(CodeMsg.IMG_URL_EMPTY);
        }
        HomepageAchievement homepageAchievement = new HomepageAchievement();
        BeanUtils.copyProperties(homepageAchievementForm,homepageAchievement);
        homepageAchievement.setRealName(currentUser.getRealName());
        homepageAchievement.setPublishTime(new Date());
        homepageAchievement.setUpdateTime(new Date());
        homepageAchievementMapper.insert(homepageAchievement);
        if(homepageAchievementForm.getIsTop() == 1){
            HomepageAchievementIsTop homepageAchievementIsTop = new HomepageAchievementIsTop();
            homepageAchievementIsTop.setHomepageAchievementId(homepageAchievement.getId());
            achievementIsTopMapper.insert(homepageAchievementIsTop);
        }
        return Result.success();
    }

    @Override
    public Result getAchievementById(IdForm idForm) {
        HomepageAchievement homepageAchievement = homepageAchievementMapper.selectByPrimaryKey(idForm.getId());

        return Result.success(homepageAchievement);
    }

    @Override
    public Result deleteAchievementById(IdForm idForm) {
        homepageAchievementMapper.deleteByPrimaryKey(idForm.getId());
        achievementIsTopMapper.deleteByAchievementKey(idForm.getId());
        return Result.success();
    }

    @Override
    public Result deleteAchievementTopById(IdForm idForm) {
        achievementIsTopMapper.deleteByAchievementKey(idForm.getId());
        return Result.success();
    }

    @Override
    public Result updateAchievementToPublished(IdForm idForm) {
        homepageAchievementMapper.updateStatusByPrimaryKey1(idForm.getId());
        return Result.success();
    }

    @Override
    public Result updateAchievementToSave(IdForm idForm) {
        homepageAchievementMapper.updateStatusByPrimaryKey2(idForm.getId());
        return Result.success();
    }

    @Override
    public Result homePagePublishAnnouncement(HomePageNewsPublishForm homePageNewsPublishForm) {
        User currentUser = getUserService.getCurrentUser();
        if (currentUser == null){
            throw new GlobalException(CodeMsg.AUTHENTICATION_ERROR);
        }
        HomepageAnnouncement homepageAnnouncement = new HomepageAnnouncement();
        BeanUtils.copyProperties(homePageNewsPublishForm,homepageAnnouncement);
        homepageAnnouncement.setPublishTime(new Date());
        homepageAnnouncement.setUpdateTime(new Date());
        homepageAnnouncement.setRealName(currentUser.getRealName());
        homepageAnnouncementMapper.insert(homepageAnnouncement);
        return Result.success();
    }

    @Override
    public Result getHomePageAnnouncementList() {
        List<HomepageAnnouncement> homepageAnnouncements = homepageAnnouncementMapper.selectAllByPublished();
        List<HomePageNewsListVO> homePageNewsListVOS = new LinkedList<>();
        for (HomepageAnnouncement newsRelease : homepageAnnouncements) {
            HomePageNewsListVO homePageNewsListVO = new HomePageNewsListVO();
            BeanUtils.copyProperties(newsRelease,homePageNewsListVO);
            homePageNewsListVOS.add(homePageNewsListVO);
        }
        return Result.success(homePageNewsListVOS);
    }

    @Override
    public Result getAnnouncementById(IdForm idForm) {
        HomepageAnnouncement homepageAnnouncement = homepageAnnouncementMapper.selectByPrimaryKey(idForm.getId());
        return Result.success(homepageAnnouncement);
    }

    @Override
    public Result updateAnnouncementToPublished(IdForm idForm) {
        homepageAnnouncementMapper.updateStatusByPrimaryKey1(idForm.getId());
        return Result.success();
    }

    @Override
    public Result updateAnnouncementToSave(IdForm idForm) {
        homepageAnnouncementMapper.updateStatusByPrimaryKey2(idForm.getId());
        return Result.success();
    }

    @Override
    public Result deleteAnnouncementById(IdForm idForm) {
        homepageAnnouncementMapper.deleteByPrimaryKey(idForm.getId());
        return Result.success();
    }

    @Override
    public Result updateAnnouncementContent(UpdateNewsContentForm updateNewsContentForm) {
        homepageAnnouncementMapper.updateByPrimaryKey(updateNewsContentForm);

        return Result.success();
    }

    @Override
    public Result getAllAnnouncementList() {
        List<HomepageAnnouncement> homepageAnnouncements = homepageAnnouncementMapper.selectAll();
        List<HomePageNewsListVO> homePageNewsListVOS = new LinkedList<>();
        for (HomepageAnnouncement newsRelease : homepageAnnouncements) {
            HomePageNewsListVO homePageNewsListVO = new HomePageNewsListVO();
            BeanUtils.copyProperties(newsRelease,homePageNewsListVO);
            homePageNewsListVOS.add(homePageNewsListVO);
        }
        return Result.success(homePageNewsListVOS);
    }

    @Override
    public Result updateAchievementContent(UpdateAchievementContentForm updateNewsContentForm) {
        homepageAchievementMapper.updateByPrimaryKey(updateNewsContentForm);
        return Result.success();
    }

    @Override
    public Result getAllAchievementShowList() {
        List<HomepageAchievement> achievements = homepageAchievementMapper.selectAll();
        return getResult(achievements);
    }

    private Result getResult(List<HomepageAchievement> achievements) {
        List<Integer> integerList = achievementIsTopMapper.selectAllAchieveId();
        List<AchievementShowVO> achievementShowVOList = new LinkedList<>();
        for (HomepageAchievement achievement : achievements) {
            AchievementShowVO achievementShowVO = new AchievementShowVO();
            if(integerList.contains(achievement.getId())){
                achievementShowVO.setIsTop(true);
            }
            BeanUtils.copyProperties(achievement,achievementShowVO);
            achievementShowVOList.add(achievementShowVO);
        }
        return Result.success(achievementShowVOList);
    }

//    private Result getResult(List<HomepageAchievement> achievements) {
//        List<AchievementShowVO> achievementShowVOList = new LinkedList<>();
//        for (HomepageAchievement achievement : achievements) {
//            AchievementShowVO achievementShowVO = new AchievementShowVO();
//            BeanUtils.copyProperties(achievement,achievementShowVO);
//            achievementShowVOList.add(achievementShowVO);
//        }
//        return Result.success(achievementShowVOList);
//    }

    @Override
    public Result getTopPublishedAchievementShowList() {
        List<TopHomepageAchievement> homepageAchievementIsTops = achievementIsTopMapper.selectAll();

        return Result.success(homepageAchievementIsTops);
    }
    /**
     * 获取所有已发布的 有二级缓存
     * @return
     */
    @Override
    public Result getPublishedAchievementShowList() {
        List<HomepageAchievement> achievements = homepageAchievementMapper.selectAllPublished();
        return getResult(achievements);
    }

    @Override
    public Result queryByCondition(QueryCondition condition) {
        User currentUser = getUserService.getCurrentUser();
        if (currentUser == null){
            throw new GlobalException(CodeMsg.AUTHENTICATION_ERROR);
        }
        User user = getUserService.selectByUserCodeAndRole(currentUser.getCode(), RoleType.LAB_ADMINISTRATOR.getValue());
        //如果是实验室主任
        if(user != null){
            condition.setCollege(user.getInstitute());
            return Result.success(announcementMapper.selectByCollgeAndConditionAndOrderByTime(condition));
        }
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
