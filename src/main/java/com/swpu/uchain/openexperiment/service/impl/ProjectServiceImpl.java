package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.VO.*;
import com.swpu.uchain.openexperiment.config.CountConfig;
import com.swpu.uchain.openexperiment.dao.ProjectGroupMapper;
import com.swpu.uchain.openexperiment.domain.*;
import com.swpu.uchain.openexperiment.enums.*;
import com.swpu.uchain.openexperiment.form.funds.FundsForm;
import com.swpu.uchain.openexperiment.form.project.AppendApplyForm;
import com.swpu.uchain.openexperiment.form.project.CreateProjectApplyForm;
import com.swpu.uchain.openexperiment.form.project.JoinForm;
import com.swpu.uchain.openexperiment.redis.RedisService;
import com.swpu.uchain.openexperiment.redis.key.ProjectGroupKey;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.*;
import com.swpu.uchain.openexperiment.util.ConvertUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-22
 * @Description:
 * 项目管理模块
 */
@Service
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private UserService userService;
    @Autowired
    private ProjectGroupMapper projectGroupMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserProjectService userProjectService;
    //TODO,注入文件的Service,答辩小组Service,资金模块的Service,
    @Autowired
    private FundsService fundsService;
    @Autowired
    private CountConfig countConfig;
    @Override
    public boolean insert(ProjectGroup projectGroup) {
        if (projectGroupMapper.insert(projectGroup) == 1){
            redisService.set(ProjectGroupKey.getByProjectGroupId, projectGroup.getId() + "", projectGroup);
            return true;
        }
        return false;
    }

    @Override
    public boolean update(ProjectGroup projectGroup) {
        if (projectGroupMapper.updateByPrimaryKey(projectGroup) == 1){
            redisService.set(ProjectGroupKey.getByProjectGroupId, projectGroup.getId() + "", projectGroup);
            return true;
        }
        return false;
    }

    @Override
    public void delete(Long projectGroupId) {
        redisService.delete(ProjectGroupKey.getByProjectGroupId, projectGroupId + "");
        projectGroupMapper.deleteByPrimaryKey(projectGroupId);
        //删除项目相关的成员信息
        userProjectService.deleteByProjectGroupId(projectGroupId);
        //TODO,删除所有的关系模块,文件,答辩小组,资金
    }

    @Override
    public Result addProjectGroup(ProjectGroup projectGroup) {
        if (insert(projectGroup)){
            return Result.success();
        }
        return Result.error(CodeMsg.ADD_ERROR);
    }

    @Override
    public ProjectGroup selectByProjectGroupId(Long projectGroupId) {
        ProjectGroup projectGroup = redisService.get(ProjectGroupKey.getByProjectGroupId, projectGroupId + "", ProjectGroup.class);
        if (projectGroup == null){
            projectGroup = projectGroupMapper.selectByPrimaryKey(projectGroupId);
            if (projectGroup != null){
                redisService.set(ProjectGroupKey.getByProjectGroupId, projectGroupId + "", projectGroup);
            }
        }
        return projectGroup;
    }

    @Override
    @Transactional
    public Result applyCreateProject(CreateProjectApplyForm createProjectApplyForm) {
        ProjectGroup projectGroup = projectGroupMapper.selectByName(createProjectApplyForm.getProjectName());
        if (projectGroup != null){
            return Result.error(CodeMsg.PROJECT_GROUP_HAD_EXIST);
        }
        projectGroup = new ProjectGroup();
        BeanUtils.copyProperties(createProjectApplyForm, projectGroup);
        projectGroup.setStatus(ProjectStatus.DECLARE.getValue());
        Result result = addProjectGroup(projectGroup);
        if (result.getCode() != 0){
            return result;
        }
        result = userService.createUserJoin(
                createProjectApplyForm.getTeacherCodes(),
                projectGroup.getId(),
                UserType.LECTURER);
        if (result.getCode() != 0){
            return result;
        }
        //TODO,对文件上传的处理,1.获取文件名,2.保存文件,3.维护数据库
        return userService.createUserJoin(
                createProjectApplyForm.getStuCodes(),
                projectGroup.getId(),
                UserType.STUDENT);
    }

    @Override
    public Result getCurrentUserProjects() {
        User currentUser = userService.getCurrentUser();
        if (userService == null){
            return Result.error(CodeMsg.AUTHENTICATION_ERROR);
        }
        //获取当前用户参与的所有项目
        List<ProjectGroup> projectGroups = redisService.getArraylist(
                ProjectGroupKey.getByUserId,
                currentUser.getId() + "",
                ProjectGroup.class);
        if (projectGroups == null || projectGroups.size() == 0){
            projectGroups = projectGroupMapper.selectByUserId(currentUser.getId());
            if (projectGroups != null && projectGroups.size() != 0){
                redisService.set(ProjectGroupKey.getByUserId, currentUser.getId() + "", projectGroups);
            }
        }
        //设置当前用户的所有项目VO
        List<MyProjectVO> myProjectVOS = new ArrayList<>();
        for (ProjectGroup projectGroup : projectGroups) {
            MyProjectVO myProjectVO = new MyProjectVO();
            BeanUtils.copyProperties(projectGroup, myProjectVO);
            myProjectVO.setProjectGroupId(projectGroup.getId());
            myProjectVO.setProjectDetails(getProjectDetails(projectGroup));
            myProjectVOS.add(myProjectVO);
        }
        return Result.success(myProjectVOS);
    }

    public ProjectDetails getProjectDetails(ProjectGroup projectGroup){
        ProjectDetails projectDetails = new ProjectDetails();
        projectDetails.setLabName(projectGroup.getLabName());
        projectDetails.setAddress(projectGroup.getAddress());
        //设置创建人,即项目负责人
        User user = userService.selectByUserId(projectGroup.getCreatorId());
        UserProjectGroup userProjectGroup = userProjectService.selectByProjectGroupIdAndUserId(
                projectGroup.getId(),
                user.getId());
        projectDetails.setCreator(new UserVO(
                user.getId(),
                user.getRealName(),
                userProjectGroup.getMemberRole()));
        //设置项目的成员信息
        List<UserProjectGroup> userProjectGroups = userProjectService.selectByProjectGroupId(projectGroup.getId());
        List<UserVO> members = new ArrayList<>();
        for (UserProjectGroup userProject : userProjectGroups) {
            User member = userService.selectByUserId(userProject.getUserId());
            //设置项目组组长
            if (userProject.getMemberRole().intValue() == MemberRole.PROJECT_GROUP_LEADER.getValue()){
                projectDetails.setLeader(new UserVO(
                        member.getId(),
                        member.getRealName(),
                        userProject.getMemberRole()));
            }
            UserVO userVO = new UserVO();
            userVO.setUserId(member.getId());
            userVO.setUserName(member.getRealName());
            userVO.setMemberRole(userProject.getMemberRole());
            members.add(userVO);
        }
        projectDetails.setMembers(members);
        //设置项目资金详情
        List<Funds> fundsDetails = fundsService.getFundsDetails(projectGroup.getId());
        int applyAmount = 0, agreeAmount = 0;
        for (Funds fundsDetail : fundsDetails) {
            applyAmount += fundsDetail.getAmount();
            if (fundsDetail.getStatus().intValue() == FundsStatus.AGREED.getValue()){
                agreeAmount += fundsDetail.getAmount();
            }
        }
        projectDetails.setTotalApplyFundsAmount(applyAmount);
        projectDetails.setTotalAgreeFundsAmount(agreeAmount);
        projectDetails.setFundsDetails(fundsDetails);
        //TODO,设置文件信息
//        projectDetails.setProjectFiles();
        return projectDetails;
    }

    @Override
    public Result agreeJoin(JoinForm joinForm) {
        User user = userService.selectByUserId(joinForm.getUserId());
        if (user == null){
            return Result.error(CodeMsg.USER_NO_EXIST);
        }
        ProjectGroup projectGroup = selectByProjectGroupId(joinForm.getProjectGroupId());
        if (projectGroup == null){
            return Result.error(CodeMsg.PROJECT_GROUP_NOT_EXIST);
        }
        UserProjectGroup userProjectGroup = userProjectService
                .selectByProjectGroupIdAndUserId(
                joinForm.getProjectGroupId(),
                joinForm.getUserId());
        if (userProjectGroup == null){
            return Result.error(CodeMsg.USER_NOT_APPLYING);
        }
        if (userProjectGroup.getStatus().intValue()
                == JoinStatus.JOINED.getValue()){
            return Result.error(CodeMsg.USER_HAD_JOINED);
        }
        userProjectGroup.setStatus(JoinStatus.JOINED.getValue());
        if (userProjectService.update(userProjectGroup)) {
            return Result.success();
        }
        return Result.error(CodeMsg.UPDATE_ERROR);
    }

    @Override
    public Result agreeEstablish(Long projectGroupId) {
        ProjectGroup projectGroup = selectByProjectGroupId(projectGroupId);
        if (projectGroup == null){
            return Result.error(CodeMsg.PROJECT_GROUP_NOT_EXIST);
        }
        projectGroup.setStatus(ProjectStatus.ESTABLISH.getValue());
        if (update(projectGroup)) {
            return Result.success();
        }
        return Result.error(CodeMsg.UPDATE_ERROR);
    }

    @Override
    public Result getApplyForm(Long projectGroupId) {
        ProjectGroup projectGroup = selectByProjectGroupId(projectGroupId);
        if (projectGroup == null){
            return Result.error(CodeMsg.PROJECT_GROUP_NOT_EXIST);
        }
        List<User> users = userService.selectProjectJoinedUsers(projectGroupId);
        if (projectGroup.getProjectType().intValue() == ProjectType.KEY.getValue()){
            ApplyKeyFormInfoVO applyKeyFormInfoVO = ConvertUtil.addUserDetailVO(users, ApplyKeyFormInfoVO.class);
            applyKeyFormInfoVO.setFundsDetails(fundsService.getFundsDetails(projectGroupId));
            BeanUtils.copyProperties(projectGroup, applyKeyFormInfoVO);
            applyKeyFormInfoVO.setProjectGroupId(projectGroup.getId());
            //TODO,添加立项表文件id
            return Result.success(applyKeyFormInfoVO);
        }else {
            ApplyGeneralFormInfoVO applyGeneralFormInfoVO = ConvertUtil.addUserDetailVO(users, ApplyGeneralFormInfoVO.class);
            BeanUtils.copyProperties(projectGroup, applyGeneralFormInfoVO);
            applyGeneralFormInfoVO.setProjectGroupId(projectGroup.getId());
            //TODO,添加立项表文件id
            return Result.success(applyGeneralFormInfoVO);
        }
    }

    @Override
    public Result appendCreateApply(AppendApplyForm appendApplyForm) {
        User currentUser = userService.getCurrentUser();
        UserProjectGroup userProjectGroup = userProjectService.selectByProjectGroupIdAndUserId(
                appendApplyForm.getProjectGroupId(),
                currentUser.getId());
        if (userProjectGroup == null){
            return Result.error(CodeMsg.USER_NOT_IN_GROUP);
        }
        if (userProjectGroup.getMemberRole().intValue() == MemberRole.NORMAL_MEMBER.getValue()){
            Result.error(CodeMsg.PERMISSION_DENNY);
        }
        FundsForm[] fundsForms = appendApplyForm.getFundsForms();
        for (FundsForm fundsForm : fundsForms) {
            //资金id不为空进行更新操作
            if (fundsForm.getFundsId() != null){
                Funds funds = fundsService.selectById(fundsForm.getFundsId());
                if (funds == null){
                    return Result.error(CodeMsg.FUNDS_NOT_EXIST);
                }
                //申请通过的资金无进行更新操作
                if (funds.getStatus().intValue() == FundsStatus.AGREED.getValue()){
                    return Result.error(CodeMsg.FUNDS_AGREE_CANT_CHANGE);
                }
                BeanUtils.copyProperties(fundsForm, funds);
                funds.setUpdateTime(new Date());
                if (!fundsService.update(funds)) {
                    return Result.error(CodeMsg.UPDATE_ERROR);
                }
            }else {
                //添加资金信息
                Funds funds = new Funds();
                BeanUtils.copyProperties(fundsForm, funds);
                funds.setProjectGroupId(appendApplyForm.getProjectGroupId());
                funds.setApplicantId(currentUser.getId());
                funds.setStatus(FundsStatus.APPLYING.getValue());
                funds.setCreateTime(new Date());
                funds.setUpdateTime(new Date());
                if (!fundsService.insert(funds)) {
                    return Result.error(CodeMsg.ADD_ERROR);
                }
            }
        }
        return Result.success();
    }

    @Override
    public Result checkApplyInfo(Integer pageNum) {
        Integer startNum = (pageNum - 1 ) * countConfig.getCheckProject();
        List<ProjectGroup> projectGroups = projectGroupMapper.selectApplyByPageOrderByTime(
                startNum,
                countConfig.getCheckProject());
        for (ProjectGroup projectGroup : projectGroups) {

        }
        //TODO
        return null;
    }

}
