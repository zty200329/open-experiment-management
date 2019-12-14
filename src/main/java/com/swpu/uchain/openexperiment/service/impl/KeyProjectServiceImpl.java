package com.swpu.uchain.openexperiment.service.impl;

import com.sun.org.apache.xpath.internal.operations.Operation;
import com.swpu.uchain.openexperiment.DTO.KeyProjectDTO;
import com.swpu.uchain.openexperiment.DTO.OperationRecord;
import com.swpu.uchain.openexperiment.DTO.ProjectHistoryInfo;
import com.swpu.uchain.openexperiment.VO.limit.AmountAndTypeVO;
import com.swpu.uchain.openexperiment.VO.limit.AmountLimitVO;
import com.swpu.uchain.openexperiment.domain.*;
import com.swpu.uchain.openexperiment.form.amount.AmountAndType;
import com.swpu.uchain.openexperiment.mapper.*;
import com.swpu.uchain.openexperiment.enums.*;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import com.swpu.uchain.openexperiment.form.query.HistoryQueryKeyProjectInfo;
import com.swpu.uchain.openexperiment.form.check.KeyProjectCheck;
import com.swpu.uchain.openexperiment.form.project.KeyProjectApplyForm;
import com.swpu.uchain.openexperiment.form.query.QueryConditionForm;
import com.swpu.uchain.openexperiment.form.user.StuMember;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.GetUserService;
import com.swpu.uchain.openexperiment.service.KeyProjectService;
import com.swpu.uchain.openexperiment.service.TimeLimitService;
import com.swpu.uchain.openexperiment.util.SerialNumberUtil;
import io.swagger.models.auth.In;
import net.sf.jsqlparser.statement.select.Join;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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
    private TimeLimitService timeLimitService;
    private AmountLimitMapper amountLimitMapper;
    private ProjectFileMapper projectFileMapper;
    private UserRoleMapper userRoleMapper;

    @Autowired
    public KeyProjectServiceImpl(ProjectGroupMapper projectGroupMapper, UserProjectGroupMapper userProjectGroupMapper,
                                 KeyProjectStatusMapper keyProjectStatusMapper,GetUserService getUserService,
                                 OperationRecordMapper operationRecordMapper,TimeLimitService timeLimitService,
                                 AmountLimitMapper amountLimitMapper,ProjectFileMapper projectFileMapper,
                                 UserRoleMapper userRoleMapper) {
        this.projectGroupMapper = projectGroupMapper;
        this.userProjectGroupMapper = userProjectGroupMapper;
        this.keyProjectStatusMapper = keyProjectStatusMapper;
        this.getUserService = getUserService;
        this.operationRecordMapper = operationRecordMapper;
        this.timeLimitService = timeLimitService;
        this.amountLimitMapper = amountLimitMapper;
        this.projectFileMapper = projectFileMapper;
        this.userRoleMapper = userRoleMapper;
    }

    @Transactional(rollbackFor = GlobalException.class)
    @Override
    public Result createKeyApply(KeyProjectApplyForm form) {

        //用户信息不完整
        User user = getUserService.getCurrentUser();
        if (user.getMobilePhone() == null) {
            throw new GlobalException(CodeMsg.USER_INFO_NOT_COMPLETE);
        }

        //验证项目是否存在
        ProjectGroup projectGroup = projectGroupMapper.selectByPrimaryKey(form.getProjectId());
        if (projectGroup == null){
            throw new GlobalException(CodeMsg.PROJECT_GROUP_NOT_EXIST);
        }

        //判断项目是否为重点项目
        if (ProjectType.GENERAL.getValue().equals(projectGroup.getProjectType())){
            throw new GlobalException(CodeMsg.GENERAL_PROJECT_CANT_APPLY);
        }

        //文件上传验证，没有进行证明材料上传不得申请重点项目
        ProjectFile projectFile = projectFileMapper.selectByProjectGroupIdAndMaterialType(form.getProjectId(),MaterialType.APPLY_MATERIAL.getValue());
        if (projectFile == null) {
            throw new GlobalException(CodeMsg.KEY_PROJECT_APPLY_MATERIAL_EMPTY);
        }

        //验证用户是否有权限操作该项目组
        UserProjectGroup userProjectGroup = userProjectGroupMapper.selectByProjectGroupIdAndUserId(form.getProjectId(), Long.valueOf(user.getCode()));
        if (userProjectGroup == null || !userProjectGroup.getMemberRole().equals(MemberRole.PROJECT_GROUP_LEADER.getValue())){
            throw new GlobalException(CodeMsg.PERMISSION_DENNY);
        }

        //验证时间的合法性
        timeLimitService.validTime(TimeLimitType.KEY_DECLARE_LIMIT);

        //验证项目状态合法性
        Long projectId = projectGroup.getId();
        if (!projectGroup.getStatus().equals(ProjectStatus.LAB_ALLOWED.getValue()) &&
                //被指导教师或者是实验室驳回就是这个状态
            !projectGroup.getStatus().equals(ProjectStatus.KEY_PROJECT_APPLY.getValue())){
            throw new GlobalException(CodeMsg.PROJECT_IS_NOT_LAB_ALLOWED);
        }
        //验证是否已经进行了重点项目申请和是否被驳回，被驳回重点项目状态的空的
        Integer keyProjectStatus = keyProjectStatusMapper.getStatusByProjectId(projectGroup.getId());
        if (keyProjectStatus !=null && !keyProjectStatus.equals(ProjectStatus.REJECT_MODIFY.getValue())) {
            throw new GlobalException(CodeMsg.PROJECT_CURRENT_STATUS_ERROR);
        }
        //将项目组表中的项目状态变为重点项目申请  （之后的状态查询都在重点项目状态表中查询）
        projectGroupMapper.updateProjectStatus(form.getProjectId(),ProjectStatus.KEY_PROJECT_APPLY.getValue());

        if (keyProjectStatus == null) {
            //不存在则插入
            keyProjectStatusMapper.insert(projectId, ProjectStatus.TO_DE_CONFIRMED.getValue(),
                    projectGroup.getSubordinateCollege(), Long.valueOf(user.getCode()));
        }else {
            //存在则更新
            keyProjectStatusMapper.update(form.getProjectId(),ProjectStatus.TO_DE_CONFIRMED.getValue());
        }


        //将重点项目上报的操作存入历史
        OperationRecord operationRecord = new OperationRecord();
        operationRecord.setOperationUnit(RoleType.PROJECT_LEADER.getValue());
        operationRecord.setOperationType(OperationType.REPORT.getValue());
        operationRecord.setOperationExecutorId(Long.valueOf(user.getCode()));
        operationRecord.setRelatedId(projectGroup.getId());

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
        List<KeyProjectDTO> list = keyProjectStatusMapper.getKeyProjectListByUserIdAndProjectStatus(userId,ProjectStatus.TO_DE_CONFIRMED.getValue());
        for (KeyProjectDTO keyProjectDTO :list
        ) {
            keyProjectDTO.setNumberOfTheSelected(userProjectGroupMapper.getMemberAmountOfProject(keyProjectDTO.getId(),null));
            keyProjectDTO.setGuidanceTeachers(userProjectGroupMapper.selectUserMemberVOListByMemberRoleAndProjectId(MemberRole.GUIDANCE_TEACHER.getValue(),keyProjectDTO.getId(),JoinStatus.JOINED.getValue()));
        }
        return Result.success(list);
    }

    @Override
    public Result getKeyProjectApplyingListByLabAdmin() {
         User user  = getUserService.getCurrentUser();
         if ( !userRoleMapper.selectByUserId(Long.valueOf(user.getCode())).getRoleId().equals(RoleType.LAB_ADMINISTRATOR.getValue())) {
             throw new GlobalException(CodeMsg.PERMISSION_DENNY);
         }
         return getKeyProjectDTOListByStatusAndCollege(ProjectStatus.GUIDE_TEACHER_ALLOWED,user.getInstitute());
    }

    @Override
    public Result getKeyProjectApplyingListBySecondaryUnit() {
        User user  = getUserService.getCurrentUser();
        if ( !userRoleMapper.selectByUserId(Long.valueOf(user.getCode())).getRoleId().equals(RoleType.SECONDARY_UNIT.getValue())) {
            throw new GlobalException(CodeMsg.PERMISSION_DENNY);
        }
        return getKeyProjectDTOListByStatusAndCollege(ProjectStatus.LAB_ALLOWED_AND_REPORTED,user.getInstitute());
    }

    @Override
    public Result getKeyProjectApplyingListByFunctionalDepartment() {
        User user  = getUserService.getCurrentUser();
        if ( !userRoleMapper.selectByUserId(Long.valueOf(user.getCode())).getRoleId().equals(RoleType.SECONDARY_UNIT.getValue())) {
            throw new GlobalException(CodeMsg.PERMISSION_DENNY);
        }
        return getKeyProjectDTOListByStatusAndCollege(ProjectStatus.SECONDARY_UNIT_ALLOWED_AND_REPORTED,null);
    }

    @Override
    public Result getIntermediateInspectionKeyProject(Integer college) {
        return getKeyProjectDTOListByStatusAndCollege(ProjectStatus.ESTABLISH,college);
    }

    @Override
    public Result getToBeConcludingKeyProject(Integer college) {
        return getKeyProjectDTOListByStatusAndCollege(ProjectStatus.MID_TERM_INSPECTION,college);
    }

    private Result getKeyProjectDTOListByStatusAndCollege(ProjectStatus status, Integer college){
        List<KeyProjectDTO> list = keyProjectStatusMapper.getKeyProjectDTOListByStatusAndCollege(status.getValue(),college);
        for (KeyProjectDTO keyProjectDTO :list
             ) {
            keyProjectDTO.setNumberOfTheSelected(userProjectGroupMapper.selectStuCount(keyProjectDTO.getId(),JoinStatus.JOINED.getValue()) );
            keyProjectDTO.setGuidanceTeachers(userProjectGroupMapper.selectUserMemberVOListByMemberRoleAndProjectId(MemberRole.GUIDANCE_TEACHER.getValue(),keyProjectDTO.getId(),JoinStatus.JOINED.getValue()));
        }
        return Result.success(list);
    }

    private ProjectStatus getNextStatusByRoleAndOperation(RoleType roleType, OperationType operationType){
        ProjectStatus keyProjectStatus;
        //如果是二级单位 则是立项失败
        if (operationType == OperationType.REJECT && (roleType == RoleType.SECONDARY_UNIT || roleType == RoleType.FUNCTIONAL_DEPARTMENT)) {
            keyProjectStatus = ProjectStatus.ESTABLISH_FAILED;
            return keyProjectStatus;
        }

        //如果是二级单位和职能部门上报驳回则是立项失败
        if (operationType == OperationType.REPORT_REJECT && (roleType == RoleType.SECONDARY_UNIT ||
                roleType == RoleType.FUNCTIONAL_DEPARTMENT)) {
            return ProjectStatus.ESTABLISH_FAILED;
        }

        switch (roleType.getValue()){
            //如果是指导老师
            case 3:
                keyProjectStatus = ProjectStatus.GUIDE_TEACHER_ALLOWED;
                    break;
            //如果是实验室
            case 4:
                if (operationType == OperationType.AGREE){
                    keyProjectStatus = ProjectStatus.LAB_ALLOWED;
                }else {
                    keyProjectStatus = ProjectStatus.LAB_ALLOWED_AND_REPORTED;
                }
                break;
                //如果是二级单位
            case 5:
                if (operationType == OperationType.AGREE){
                    keyProjectStatus = ProjectStatus.SECONDARY_UNIT_ALLOWED;
                }else {
                    keyProjectStatus = ProjectStatus.SECONDARY_UNIT_ALLOWED_AND_REPORTED;
                }
                break;
                //如果是职能部门
            case 6:
                keyProjectStatus = ProjectStatus.ESTABLISH;
                    break;
            default:
                throw new GlobalException(CodeMsg.UNKNOWN_ROLE_TYPE_AND_OPERATION_TYPE);
        }
        return keyProjectStatus;
    }

    private Result operateKeyProjectOfSpecifiedRoleAndOperation(RoleType roleType, OperationType operationType,
                                                                List<KeyProjectCheck> list){
        User user = getUserService.getCurrentUser();
        if (user == null){
            throw new GlobalException(CodeMsg.AUTHENTICATION_ERROR);
        }

        //需要生成编号，通过操作人来判断学院信息
        Integer college = getUserService.getCurrentUser().getInstitute();
        if (college == null){
            throw new GlobalException(CodeMsg.COLLEGE_TYPE_NULL_ERROR);
        }

        List<OperationRecord> operationRecordList = new LinkedList<>();
        List<Long> idList = new LinkedList<>();
        for (KeyProjectCheck check:list
        ) {
            UserProjectGroup userProjectGroup = userProjectGroupMapper.selectByProjectGroupIdAndUserId(check.getProjectId(), Long.valueOf(user.getCode()));

            //验证属于该项目并且是该项目的指导教师
            if (userProjectGroup == null || !userProjectGroup.getMemberRole().equals(MemberRole.GUIDANCE_TEACHER.getValue())) {
                throw new GlobalException(CodeMsg.PERMISSION_DENNY);
            }

            if ( projectFileMapper.selectByProjectGroupIdAndMaterialType(check.getProjectId(),MaterialType.APPLY_MATERIAL.getValue()) == null) {
                throw new GlobalException(CodeMsg.KEY_PROJECT_APPLY_MATERIAL_EMPTY);
            }

            OperationRecord operationRecord = new OperationRecord();
            operationRecord.setOperationUnit(roleType.getValue());
            operationRecord.setOperationType(operationType.getValue());
            operationRecord.setOperationReason(check.getReason());
            operationRecord.setOperationExecutorId(Long.valueOf(user.getCode()));
            operationRecord.setRelatedId(check.getProjectId());
            operationRecord.setOperationCollege(user.getInstitute());

            //批量插入
            operationRecordList.add(operationRecord);

            idList.add(check.getProjectId());
        }
        if (operationType == OperationType.REJECT && (roleType == RoleType.LAB_ADMINISTRATOR || roleType == RoleType.MENTOR)) {
            for (KeyProjectCheck check:list
            ) {
                //删除重点项目，回到拟题通过状态
                keyProjectStatusMapper.deleteByProjectId(check.getProjectId());
                projectGroupMapper.updateProjectStatus(check.getProjectId(),ProjectStatus.LAB_ALLOWED.getValue());
            }
        }else {
            Integer nextProjectStatus = getNextStatusByRoleAndOperation(roleType, operationType).getValue();
            keyProjectStatusMapper.updateList(idList,nextProjectStatus);
        }
        operationRecordMapper.multiInsert(operationRecordList);
        return Result.success();
    }

    @Override
    public Result agreeKeyProjectByGuideTeacher(List<KeyProjectCheck> list) {
        //验证时间的合法性
        timeLimitService.validTime(TimeLimitType.TEACHER_KEY_CHECK_LIMIT);

        return operateKeyProjectOfSpecifiedRoleAndOperation(RoleType.MENTOR, OperationType.AGREE,list);
    }

    @Override
    public Result agreeKeyProjectByLabAdministrator(List<KeyProjectCheck> list) {
        //重点项目审核同意后直接相当于上报
        return operateKeyProjectOfSpecifiedRoleAndOperation(RoleType.LAB_ADMINISTRATOR, OperationType.REPORT,list);
    }

    @Override
    public Result agreeKeyProjectBySecondaryUnit(List<KeyProjectCheck> list) {
        return operateKeyProjectOfSpecifiedRoleAndOperation(RoleType.SECONDARY_UNIT, OperationType.AGREE,list);
    }

    @Override
    public Result agreeKeyProjectByFunctionalDepartment(List<KeyProjectCheck> list) {
        return operateKeyProjectOfSpecifiedRoleAndOperation(RoleType.FUNCTIONAL_DEPARTMENT, OperationType.AGREE,list);
    }

    @Override
    public Result agreeIntermediateInspectionKeyProject(List<KeyProjectCheck> list) {
        return operateKeyProjectOfSpecifiedRoleAndOperation(RoleType.FUNCTIONAL_DEPARTMENT, OperationType.OFFLINE_CHECK,list);
    }

    @Override
    public Result agreeToBeConcludingKeyProject(List<KeyProjectCheck> list) {
        return operateKeyProjectOfSpecifiedRoleAndOperation(RoleType.FUNCTIONAL_DEPARTMENT, OperationType.CONCLUSION,list);
    }

    @Override
    public Result reportKeyProjectByLabAdministrator(List<KeyProjectCheck> list) {
        return operateKeyProjectOfSpecifiedRoleAndOperation(RoleType.LAB_ADMINISTRATOR, OperationType.REPORT,list);
    }

    @Override
    public Result rejectIntermediateInspectionKeyProject(List<KeyProjectCheck> list) {
        return operateKeyProjectOfSpecifiedRoleAndOperation(RoleType.FUNCTIONAL_DEPARTMENT, OperationType.OFFLINE_CHECK_REJECT,list);
    }

    @Override
    public Result rejectToBeConcludingKeyProject(List<KeyProjectCheck> list) {
        return operateKeyProjectOfSpecifiedRoleAndOperation(RoleType.FUNCTIONAL_DEPARTMENT, OperationType.CONCLUSION_REJECT,list);
    }

    @Override
    public Result reportKeyProjectBySecondaryUnit(List<KeyProjectCheck> list) {
        //验证数量
        User user = getUserService.getCurrentUser();
        Integer college = user.getInstitute();
        if (college == null){
            throw new GlobalException(CodeMsg.COLLEGE_TYPE_NULL_ERROR);
        }

        //生成项目编号
        for (KeyProjectCheck check : list) {
            String serialNumber = projectGroupMapper.getMaxSerialNumberByCollege(college);
            //计算编号并在数据库中插入编号
            projectGroupMapper.updateProjectSerialNumber(check.getProjectId(), SerialNumberUtil.getSerialNumberOfProject(college, ProjectType.KEY.getValue(), serialNumber));
        }

        AmountAndTypeVO amountAndTypeVO = amountLimitMapper.getAmountAndTypeVOByCollegeAndProjectType(college,ProjectType.KEY.getValue(),RoleType.SECONDARY_UNIT.getValue());
        Integer currentAmount = keyProjectStatusMapper.getCountOfSpecifiedStatusAndProjectProject(ProjectStatus.SECONDARY_UNIT_ALLOWED_AND_REPORTED.getValue(),college);
        if (currentAmount + list.size() > amountAndTypeVO.getMaxAmount()) {
            throw new GlobalException(CodeMsg.KEY_PROJECT_AMOUNT_LIMIT);
        }

        return operateKeyProjectOfSpecifiedRoleAndOperation(RoleType.SECONDARY_UNIT, OperationType.REPORT,list);
    }


    @Override
    public Result rejectKeyProjectByGuideTeacher(List<KeyProjectCheck> list) {
        return operateKeyProjectOfSpecifiedRoleAndOperation(RoleType.MENTOR, OperationType.REJECT,list);
    }

    @Override
    public Result getHistoricalKeyProjectInfo(HistoryQueryKeyProjectInfo info) {

        User user = getUserService.getCurrentUser();
        Integer college = user.getInstitute();
        //职能部门不需要学院信息
        if (info.getOperationUnit().equals(OperationUnit.FUNCTIONAL_DEPARTMENT.getValue())) {
            college = null;
        }

        List<ProjectGroup> list = projectGroupMapper.selectKeyHistoricalInfoByUnitAndOperation(info.getOperationUnit(),info.getOperationType(),college);
        for (ProjectGroup projectGroup:list
        ) {
            projectGroup.setNumberOfTheSelected(userProjectGroupMapper.getMemberAmountOfProject(projectGroup.getId(),null));
            projectGroup.setGuidanceTeachers(userProjectGroupMapper.selectUserMemberVOListByMemberRoleAndProjectId(MemberRole.GUIDANCE_TEACHER.getValue(),projectGroup.getId(),JoinStatus.JOINED.getValue()));
        }
        return Result.success(list);
    }

    @Override
    public Result getToBeReportedProjectByLabAdmin() {
        return getKeyProjectDTOListByStatusAndCollege(ProjectStatus.LAB_ALLOWED,null);
    }

    @Override
    public Result getToBeReportedProjectBySecondaryUnit() {
        return getKeyProjectDTOListByStatusAndCollege(ProjectStatus.SECONDARY_UNIT_ALLOWED,null);
    }


    @Override
    public Result rejectKeyProjectByLabAdministrator(List<KeyProjectCheck> list) {
        UserRole userRole = userRoleMapper.selectByUserId(Long.valueOf(getUserService.getCurrentUser().getCode()));
        if (!userRole.getRoleId().equals(RoleType.LAB_ADMINISTRATOR.getValue())) {
            throw new GlobalException(CodeMsg.PERMISSION_DENNY);
        }
        return operateKeyProjectOfSpecifiedRoleAndOperation(RoleType.LAB_ADMINISTRATOR, OperationType.REJECT,list);
    }

    @Override
    public Result rejectKeyProjectBySecondaryUnit(List<KeyProjectCheck> list) {
        return operateKeyProjectOfSpecifiedRoleAndOperation(RoleType.SECONDARY_UNIT, OperationType.REJECT,list);
    }

    @Override
    public Result rejectKeyProjectReportBySecondaryUnit(List<KeyProjectCheck> list) {
        return operateKeyProjectOfSpecifiedRoleAndOperation(RoleType.SECONDARY_UNIT, OperationType.REPORT_REJECT,list);
    }

    @Override
    public Result rejectKeyProjectByFunctionalDepartment(List<KeyProjectCheck> list) {
        return operateKeyProjectOfSpecifiedRoleAndOperation(RoleType.FUNCTIONAL_DEPARTMENT, OperationType.REJECT,list);
    }

    @Override
    public Result getKeyProjectDetailById(Long projectId) {
        List<ProjectHistoryInfo> list = operationRecordMapper.selectAllOfKeyProjectByProjectId(projectId);
        return Result.success(list);
    }

    @Override
    public Result conditionallyQueryOfKeyProject(QueryConditionForm form){
        return conditionallyQueryOfCheckedProject(form);
    }

    private Result conditionallyQueryOfCheckedProject(QueryConditionForm form) {
        List<Long>  projectIdList = projectGroupMapper.conditionQueryOfKeyProject(form);
        if (projectIdList.isEmpty()){
            return Result.success(null);
        }
        List<ProjectGroup> list = projectGroupMapper.selectAllByList(projectIdList);
        for (ProjectGroup projectGroup:list
        ) {
            Long id = projectGroup.getId();
            projectGroup.setNumberOfTheSelected(userProjectGroupMapper.getMemberAmountOfProject(id,null));
            projectGroup.setGuidanceTeachers(userProjectGroupMapper.selectUserMemberVOListByMemberRoleAndProjectId(null,id, JoinStatus.JOINED.getValue()));
        }
        return Result.success(list);
    }

    private void validProjectStatus(Integer currentStatus,Integer rightStatus){
        if (!rightStatus.equals(currentStatus)){
            throw new GlobalException(CodeMsg.CURRENT_PROJECT_STATUS_ERROR);
        }
    }
}
