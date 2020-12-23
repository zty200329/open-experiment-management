package com.swpu.uchain.openexperiment.service;

import com.swpu.uchain.openexperiment.VO.user.UserVO;
import com.swpu.uchain.openexperiment.domain.ProjectGroup;
import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.domain.UserProjectGroup;
import com.swpu.uchain.openexperiment.form.project.AimForm;
import com.swpu.uchain.openexperiment.form.project.JoinProjectApplyForm;
import com.swpu.uchain.openexperiment.result.Result;

import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-23
 * @Description:
 * 用户项目关系模块
 */
public interface UserProjectService {

    /**
     * 插入新的用户项目关系
     * @param userProjectGroup
     * @return
     */
    boolean insert(UserProjectGroup userProjectGroup);

    /**
     * 更新用户与项目组关系
     * @param userProjectGroup
     * @return
     */
    boolean update(UserProjectGroup userProjectGroup);

    /**
     * 移除某用户和项目的关系
     * @param id
     */
    void delete(Long id);

    /**
     * 删除项目组的所有成员
     * @param projectGroupId
     */
    void deleteByProjectGroupId(Long projectGroupId);

    /**
     * 添加用户参与项目组关系
     * @param userProjectGroup
     * @return
     */
    Result addUserProject(UserProjectGroup userProjectGroup);

    /**
     * 根据项目id查找指导教师
     * @param projectGroupId
     * @return
     */
    List<UserVO> selectGuideTeacherByGroupId(Long projectGroupId);

    List<UserProjectGroup> selectByProjectGroupId(Long projectGroupId);

    /**
     * 获取当前项目的所有用户编号
     * @param projectGroupId
     * @return
     */
    List<String> getUserCodesByProjectGroupId(Long projectGroupId);

    /**
     * 按项目组id和用户id进行查找
     * @param projectGroupId
     * @param userId
     * @return
     */
    UserProjectGroup selectByProjectGroupIdAndUserId(Long projectGroupId, Long userId);

    /**
     * 申请加入项目
     * @param joinProjectApplyForm
     * @return
     */
    Result applyJoinProject(JoinProjectApplyForm joinProjectApplyForm);

    /**
     * @param projectGroup
     * @return
     */
    Result checkUserMatch(User user, ProjectGroup projectGroup);

    /**
     * 指定用户为项目组组长
     * @param aimForm
     * @return
     */
    Result aimUserMemberRole(AimForm aimForm);

    /**
     * 查询指定项目参与状态的关系信息
     * @param joinStatus
     * @param projectGroupId
     * @return
     */
    List<UserProjectGroup> selectByProjectAndStatus(Long projectGroupId, Integer joinStatus);

    /**
     * 添加用户学生和老师参与项目
     * @param stuCodes
     * @param teacherCodes
     * @param projectGroupId
     */
    void addStuAndTeacherJoin(String[] stuCodes, String[] teacherCodes, Long projectGroupId);

    /**
     * 项目申请时添加指导老师
     * @param teacherCodes 指导老师学工号
     * @param projectGroupId  项目编号
     */
    void addTeacherJoin(String[] teacherCodes, Long projectGroupId);
}
