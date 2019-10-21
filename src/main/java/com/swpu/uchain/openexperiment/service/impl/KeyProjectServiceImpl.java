package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.DTO.KeyProjectDTO;
import com.swpu.uchain.openexperiment.dao.KeyProjectStatusMapper;
import com.swpu.uchain.openexperiment.dao.ProjectGroupMapper;
import com.swpu.uchain.openexperiment.dao.UserProjectGroupMapper;
import com.swpu.uchain.openexperiment.domain.ProjectGroup;
import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.enums.KeyProjectStatus;
import com.swpu.uchain.openexperiment.enums.ProjectStatus;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import com.swpu.uchain.openexperiment.form.project.KeyProjectApplyForm;
import com.swpu.uchain.openexperiment.form.user.StuMember;
import com.swpu.uchain.openexperiment.form.user.TeacherMember;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.GetUserService;
import com.swpu.uchain.openexperiment.service.KeyProjectService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author dengg
 */
@Service
public class KeyProjectServiceImpl implements KeyProjectService {


    private ProjectGroupMapper projectGroupMapper;
    private UserProjectGroupMapper userProjectGroupMapper;
    private KeyProjectStatusMapper keyProjectStatusMapper;
    private GetUserService getUserService;

    public KeyProjectServiceImpl(ProjectGroupMapper projectGroupMapper, UserProjectGroupMapper userProjectGroupMapper,
                                 KeyProjectStatusMapper keyProjectStatusMapper,GetUserService getUserService) {
        this.projectGroupMapper = projectGroupMapper;
        this.userProjectGroupMapper = userProjectGroupMapper;
        this.keyProjectStatusMapper = keyProjectStatusMapper;
        this.getUserService = getUserService;
    }

    @Transactional(rollbackFor = GlobalException.class)
    @Override
    public Result createKeyApply(KeyProjectApplyForm form) {
        String projectName = form.getProjectName();
        ProjectGroup projectGroup = projectGroupMapper.selectByPrimaryProjectName(projectName);
        if (projectGroup == null){
            throw new GlobalException(CodeMsg.PROJECT_GROUP_NOT_EXIST);
        }
        User user = getUserService.getCurrentUser();
        Long projectId = projectGroup.getId();
        if (projectGroup.getStatus() < ProjectStatus.LAB_ALLOWED.getValue()){
            throw new GlobalException(CodeMsg.PROJECT_IS_NOT_LAB_ALLOWED);
        }
        //创建重点项目状态
        keyProjectStatusMapper.insert(projectId, KeyProjectStatus.TO_DE_CONFIRMED.getValue(),
                projectGroup.getSubordinateCollege(), Long.valueOf(user.getCode()));

        List<StuMember> stuMemberList = form.getMembers();

        //用于记录更新的条数
        int counter = 0;
        for (StuMember stuMember:stuMemberList
        ) {
            if (userProjectGroupMapper.updateUserInfo(stuMember,new Date(),projectId) != 0){
                counter ++;
            }
        }
        //更新条数和传入数值不一致，则说明信息不匹配,同理，老师的信息也是
        if (counter != stuMemberList.size()) {
            throw new GlobalException(CodeMsg.USER_INFORMATION_MATCH_ERROR);
        }
        counter = 0;
        for (TeacherMember teacher:form.getTeachers()
        ) {
            if (userProjectGroupMapper.updateTeacherTechnicalRole(teacher,projectId) != 0){
                counter ++;
            }
        }
        if (counter != stuMemberList.size()) {
            throw new GlobalException(CodeMsg.USER_INFORMATION_MATCH_ERROR);
        }

        return Result.success();
    }

    @Override
    public Result getKeyProjectApplyingListByGuideTeacher() {
        User user = getUserService.getCurrentUser();
        if (user == null){
            throw new GlobalException(CodeMsg.AUTHENTICATION_ERROR);
        }
        Long userId = Long.valueOf(user.getCode());
        List<KeyProjectDTO> list = keyProjectStatusMapper.getKeyProjectListByUserId(userId,KeyProjectStatus.TO_DE_CONFIRMED.getValue());
        return Result.success(list);
    }

}
