package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.dao.UserProjectGroupMapper;
import com.swpu.uchain.openexperiment.domain.ProjectGroup;
import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.domain.UserProjectGroup;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.enums.JoinStatus;
import com.swpu.uchain.openexperiment.enums.MemberRole;
import com.swpu.uchain.openexperiment.enums.UserType;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import com.swpu.uchain.openexperiment.form.project.AimForm;
import com.swpu.uchain.openexperiment.form.project.JoinProjectApplyForm;
import com.swpu.uchain.openexperiment.redis.RedisService;
import com.swpu.uchain.openexperiment.redis.key.UserProjectGroupKey;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.ProjectService;
import com.swpu.uchain.openexperiment.service.UserProjectService;
import com.swpu.uchain.openexperiment.service.UserService;
import com.swpu.uchain.openexperiment.util.CountUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-23
 * @Description:
 */
@Service
public class UserProjectServiceImpl implements UserProjectService {
    @Autowired
        private UserProjectGroupMapper userProjectGroupMapper;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserService userService;
    @Override
    public boolean insert(UserProjectGroup userProjectGroup) {
        int result = userProjectGroupMapper.insert(userProjectGroup);
        if (result == 1){
            redisService.set(UserProjectGroupKey.getByProjectGroupIdAndUserId,
                    userProjectGroup.getId() + "_" + userProjectGroup.getUserId(),
                    userProjectGroup);
            return true;
        }
        return false;
    }

    @Override
    public boolean update(UserProjectGroup userProjectGroup) {
        int result = userProjectGroupMapper.updateByPrimaryKey(userProjectGroup);
        if (result == 1){
            redisService.set(UserProjectGroupKey.getByProjectGroupIdAndUserId,
                    userProjectGroup.getId() + "_" +  userProjectGroup.getUserId(),
                    userProjectGroup);
            return true;
        }
        return false;
    }

    @Override
    public void delete(Long id) {
        UserProjectGroup userProjectGroup = userProjectGroupMapper.selectByPrimaryKey(id);
        if (userProjectGroup == null){
            return;
        }
        redisService.delete(UserProjectGroupKey.getByProjectGroupIdAndUserId,
                userProjectGroup.getProjectGroupId() + "_" + userProjectGroup.getUserId());
        userProjectGroupMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteByProjectGroupId(Long projectGroupId) {
        List<UserProjectGroup> userProjectGroups = selectByProjectGroupId(projectGroupId);
        for (UserProjectGroup userProjectGroup : userProjectGroups) {
            redisService.delete(UserProjectGroupKey.getByProjectGroupIdAndUserId,
                    userProjectGroup.getProjectGroupId() + "_" + userProjectGroup.getUserId());
        }
        userProjectGroupMapper.deleteByProjectGroupId(projectGroupId);
    }

    @Override
    public Result addUserProject(UserProjectGroup userProjectGroup) {
        if (insert(userProjectGroup)){
            return Result.success();
        }
        return Result.error(CodeMsg.ADD_ERROR);
    }

    @Override
    public List<UserProjectGroup> selectByProjectGroupId(Long projectGroupId) {
        return userProjectGroupMapper.selectByProjectGroupId(projectGroupId);
    }

    @Override
    public List<String> getUserCodesByProjectGroupId(Long projectGroupId) {
        return userProjectGroupMapper.selectUserCodesByProjectGroupId(projectGroupId);
    }

    @Override
    public UserProjectGroup selectByProjectGroupIdAndUserId(Long projectGroupId, Long userId) {
        UserProjectGroup userProjectGroup = redisService.get(UserProjectGroupKey.getByProjectGroupIdAndUserId,
                projectGroupId + "_" + userId,
                UserProjectGroup.class);
        if (userProjectGroup == null){
            userProjectGroup = userProjectGroupMapper.selectByProjectGroupIdAndUserId(projectGroupId, userId);
            if (userProjectGroup != null){
                redisService.set(UserProjectGroupKey.getByProjectGroupIdAndUserId,
                        projectGroupId + "_" + userId,
                        userProjectGroup);
            }
        }
        return userProjectGroup;
    }

    @Override
    public Result applyJoinProject(JoinProjectApplyForm joinProjectApplyForm) {
        User user = userService.getCurrentUser();
        if (user == null){
            return Result.error(CodeMsg.AUTHENTICATION_ERROR);
        }
        ProjectGroup projectGroup = projectService.selectByProjectGroupId(joinProjectApplyForm.getProjectGroupId());
        if (projectGroup == null){
            return Result.error(CodeMsg.PROJECT_GROUP_NOT_EXIST);
        }
        if (selectByProjectGroupIdAndUserId(projectGroup.getId(), user.getId()) != null){
            return Result.error(CodeMsg.ALREADY_APPLY);
        }
        //检验申请条件
        Result result = checkUserMatch(user, projectGroup);
        //检验通过
        if (result.getCode().intValue()
                == Result.success().getCode().intValue()){
            UserProjectGroup userProjectGroup = new UserProjectGroup();
            userProjectGroup.setMemberRole(MemberRole.NORMAL_MEMBER.getValue());
            userProjectGroup.setPersonalJudge(joinProjectApplyForm.getPersonalJudge());
            userProjectGroup.setProjectGroupId(joinProjectApplyForm.getProjectGroupId());
            userProjectGroup.setStatus(JoinStatus.APPLYING.getValue());
            userProjectGroup.setJoinTime(new Date());
            userProjectGroup.setUpdateTime(new Date());
            userProjectGroup.setUserId(user.getId());
            userProjectGroup.setTechnicalRole(userProjectGroup.getTechnicalRole());
            if (insert(userProjectGroup)) {
                return Result.success("已申请");
            }
        }
        return result;
    }

    @Override
    public Result checkUserMatch(User user, ProjectGroup projectGroup){
        List<User> users = userService.selectProjectJoinedUsers(projectGroup.getId());
        if (users.size() < projectGroup.getFitPeopleNum()
                && selectByProjectGroupId(projectGroup.getId()).size()
                < CountUtil.getMaxApplyNum(projectGroup.getFitPeopleNum())){
//            if (projectGroup.getLimitGrade() != null
//                    && projectGroup.getLimitGrade().intValue() != user.getGrade().intValue()){
//                return Result.error(CodeMsg.NOT_MATCH_LIMIT);
//            }
//            if (projectGroup.getLimitCollege() != null
//                    && !projectGroup.getLimitCollege().equals(user.getInstitute())){
//                return Result.error(CodeMsg.NOT_MATCH_LIMIT);
//            }
//            if (projectGroup.getLimitMajor() != null
//                    && !projectGroup.getLimitMajor().equals(user.getMajor())){
//                return Result.error(CodeMsg.NOT_MATCH_LIMIT);
//            }
            return Result.success();
        }
        return Result.error(CodeMsg.REACH_NUM_MAX);
    }

    @Override
    public Result aimUserMemberRole(AimForm aimForm) {
        //判断当前操作用户是否存在项目组
        User currentUser = userService.getCurrentUser();
        UserProjectGroup group = selectByProjectGroupIdAndUserId(aimForm.getProjectGroupId(), currentUser.getId());
        if (group == null){
            Result.error(CodeMsg.USER_NOT_IN_GROUP);
        }
        //判断指定用户是否存在与项目组
        UserProjectGroup userProjectGroup = selectByProjectGroupIdAndUserId(
                aimForm.getProjectGroupId(),
                aimForm.getUserId());
        if (userProjectGroup == null){
            return Result.error(CodeMsg.USER_GROUP_NOT_EXIST);
        }
        //判断指定用户是否是指导老师
        if (userProjectGroup.getMemberRole().intValue() == MemberRole.GUIDANCE_TEACHER.getValue()){
            return Result.error(CodeMsg.CANT_AIM_TEACHER);
        }
        if (aimForm.getMemberRole().intValue() == MemberRole.PROJECT_GROUP_LEADER.getValue()){
            //判断项目组是已否存在组长
            User user = userService.selectGroupLeader(aimForm.getProjectGroupId());
            if (user != null){
                return Result.error(CodeMsg.GROUP_LEADER_EXIST);
            }
            userProjectGroup.setMemberRole(MemberRole.PROJECT_GROUP_LEADER.getValue());
        }else if (aimForm.getMemberRole().intValue() == MemberRole.NORMAL_MEMBER.getValue()){
            userProjectGroup.setMemberRole(MemberRole.NORMAL_MEMBER.getValue());
        }else {
            return Result.error(CodeMsg.ILLEGAL_MEMBER_ROLE);
        }
        if (update(userProjectGroup)) {
            return Result.success();
        }
        return Result.error(CodeMsg.UPDATE_ERROR);
    }

    @Override
    public List<UserProjectGroup> selectByProjectAndStatus(Long projectGroupId, Integer joinStatus) {
        return userProjectGroupMapper.selectByProjectGroupIdAndJoinStatus(projectGroupId, joinStatus);
    }

    @Override
    public void addStuAndTeacherJoin(String[] stuCodes, String[] teacherCodes, Long projectGroupId) {
        Result result = null;
        if (teacherCodes != null){
            result = userService.createUserJoin(
                    teacherCodes,
                    projectGroupId,
                    UserType.LECTURER);
        }
        if (stuCodes != null){
            result = userService.createUserJoin(
                    stuCodes,
                    projectGroupId,
                    UserType.STUDENT);
        }
        if (result.getCode() != 0){
            throw new GlobalException(CodeMsg.ADD_USER_JOIN_ERROR);
        }
    }
}
