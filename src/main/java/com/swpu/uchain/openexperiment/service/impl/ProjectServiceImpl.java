package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.VO.ApplyPointFormInfoVO;
import com.swpu.uchain.openexperiment.dao.ProjectGroupMapper;
import com.swpu.uchain.openexperiment.domain.ProjectGroup;
import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.domain.UserProjectGroup;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.enums.JoinStatus;
import com.swpu.uchain.openexperiment.enums.ProjectStatus;
import com.swpu.uchain.openexperiment.enums.UserType;
import com.swpu.uchain.openexperiment.form.project.JoinForm;
import com.swpu.uchain.openexperiment.form.project.CreateProjectApplyForm;
import com.swpu.uchain.openexperiment.redis.RedisService;
import com.swpu.uchain.openexperiment.redis.key.ProjectGroupKey;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.ProjectService;
import com.swpu.uchain.openexperiment.service.UserProjectService;
import com.swpu.uchain.openexperiment.service.UserService;
import com.swpu.uchain.openexperiment.util.ConvertUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    public Result applyCreateProject(CreateProjectApplyForm createProjectApplyForm) {
        ProjectGroup projectGroup = projectGroupMapper.selectByName(createProjectApplyForm.getProjectName());
        if (projectGroup != null){
            return Result.error(CodeMsg.PROJECT_GROUP_HAD_EXIST);
        }
        projectGroup = new ProjectGroup();
        BeanUtils.copyProperties(createProjectApplyForm, projectGroup);
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
        //TODO,封装项目展示的VO
        return null;
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
        ApplyPointFormInfoVO applyPointFormInfoVO = ConvertUtil.addUserDetailVO(users);

        //TODO,完成重点项目立项表单数据展示
        return null;
    }

}
