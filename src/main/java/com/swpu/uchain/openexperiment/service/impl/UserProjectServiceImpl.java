package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.VO.user.UserVO;
import com.swpu.uchain.openexperiment.domain.*;
import com.swpu.uchain.openexperiment.mapper.ProjectGroupMapper;
import com.swpu.uchain.openexperiment.mapper.UserProjectAccountMapper;
import com.swpu.uchain.openexperiment.mapper.UserProjectGroupMapper;
import com.swpu.uchain.openexperiment.enums.*;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import com.swpu.uchain.openexperiment.form.project.AimForm;
import com.swpu.uchain.openexperiment.form.project.JoinProjectApplyForm;
import com.swpu.uchain.openexperiment.mapper.UserRoleMapper;
import com.swpu.uchain.openexperiment.redis.RedisService;
import com.swpu.uchain.openexperiment.redis.key.UserProjectGroupKey;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.*;
import com.swpu.uchain.openexperiment.util.CountUtil;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class UserProjectServiceImpl implements UserProjectService {
    @Autowired
    private UserProjectGroupMapper userProjectGroupMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserService userService;
    @Autowired
    private GetUserService getUserService;
    @Autowired
    private ProjectGroupMapper projectGroupMapper;
    @Autowired
    private TimeLimitService timeLimitService;
    @Autowired
    private UserRoleService userRoleService;
    @Autowired
    private UserProjectAccountMapper userProjectAccountMapper;

    @Override
    public boolean insert(UserProjectGroup userProjectGroup) {
        int result = userProjectGroupMapper.insert(userProjectGroup);
        return result == 1;
    }

    @Override
    public boolean update(UserProjectGroup userProjectGroup) {
        int result = userProjectGroupMapper.updateByPrimaryKey(userProjectGroup);
        return result == 1;
    }

    @Override
    public void delete(Long id) {
        UserProjectGroup userProjectGroup = userProjectGroupMapper.selectByPrimaryKey(id);
        if (userProjectGroup == null) {
            return;
        }
        redisService.delete(UserProjectGroupKey.getByProjectGroupIdAndUserId,
                userProjectGroup.getProjectGroupId() + "_" + userProjectGroup.getUserId());
        userProjectGroupMapper.deleteByPrimaryKey(id);
    }

    @Override
    public void deleteByProjectGroupId(Long projectGroupId) {
        userProjectGroupMapper.deleteByProjectGroupId(projectGroupId);
    }

    @Override
    public Result addUserProject(UserProjectGroup userProjectGroup) {
        if (insert(userProjectGroup)) {
            return Result.success();
        }
        return Result.error(CodeMsg.ADD_ERROR);
    }

    @Override
    public List<UserVO> selectGuideTeacherByGroupId(Long projectGroupId) {
        return projectGroupMapper.selectGuideTeacherByGroupId(projectGroupId);
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
        return userProjectGroupMapper.selectByProjectGroupIdAndUserId(projectGroupId, userId);
    }

    private String[] strToStrArr(String str) {
        if (str == null) {
            return null;
        }
        return str.replace("[", "").replace("]", "").split(",");
    }

    @Override
    public Result applyJoinProject(JoinProjectApplyForm joinProjectApplyForm) {

        //时间验证
        timeLimitService.validTime(TimeLimitType.JOIN_APPLY_LIMIT);

        User user = getUserService.getCurrentUser();
        if (user.getMobilePhone() == null || user.getQqNum() == null) {
            throw new GlobalException(CodeMsg.USER_INFO_NOT_COMPLETE);
        }

        //验证成员身份
        if (!userRoleService.validContainsUserRole(RoleType.NORMAL_STU)) {
            throw new GlobalException(CodeMsg.PERMISSION_DENNY);
        }

        ProjectGroup projectGroup = projectGroupMapper.selectByPrimaryKey(joinProjectApplyForm.getProjectGroupId());

        //验证项目是否存在
        if (projectGroup == null) {
            return Result.error(CodeMsg.PROJECT_GROUP_NOT_EXIST);
        }

        //验证是否已经达到最大数量
        if (userProjectGroupMapper.selectStuCount(joinProjectApplyForm.getProjectGroupId(), JoinStatus.JOINED.getValue()) >= projectGroup.getFitPeopleNum()) {
            throw new GlobalException(CodeMsg.PROJECT_USER_MAX_ERROR);
        }

        //判断加入数量是否已经满了
        UserProjectAccount userProjectAccount2 = userProjectAccountMapper.selectByCode(user.getCode());
        //存在该用户记录
        if(userProjectAccount2 != null) {
            if (userProjectAccount2.getKeyNum() + userProjectAccount2.getGeneralNum() > 3) {
                throw new GlobalException(CodeMsg.STU_MAX_NUM_OF_TYPE);
            }else{
                if(projectGroup.getProjectType().equals(ProjectType.GENERAL.getValue())){
                    userProjectAccount2.setGeneralNum(userProjectAccount2.getGeneralNum()+1);
                }else {
                    userProjectAccount2.setKeyNum(userProjectAccount2.getKeyNum()+1);
                }
                userProjectAccountMapper.updateByPrimaryKey(userProjectAccount2);
            }
            //不存在
        }else{
            UserProjectAccount userAccount = new UserProjectAccount();
            userAccount.setCode(user.getCode());
            userAccount.setCollege(String.valueOf(user.getInstitute()));
            userAccount.setUserType(1);
            if(projectGroup.getProjectType().equals(ProjectType.GENERAL.getValue())){
                userAccount.setGeneralNum(1);
            }else {
                userAccount.setKeyNum(1);
            }

            userProjectAccountMapper.insert(userAccount);
        }

        //学生是否可以加入判断
        int allowed = 0;

        String limitGrade = projectGroup.getLimitGrade();
        String[] limitGradeArr = strToStrArr(limitGrade);
        if (limitGradeArr == null) {
            allowed += 1;
        } else {
            for (String grade : limitGradeArr
            ) {
                if (grade.equals(user.getGrade().toString())) {
                    allowed += 1;
                    log.info("年级符合要求----");
                }
            }
        }

        String limitCollege = projectGroup.getLimitCollege();
        String[] limitCollegeArr = strToStrArr(limitCollege);
        if (limitCollegeArr == null) {
            allowed += 1;
        } else {
            for (String grade : limitCollegeArr
            ) {
                if (grade.equals("\"" + user.getInstitute().toString() + "\"")) {
                    allowed += 1;
                    log.info("学院符合要求----");
                }
            }
        }

        String limitMajor = projectGroup.getLimitMajor();
        String[] limitMajorArr = strToStrArr(limitMajor);
        //大类专业直接通过
        if (limitMajorArr == null || user.getMajor().equals("2019")) {
            allowed += 1;
        } else {
            for (String grade : limitMajorArr
            ) {
                if (grade.equals("\"" + user.getMajor() + "\"")) {
                    allowed += 1;
                    log.info("专业符合要求----");
                }
            }
        }
        if (allowed != 3) {
            throw new GlobalException(CodeMsg.NOT_MATCH_LIMIT);
        }


        //验证项目状态
        if (!projectGroup.getStatus().equals(ProjectStatus.LAB_ALLOWED.getValue())) {
            return Result.error(CodeMsg.PROJECT_IS_NOT_LAB_ALLOWED);
        }
        //判断已经申请和申请被拒绝
        if (selectByProjectGroupIdAndUserId(projectGroup.getId(), Long.valueOf(user.getCode())) != null) {
            return Result.error(CodeMsg.ALREADY_APPLY);
        }
        //检验通过
        UserProjectGroup userProjectGroup = new UserProjectGroup();
        userProjectGroup.setMemberRole(MemberRole.NORMAL_MEMBER.getValue());
        userProjectGroup.setPersonalJudge(joinProjectApplyForm.getPersonalJudge());
        userProjectGroup.setProjectGroupId(joinProjectApplyForm.getProjectGroupId());
        userProjectGroup.setStatus(JoinStatus.APPLYING.getValue());
        userProjectGroup.setJoinTime(new Date());
        userProjectGroup.setUpdateTime(new Date());
        userProjectGroup.setUserId(Long.valueOf(user.getCode()));
        userProjectGroup.setTechnicalRole(userProjectGroup.getTechnicalRole());
        //入库
        insert(userProjectGroup);
        return Result.success("已申请");
    }

    @Override
    public Result checkUserMatch(User user, ProjectGroup projectGroup) {
        List<User> users = userService.selectProjectJoinedUsers(projectGroup.getId());
        if (users.size() < projectGroup.getFitPeopleNum()
                && selectByProjectGroupId(projectGroup.getId()).size()
                < CountUtil.getMaxApplyNum(projectGroup.getFitPeopleNum())) {
            return Result.success();
        }
        return Result.error(CodeMsg.REACH_NUM_MAX);
    }

    @Override
    public Result aimUserMemberRole(AimForm aimForm) {

        //判断当前操作用户是否存在项目组
        User currentUser = getUserService.getCurrentUser();
        UserProjectGroup group = selectByProjectGroupIdAndUserId(aimForm.getProjectGroupId(), Long.valueOf(currentUser.getCode()));
        if (group == null) {
            Result.error(CodeMsg.USER_NOT_IN_GROUP);
        }
        //判断指定用户是否存在与项目组
        UserProjectGroup userProjectGroup = selectByProjectGroupIdAndUserId(
                aimForm.getProjectGroupId(),
                aimForm.getUserId());
        if (userProjectGroup == null) {
            return Result.error(CodeMsg.USER_GROUP_NOT_EXIST);
        }
        //判断指定用户是否是指导老师
        if (userProjectGroup.getMemberRole().intValue() == MemberRole.GUIDANCE_TEACHER.getValue()) {
            return Result.error(CodeMsg.CANT_AIM_TEACHER);
        }

        //验证项目状态
        Integer status = projectGroupMapper.selectByPrimaryKey(aimForm.getProjectGroupId()).getStatus();
        if (!status.equals(ProjectStatus.LAB_ALLOWED.getValue()) && !status.equals(ProjectStatus.REJECT_MODIFY.getValue()) && !status.equals(ProjectStatus.GUIDE_TEACHER_ALLOWED.getValue())
        ) {
            int SubordinateCollege = projectGroupMapper.selectSubordinateCollege(aimForm.getProjectGroupId());
            if (SubordinateCollege != 39) {
                throw new GlobalException(CodeMsg.CURRENT_PROJECT_STATUS_ERROR);
            }
        }


        if (aimForm.getMemberRole().intValue() == MemberRole.PROJECT_GROUP_LEADER.getValue()) {
            //判断项目组是已否存在组长
            User user = userService.selectGroupLeader(aimForm.getProjectGroupId());
            if (user != null) {
                return Result.error(CodeMsg.GROUP_LEADER_EXIST);
            }
            userProjectGroup.setMemberRole(MemberRole.PROJECT_GROUP_LEADER.getValue());
        } else if (aimForm.getMemberRole().intValue() == MemberRole.NORMAL_MEMBER.getValue()) {
            userProjectGroup.setMemberRole(MemberRole.NORMAL_MEMBER.getValue());
        } else {
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
        if (teacherCodes != null) {
            result = userService.createUserJoin(
                    teacherCodes,
                    projectGroupId,
                    UserType.LECTURER);
        }
        if (stuCodes != null) {
            result = userService.createUserJoin(
                    stuCodes,
                    projectGroupId,
                    UserType.STUDENT);
        }
        assert result != null;
        if (result.getCode() != 0) {
            throw new GlobalException(CodeMsg.USER_NO_EXIST);
        }
    }

    @Override
    public void addTeacherJoin(String[] teacherCodes, Long projectGroupId) {
        Result result = null;
        if (teacherCodes != null) {
            result = userService.createUserJoin(
                    teacherCodes,
                    projectGroupId,
                    UserType.LECTURER);
        }

        if (result.getCode() != 0) {
            throw new GlobalException(CodeMsg.ADD_USER_JOIN_ERROR);
        }
    }
}
