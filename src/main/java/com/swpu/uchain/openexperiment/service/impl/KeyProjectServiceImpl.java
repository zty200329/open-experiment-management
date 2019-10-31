package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.DTO.KeyProjectDTO;
import com.swpu.uchain.openexperiment.DTO.OperationRecord;
import com.swpu.uchain.openexperiment.DTO.ProjectHistoryInfo;
import com.swpu.uchain.openexperiment.dao.KeyProjectStatusMapper;
import com.swpu.uchain.openexperiment.dao.OperationRecordMapper;
import com.swpu.uchain.openexperiment.dao.ProjectGroupMapper;
import com.swpu.uchain.openexperiment.dao.UserProjectGroupMapper;
import com.swpu.uchain.openexperiment.domain.ProjectGroup;
import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.enums.*;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import com.swpu.uchain.openexperiment.form.query.HistoryQueryKeyProjectInfo;
import com.swpu.uchain.openexperiment.form.check.KeyProjectCheck;
import com.swpu.uchain.openexperiment.form.project.KeyProjectApplyForm;
import com.swpu.uchain.openexperiment.form.user.StuMember;
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
        ProjectGroup projectGroup = projectGroupMapper.selectByPrimaryKey(form.getProjectId());
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

        if (stuMemberList!=null){
            for (StuMember stuMember:stuMemberList
            ) {
                userProjectGroupMapper.updateUserInfo(stuMember, new Date(), projectId);
            }
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
         return getKeyProjectDTOListByStatusAndCollege(KeyProjectStatus.GUIDE_TEACHER_ALLOWED,null);
    }

    @Override
    public Result getKeyProjectApplyingListBySecondaryUnit() {
        return getKeyProjectDTOListByStatusAndCollege(KeyProjectStatus.LAB_ALLOWED_AND_REPORTED,null);
    }

    @Override
    public Result getKeyProjectApplyingListByFunctionalDepartment() {
        return getKeyProjectDTOListByStatusAndCollege(KeyProjectStatus.SECONDARY_UNIT_ALLOWED_AND_REPORTED,null);
    }

    private Result getKeyProjectDTOListByStatusAndCollege(KeyProjectStatus status,CollegeType college){
        Integer collegeOfInteger = null;
        if (college!=null){
            collegeOfInteger = college.getValue();
        }
        List<KeyProjectDTO> list = keyProjectStatusMapper.getKeyProjectDTOListByStatusAndCollege(status.getValue(),collegeOfInteger);
        for (KeyProjectDTO keyProjectDTO :list
             ) {
            keyProjectDTO.setNumberOfTheSelected(userProjectGroupMapper.getMemberAmountOfProject(keyProjectDTO.getId(),null));
            keyProjectDTO.setMemberVOList(userProjectGroupMapper.selectUserMemberVOListByMemberRoleAndProjectId(null,keyProjectDTO.getId()));
        }
        return Result.success(list);
    }

    private KeyProjectStatus getNextStatusByRoleAndOperation(RoleType roleType,OperationTypeOfKetProject operationType){
        KeyProjectStatus keyProjectStatus;
        if (operationType == OperationTypeOfKetProject.REJECT) {
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
                if (operationType == OperationTypeOfKetProject.AGREE){
                    keyProjectStatus = KeyProjectStatus.LAB_ALLOWED;
                }else {
                    keyProjectStatus = KeyProjectStatus.LAB_ALLOWED_AND_REPORTED;
                }
                break;
                //如果是二级单位
            case 5:
                if (operationType == OperationTypeOfKetProject.AGREE){
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

    private Result operateKeyProjectOfSpecifiedRoleAndOperation(RoleType roleType,OperationTypeOfKetProject operationType,
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
        return operateKeyProjectOfSpecifiedRoleAndOperation(RoleType.MENTOR,OperationTypeOfKetProject.AGREE,list);
    }

    @Override
    public Result agreeKeyProjectByLabAdministrator(List<KeyProjectCheck> list) {
        return operateKeyProjectOfSpecifiedRoleAndOperation(RoleType.LAB_ADMINISTRATOR,OperationTypeOfKetProject.AGREE,list);
    }

    @Override
    public Result agreeKeyProjectBySecondaryUnit(List<KeyProjectCheck> list) {
        return operateKeyProjectOfSpecifiedRoleAndOperation(RoleType.SECONDARY_UNIT,OperationTypeOfKetProject.AGREE,list);
    }

    @Override
    public Result agreeKeyProjectByFunctionalDepartment(List<KeyProjectCheck> list) {
        return operateKeyProjectOfSpecifiedRoleAndOperation(RoleType.FUNCTIONAL_DEPARTMENT,OperationTypeOfKetProject.AGREE,list);
    }

    @Override
    public Result reportKeyProjectByLabAdministrator(List<KeyProjectCheck> list) {
        return operateKeyProjectOfSpecifiedRoleAndOperation(RoleType.LAB_ADMINISTRATOR,OperationTypeOfKetProject.REPORT,list);
    }

    @Override
    public Result reportKeyProjectBySecondaryUnit(List<KeyProjectCheck> list) {
        return operateKeyProjectOfSpecifiedRoleAndOperation(RoleType.SECONDARY_UNIT,OperationTypeOfKetProject.REPORT,list);
    }


    @Override
    public Result rejectKeyProjectByGuideTeacher(List<KeyProjectCheck> list) {
        return operateKeyProjectOfSpecifiedRoleAndOperation(RoleType.MENTOR,OperationTypeOfKetProject.REJECT,list);
    }

    @Override
    public Result getHistoricalKeyProjectInfo(HistoryQueryKeyProjectInfo info) {

        // TODO 权限验证

        List<ProjectGroup> list = projectGroupMapper.selectHistoricalInfoByUnitAndOperation(info.getOperationUnit(),info.getOperationType());
        for (ProjectGroup projectGroup:list
        ) {
            projectGroup.setNumberOfTheSelected(userProjectGroupMapper.getMemberAmountOfProject(projectGroup.getId(),null));
            projectGroup.setMemberVOList(userProjectGroupMapper.selectUserMemberVOListByMemberRoleAndProjectId(MemberRole.GUIDANCE_TEACHER.getValue(),projectGroup.getId()));
        }
        return Result.success(list);
    }

    @Override
    public Result getToBeReportedProjectByLabAdmin() {
        return getKeyProjectDTOListByStatusAndCollege(KeyProjectStatus.LAB_ALLOWED,null);
    }

    @Override
    public Result getToBeReportedProjectBySecondaryUnit() {
        return getKeyProjectDTOListByStatusAndCollege(KeyProjectStatus.SECONDARY_UNIT_ALLOWED,null);
    }


    @Override
    public Result rejectKeyProjectByLabAdministrator(List<KeyProjectCheck> list) {
        return operateKeyProjectOfSpecifiedRoleAndOperation(RoleType.LAB_ADMINISTRATOR,OperationTypeOfKetProject.REJECT,list);
    }

    @Override
    public Result rejectKeyProjectBySecondaryUnit(List<KeyProjectCheck> list) {
        return operateKeyProjectOfSpecifiedRoleAndOperation(RoleType.SECONDARY_UNIT,OperationTypeOfKetProject.REJECT,list);
    }

    @Override
    public Result rejectKeyProjectByFunctionalDepartment(List<KeyProjectCheck> list) {
        return operateKeyProjectOfSpecifiedRoleAndOperation(RoleType.FUNCTIONAL_DEPARTMENT,OperationTypeOfKetProject.REJECT,list);
    }

    @Override
    public Result getKeyProjectDetailById(Long projectId) {
        List<ProjectHistoryInfo> list = operationRecordMapper.selectAllOfKeyProjectByProjectId(projectId);
        return Result.success(list);
    }
}
