package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.DTO.KeyProjectDTO;
import com.swpu.uchain.openexperiment.DTO.OperationRecord;
import com.swpu.uchain.openexperiment.dao.KeyProjectStatusMapper;
import com.swpu.uchain.openexperiment.dao.OperationRecordMapper;
import com.swpu.uchain.openexperiment.dao.ProjectGroupMapper;
import com.swpu.uchain.openexperiment.dao.UserProjectGroupMapper;
import com.swpu.uchain.openexperiment.domain.ProjectGroup;
import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.enums.*;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import com.swpu.uchain.openexperiment.form.check.KeyProjectCheck;
import com.swpu.uchain.openexperiment.form.project.KeyProjectApplyForm;
import com.swpu.uchain.openexperiment.form.user.StuMember;
import com.swpu.uchain.openexperiment.form.user.TeacherMember;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.GetUserService;
import com.swpu.uchain.openexperiment.service.KeyProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.LinkedList;
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
    private OperationRecordMapper operationRecordMapper;

    @Autowired
    public KeyProjectServiceImpl(ProjectGroupMapper projectGroupMapper, UserProjectGroupMapper userProjectGroupMapper,
                                 KeyProjectStatusMapper keyProjectStatusMapper,GetUserService getUserService,
                                 OperationRecordMapper operationRecordMapper) {
        this.projectGroupMapper = projectGroupMapper;
        this.userProjectGroupMapper = userProjectGroupMapper;
        this.keyProjectStatusMapper = keyProjectStatusMapper;
        this.getUserService = getUserService;
        this.operationRecordMapper = operationRecordMapper;
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

    @Override
    public Result getKeyProjectApplyingListByLabAdmin() {
        List<KeyProjectDTO> list = keyProjectStatusMapper.getProjectIdListByStatusAndCollege(KeyProjectStatus.GUIDE_TEACHER_ALLOWED.getValue(),null);
        return Result.success(list);
    }

    @Override
    public Result getKeyProjectApplyingListBySecondaryUnit() {
        List<KeyProjectDTO> list = keyProjectStatusMapper.getProjectIdListByStatusAndCollege(KeyProjectStatus.LAB_ALLOWED_AND_REPORTED.getValue(),null);
        return Result.success(list);
    }

    @Override
    public Result getKeyProjectApplyingListByFunctionalDepartment() {
        List<KeyProjectDTO> list = keyProjectStatusMapper.getProjectIdListByStatusAndCollege(KeyProjectStatus.SECONDARY_UNIT_ALLOWED_AND_REPORTED.getValue(),null);
        return Result.success(list);
    }

    private KeyProjectStatus getNextStatusByRoleAndOperation(RoleType roleType,OperationType operationType){
        KeyProjectStatus keyProjectStatus;
        if (operationType == OperationType.REJECT) {
            keyProjectStatus = KeyProjectStatus.REJECT_MODIFY;
            return keyProjectStatus;
        }
        switch (roleType.getValue()){
            //如果是指导老师
            case 3:
                keyProjectStatus = KeyProjectStatus.GUIDE_TEACHER_ALLOWED;
                    break;
            //如果是实验室
            case 4:
                if (operationType == OperationType.AGREE){
                    keyProjectStatus = KeyProjectStatus.LAB_ALLOWED;
                }else {
                    keyProjectStatus = KeyProjectStatus.LAB_ALLOWED_AND_REPORTED;
                }
                break;
                //如果是二级单位
            case 5:
                if (operationType == OperationType.AGREE){
                    keyProjectStatus = KeyProjectStatus.SECONDARY_UNIT_ALLOWED;
                }else {
                    keyProjectStatus = KeyProjectStatus.SECONDARY_UNIT_ALLOWED_AND_REPORTED;
                }
                break;
                //如果是职能部门
            case 6:
                keyProjectStatus = KeyProjectStatus.ESTABLISH;
                    break;
            default:
                throw new GlobalException(CodeMsg.UNKNOWN_ROLE_TYPE_AND_OPERATION_TYPE);
        }
        return keyProjectStatus;
    }

    private Result operateKeyProjectOfSpecifiedRoleAndOperation(RoleType roleType,OperationType operationType,
                                                   List<KeyProjectCheck> list){
        User user = getUserService.getCurrentUser();
        if (user == null){
            throw new GlobalException(CodeMsg.AUTHENTICATION_ERROR);
        }
        List<OperationRecord> operationRecordList = new LinkedList<>();
        List<Long> idList = new LinkedList<>();
        for (KeyProjectCheck check:list
        ) {
            OperationRecord operationRecord = new OperationRecord();
            operationRecord.setOperationUnit(roleType.getValue());
            operationRecord.setOperationType(operationType.getValue());
            operationRecord.setOperationReason(check.getReason());
            operationRecord.setOperationExecutorId(Long.valueOf(user.getCode()));
            operationRecord.setRelatedId(check.getProjectId());
            operationRecordList.add(operationRecord);

            idList.add(check.getProjectId());
        }
        keyProjectStatusMapper.updateList(idList,getNextStatusByRoleAndOperation(roleType, operationType).getValue());
        operationRecordMapper.multiInsert(operationRecordList);
        return Result.success();
    }

    @Override
    public Result agreeKeyProjectByGuideTeacher(List<KeyProjectCheck> list) {
        return operateKeyProjectOfSpecifiedRoleAndOperation(RoleType.MENTOR,OperationType.AGREE,list);
    }

    @Override
    public Result agreeKeyProjectByLabAdministrator(List<KeyProjectCheck> list) {
        return operateKeyProjectOfSpecifiedRoleAndOperation(RoleType.LAB_ADMINISTRATOR,OperationType.AGREE,list);
    }

    @Override
    public Result agreeKeyProjectBySecondaryUnit(List<KeyProjectCheck> list) {
        return operateKeyProjectOfSpecifiedRoleAndOperation(RoleType.SECONDARY_UNIT,OperationType.AGREE,list);
    }

    @Override
    public Result agreeKeyProjectByFunctionalDepartment(List<KeyProjectCheck> list) {
        return operateKeyProjectOfSpecifiedRoleAndOperation(RoleType.FUNCTIONAL_DEPARTMENT,OperationType.AGREE,list);
    }

    @Override
    public Result reportKeyProjectByLabAdministrator(List<KeyProjectCheck> list) {
        return operateKeyProjectOfSpecifiedRoleAndOperation(RoleType.LAB_ADMINISTRATOR,OperationType.REPORT,list);
    }

    @Override
    public Result reportKeyProjectBySecondaryUnit(List<KeyProjectCheck> list) {
        return operateKeyProjectOfSpecifiedRoleAndOperation(RoleType.SECONDARY_UNIT,OperationType.REPORT,list);
    }


    @Override
    public Result rejectKeyProjectByGuideTeacher(List<KeyProjectCheck> list) {
        return operateKeyProjectOfSpecifiedRoleAndOperation(RoleType.MENTOR,OperationType.REJECT,list);
    }

    @Override
    public Result rejectKeyProjectByLabAdministrator(List<KeyProjectCheck> list) {
        return operateKeyProjectOfSpecifiedRoleAndOperation(RoleType.LAB_ADMINISTRATOR,OperationType.REJECT,list);
    }

    @Override
    public Result rejectKeyProjectBySecondaryUnit(List<KeyProjectCheck> list) {
        return operateKeyProjectOfSpecifiedRoleAndOperation(RoleType.SECONDARY_UNIT,OperationType.REJECT,list);
    }

    @Override
    public Result rejectKeyProjectByFunctionalDepartment(List<KeyProjectCheck> list) {
        return operateKeyProjectOfSpecifiedRoleAndOperation(RoleType.FUNCTIONAL_DEPARTMENT,OperationType.REJECT,list);
    }


}
