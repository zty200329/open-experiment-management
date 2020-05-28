package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.DTO.OperationRecord;
import com.swpu.uchain.openexperiment.DTO.ProjectHistoryInfo;
import com.swpu.uchain.openexperiment.VO.limit.AmountAndTypeVO;
import com.swpu.uchain.openexperiment.VO.project.*;
import com.swpu.uchain.openexperiment.VO.user.UserMemberVO;
import com.swpu.uchain.openexperiment.config.UploadConfig;
import com.swpu.uchain.openexperiment.domain.*;
import com.swpu.uchain.openexperiment.enums.*;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import com.swpu.uchain.openexperiment.form.member.MemberQueryCondition;
import com.swpu.uchain.openexperiment.form.project.*;
import com.swpu.uchain.openexperiment.form.query.HistoryQueryProjectInfo;
import com.swpu.uchain.openexperiment.form.query.QueryConditionForm;
import com.swpu.uchain.openexperiment.mapper.*;
import com.swpu.uchain.openexperiment.redis.RedisService;
import com.swpu.uchain.openexperiment.redis.key.ProjectGroupKey;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.*;
import com.swpu.uchain.openexperiment.util.ConvertUtil;
import com.swpu.uchain.openexperiment.util.RedisUtil;
import com.swpu.uchain.openexperiment.util.SerialNumberUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


/**
 * @author panghu
 */
@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {

    private UserService userService;
    private ProjectGroupMapper projectGroupMapper;
    private RedisService redisService;
    private UserProjectService userProjectService;
    private ProjectFileService projectFileService;
    private FundsService fundsService;
    private UploadConfig uploadConfig;
    private ConvertUtil convertUtil;
    private GetUserService getUserService;
    private UserProjectGroupMapper userProjectGroupMapper;
    private RoleMapper roleMapper;
    private UserRoleService userRoleService;
    private OperationRecordMapper recordMapper;
    private UserMapper userMapper;
    private KeyProjectStatusMapper keyProjectStatusMapper;
    private ProjectFileMapper projectFileMapper;
    private TimeLimitService timeLimitService;
    private AmountLimitMapper amountLimitMapper;
    private HitBackMessageMapper hitBackMessageMapper;
    private RedisUtil redisUtil;

    @Autowired
    public ProjectServiceImpl(UserService userService, ProjectGroupMapper projectGroupMapper,
                              RedisService redisService, UserProjectService userProjectService,
                              ProjectFileService projectFileService, FundsService fundsService,
                              UploadConfig uploadConfig,
                              ConvertUtil convertUtil, GetUserService getUserService,
                              OperationRecordMapper recordMapper,
                              RoleMapper roleMapper, AmountLimitMapper amountLimitMapper,
                              UserProjectGroupMapper userProjectGroupMapper, UserMapper userMapper,
                              KeyProjectStatusMapper keyProjectStatusMapper, ProjectFileMapper projectFileMapper,
                              TimeLimitService timeLimitService,RedisUtil redisUtil, UserRoleService userRoleService, HitBackMessageMapper hitBackMessageMapper) {
        this.userService = userService;
        this.projectGroupMapper = projectGroupMapper;
        this.redisService = redisService;
        this.userProjectService = userProjectService;
        this.projectFileService = projectFileService;
        this.fundsService = fundsService;
        this.uploadConfig = uploadConfig;
        this.convertUtil = convertUtil;
        this.getUserService = getUserService;
        this.userProjectGroupMapper = userProjectGroupMapper;
        this.recordMapper = recordMapper;
        this.roleMapper = roleMapper;
        this.userMapper = userMapper;
        this.keyProjectStatusMapper = keyProjectStatusMapper;
        this.projectFileMapper = projectFileMapper;
        this.timeLimitService = timeLimitService;
        this.amountLimitMapper = amountLimitMapper;
        this.userRoleService = userRoleService;
        this.hitBackMessageMapper = hitBackMessageMapper;
        this.redisUtil = redisUtil;
    }

    @Override
    public boolean insert(ProjectGroup projectGroup) {
        return projectGroupMapper.insert(projectGroup) == 1;
    }

    @Override
    public boolean update(ProjectGroup projectGroup) {
        projectGroup.setUpdateTime(new Date());
        return projectGroupMapper.updateByPrimaryKey(projectGroup) == 1;
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
    public ProjectGroup selectByProjectGroupId(Long projectGroupId) {
        ProjectGroup projectGroup = projectGroupMapper.selectByPrimaryKey(projectGroupId);
        //重点项目状态表中不为空，设置值为重点项目状态
        if (projectGroup.getWhetherCommitKeyApply() != null) {
            projectGroup.setStatus(projectGroup.getWhetherCommitKeyApply());
        }
        return projectGroupMapper.selectByPrimaryKey(projectGroupId);
    }

    /**
     * 添加项目组
     *
     * @param projectGroup
     * @return
     */
    private Result addProjectGroup(ProjectGroup projectGroup) {
        projectGroup.setCreateTime(new Date());
        projectGroup.setUpdateTime(new Date());
        if (insert(projectGroup)) {
            return Result.success();
        }
        return Result.error(CodeMsg.ADD_ERROR);
    }

    @Override
    public List<ProjectGroup> selectByUserIdAndProjectStatus(Long userId, Integer projectStatus, Integer joinStatus) {
        //获取当前用户参与的所有项目
        return projectGroupMapper.selectByUserIdAndStatus(userId, projectStatus, joinStatus);
    }

    private List<CheckProjectVO> selectFunctionCreateCommonApply(){
        return projectGroupMapper.selectFunctionCreateCommonApply();
    }

    /**
     * 指导教师填写申请立项书
     * <p>
     * 提交
     *
     * @param form 申请立项表单
     * @return 申请立项操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result applyCreateProject(CreateProjectApplyForm form) {
        //验证时间限制
        timeLimitService.validTime(TimeLimitType.DECLARE_LIMIT);

        //验证人数限制
        if (form.getFitPeopleNum() > 6 || form.getFitPeopleNum() < 2) {
            throw new GlobalException(CodeMsg.FIT_PEOPLE_ERROR);
        }

        User currentUser = getUserService.getCurrentUser();

        //验证学院信息
        Integer college = currentUser.getInstitute();
        if (college == null) {
            throw new GlobalException(CodeMsg.COLLEGE_TYPE_NULL_ERROR);
        }

        //验证项目是否达到申请上限
        AmountAndTypeVO amountAndTypeVO = amountLimitMapper.getAmountAndTypeVOByCollegeAndProjectType(null, form.getProjectType(), RoleType.MENTOR.getValue());
        if (amountAndTypeVO != null) {
            Integer maxAmount = amountAndTypeVO.getMaxAmount();
            Integer currentCount = userProjectGroupMapper.geCountOfAppliedProject(Long.valueOf(currentUser.getCode()), form.getProjectType());
            if (maxAmount >= currentCount) {
                return Result.error(CodeMsg.MAXIMUM_APPLICATION);
            }
        }

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //固定时间
        try {
            Date startTime = dateFormat.parse("2019-12-05");
            form.setStartTime(startTime);
            Date endTime;
            if (form.getProjectType().equals(ProjectType.GENERAL.getValue())) {
                endTime = dateFormat.parse("2020-06-01");
            } else {
                endTime = dateFormat.parse("2020-11-01");
            }
            form.setEndTime(endTime);
        } catch (ParseException e) {
            throw new GlobalException(CodeMsg.TIME_DEFINE_ERROR);
        }

        //判断用户类型 判断是否为指导教师
        if (!userRoleService.validContainsUserRole(RoleType.MENTOR)) {
            throw new GlobalException(CodeMsg.ONLY_TEACHER_CAN_APPLY);
        }

        //开放选题时,不进行学生选择
        if (form.getIsOpenTopic().equals(OpenTopicType.OPEN_TOPIC_ALL.getValue()) && form.getStuCodes() != null) {
            throw new GlobalException(CodeMsg.TOPIC_IS_NOT_OPEN);
        }

        //时间设置出错
        if (form.getStartTime().getTime() >= form.getEndTime().getTime()) {
            throw new GlobalException(CodeMsg.TIME_DEFINE_ERROR);
        }

        ProjectGroup projectGroup = new ProjectGroup();
        BeanUtils.copyProperties(form, projectGroup);
        projectGroup.setStatus(ProjectStatus.DECLARE.getValue());
        //设置申请人
        projectGroup.setCreatorId(Long.valueOf(currentUser.getCode()));
        projectGroup.setSubordinateCollege(college);
        //插入数据
        Result result = addProjectGroup(projectGroup);
        if (result.getCode() != 0) {
            throw new GlobalException(CodeMsg.ADD_PROJECT_GROUP_ERROR);
        }

        //设置项目创建编号
        String maxTempSerialNumber = projectGroupMapper.getMaxTempSerialNumberByCollege(college);
        //计算编号并在数据库中插入编号
        projectGroupMapper.updateProjectTempSerialNumber(projectGroup.getId(), SerialNumberUtil.getSerialNumberOfProject(college, form.getProjectType(), maxTempSerialNumber));


        String[] teacherArray = new String[1];
        teacherArray[0] = currentUser.getCode();
        String secondTeacherCode = form.getAnotherTeacherCodes();
        if (secondTeacherCode != null) {
            teacherArray = new String[2];
            teacherArray[0] = currentUser.getCode();
            teacherArray[1] = secondTeacherCode;
            //判定用户是否存在
            if (userMapper.selectByUserCode(secondTeacherCode) == null) {
                throw new GlobalException(CodeMsg.ADD_TEACHER_NOT_EXIST);
            }
        }


        String[] stuCodes = form.getStuCodes();
        //判定数量
        if (stuCodes != null && stuCodes.length > form.getFitPeopleNum()) {
//            for (String stuCode : stuCodes) {
//                //判断用户信息是否完整(当前教师和添加的教师)
//                User addUser = userMapper.selectByUserCode(stuCode);
//                if (addUser.getMobilePhone() == null) {
//                    throw new GlobalException(CodeMsg.ADD_USER_INFO_NOT_COMPLETE);
//                }
//            }
            throw new GlobalException(CodeMsg.FIT_PEOPLE_LIMIT_ERROR);
        }


        userProjectService.addStuAndTeacherJoin(stuCodes, teacherArray, projectGroup.getId());
        //记录申请信息
        OperationRecord operationRecord = new OperationRecord();
        operationRecord.setRelatedId(projectGroup.getId());
        operationRecord.setOperationType(OperationType.REPORT.getValue());
        operationRecord.setOperationUnit(OperationUnit.MENTOR.getValue());
        operationRecord.setOperationExecutorId(Long.valueOf(currentUser.getCode()));
        return Result.success();
    }

    /**
     * 职能部门内定 同指导教师相似
     *
     * @param form
     * @return
     */
    @Override
    public Result FunctionCreateCommonApply(FunctionCreateProjectApplyForm form) {
        //时间限制省略

        //验证人数限制
        if (form.getFitPeopleNum() > 6 || form.getFitPeopleNum() < 2) {
            throw new GlobalException(CodeMsg.FIT_PEOPLE_ERROR);
        }

        User currentUser = getUserService.getCurrentUser();

        //学院不验证

        //上限不验证

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        //固定时间
        try {
            Date startTime = dateFormat.parse("2019-12-05");
            form.setStartTime(startTime);
            Date endTime;
            if (form.getProjectType().equals(ProjectType.GENERAL.getValue())) {
                endTime = dateFormat.parse("2020-06-01");
            } else {
                endTime = dateFormat.parse("2020-11-01");
            }
            form.setEndTime(endTime);
        } catch (ParseException e) {
            throw new GlobalException(CodeMsg.TIME_DEFINE_ERROR);
        }

        //判断用户类型 判断是否为职能部门或职能部门领导
        if (!userRoleService.validContainsUserRole(RoleType.FUNCTIONAL_DEPARTMENT) &&
                !userRoleService.validContainsUserRole(RoleType.FUNCTIONAL_DEPARTMENT_LEADER)) {
            throw new GlobalException(CodeMsg.DOES_NOT_HAVE_DEFAULT_PROJECT_PERMISSIONS);
        }

        //时间设置出错
        if (form.getStartTime().getTime() >= form.getEndTime().getTime()) {
            throw new GlobalException(CodeMsg.TIME_DEFINE_ERROR);
        }
        ProjectGroup projectGroup = new ProjectGroup();
        BeanUtils.copyProperties(form, projectGroup);
        projectGroup.setStatus(ProjectStatus.ESTABLISH.getValue());
        //强行关闭
        projectGroup.setIsOpenTopic(2);

        //设置申请人
        projectGroup.setCreatorId(Long.valueOf(form.getTeacherCodes()));
        projectGroup.setSubordinateCollege(CollegeType.FUNCTIONAL_DEPARTMENT.getValue());
        //插入数据
        Result result = addProjectGroup(projectGroup);
        if (result.getCode() != 0) {
            throw new GlobalException(CodeMsg.ADD_PROJECT_GROUP_ERROR);
        }
        if(form.getProjectType() == 2){
            keyProjectStatusMapper.insert(projectGroup.getId(), ProjectStatus.ESTABLISH.getValue(),
                    projectGroup.getSubordinateCollege(), Long.valueOf(currentUser.getCode()));
        }
        String itemNumber = form.getItemNumber();
        projectGroupMapper.updateProjectTempSerialNumber(projectGroup.getId(),itemNumber);

        String[] teacherArray = new String[1];
        teacherArray[0] = form.getTeacherCodes();
        String secondTeacherCode = form.getAnotherTeacherCodes();
        if (secondTeacherCode != null) {
            teacherArray = new String[2];
            teacherArray[0] = form.getTeacherCodes();
            teacherArray[1] = secondTeacherCode;
            //判定用户是否存在
            if (userMapper.selectByUserCode(secondTeacherCode) == null) {
                throw new GlobalException(CodeMsg.ADD_TEACHER_NOT_EXIST);
            }
        }

        String[] stuCodes = form.getStuCodes();
        //判定数量
        if (stuCodes != null && stuCodes.length > form.getFitPeopleNum()) {
            throw new GlobalException(CodeMsg.FIT_PEOPLE_LIMIT_ERROR);
        }
        userProjectService.addStuAndTeacherJoin(stuCodes, teacherArray, projectGroup.getId());

        //记录申请信息
        OperationRecord operationRecord = new OperationRecord();
        operationRecord.setRelatedId(projectGroup.getId());
        operationRecord.setOperationType(OperationType.AGREE.getValue());
        operationRecord.setOperationUnit(OperationUnit.FUNCTIONAL_DEPARTMENT.getValue());
        operationRecord.setOperationExecutorId(Long.valueOf(currentUser.getCode()));
        recordMapper.insert(operationRecord);
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result applyUpdateProject(UpdateProjectApplyForm updateProjectApplyForm) {

        //验证时间限制
        timeLimitService.validTime(TimeLimitType.DECLARE_LIMIT);

        ProjectGroup projectGroup = selectByProjectGroupId(updateProjectApplyForm.getProjectGroupId());
        if (projectGroup == null) {
            return Result.error(CodeMsg.PROJECT_GROUP_NOT_EXIST);
        }
        User currentUser = getUserService.getCurrentUser();
        UserProjectGroup userProjectGroup = userProjectService.selectByProjectGroupIdAndUserId(updateProjectApplyForm.getProjectGroupId(), Long.valueOf(currentUser.getCode()));
        if (userProjectGroup == null) {
            return Result.error(CodeMsg.USER_NOT_IN_GROUP);
        }
        //状态不是申报或者退回修改不允许修改
        if (!(projectGroup.getStatus().equals(ProjectStatus.REJECT_MODIFY.getValue()) ||
                projectGroup.getStatus().equals(ProjectStatus.DECLARE.getValue()))) {
            return Result.error(CodeMsg.PROJECT_GROUP_INFO_CANT_CHANGE);
        }
        //修改的话将状态修改为申报状态
        projectGroup.setStatus(ProjectStatus.DECLARE.getValue());

        //设置创建者
        projectGroup.setCreatorId(Long.valueOf(currentUser.getCode()));
        //固定时间
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date startTime = dateFormat.parse("2019-12-05");
            updateProjectApplyForm.setStartTime(startTime);
            Date endTime;
            if (updateProjectApplyForm.getProjectType().equals(ProjectType.GENERAL.getValue())) {
                endTime = dateFormat.parse("2020-06-01");
            } else {
                endTime = dateFormat.parse("2020-11-01");
            }
            updateProjectApplyForm.setEndTime(endTime);
        } catch (ParseException e) {
            throw new GlobalException(CodeMsg.TIME_DEFINE_ERROR);
        }


        //更新项目组基本信息
        BeanUtils.copyProperties(updateProjectApplyForm, projectGroup);
        update(projectGroup);

        OperationRecord operationRecord = new OperationRecord();
        operationRecord.setOperationCollege(currentUser.getInstitute());
        operationRecord.setRelatedId(updateProjectApplyForm.getProjectGroupId());
        operationRecord.setOperationType(OperationType.MODIFY.getValue());
        operationRecord.setOperationUnit(OperationUnit.MENTOR.getValue());
        operationRecord.setOperationExecutorId(Long.valueOf(currentUser.getCode()));
        //设置执行人
        setOperationExecutor(operationRecord);

        //修改项目状态
        projectGroupMapper.updateProjectStatus(projectGroup.getId(), ProjectStatus.DECLARE.getValue());

        recordMapper.insert(operationRecord);
        return Result.success();
    }

    @Override
    public Result getCurrentUserProjects(Integer projectStatus, Integer joinStatus) {
        User currentUser = getUserService.getCurrentUser();


        List<ProjectGroup> projectGroups = selectByUserIdAndProjectStatus(Long.valueOf(currentUser.getCode()), projectStatus, joinStatus);
        //设置当前用户的所有项目VO
        List<MyProjectVO> projectVOS = new ArrayList<>();
        for (ProjectGroup projectGroup : projectGroups) {
            Integer numberOfSelectedStu = userProjectGroupMapper.selectStuCount(projectGroup.getId(), JoinStatus.JOINED.getValue());
            MyProjectVO myProjectVO = new MyProjectVO();
            Integer status = keyProjectStatusMapper.getStatusByProjectId(projectGroup.getId());


            BeanUtils.copyProperties(projectGroup, myProjectVO);
            myProjectVO.setMemberRole(userProjectGroupMapper.selectByProjectIdAndUserId(projectGroup.getId(), Long.valueOf(currentUser.getCode())).getMemberRole());
            myProjectVO.setId(projectGroup.getId());
            myProjectVO.setNumberOfTheSelected(numberOfSelectedStu);
            myProjectVO.setProjectDetails(getProjectDetails(projectGroup));
            if (status != null) {
                myProjectVO.setStatus(status);
            }
            projectVOS.add(myProjectVO);
        }
        return Result.success(projectVOS);
    }

    private ProjectDetails getProjectDetails(ProjectGroup projectGroup) {
        ProjectDetails projectDetails = new ProjectDetails();
        projectDetails.setLabName(projectGroup.getLabName());
        projectDetails.setAddress(projectGroup.getAddress());
        //设置创建人,即项目负责人
        User user = userService.selectByUserId(projectGroup.getCreatorId());
        UserProjectGroup userProjectGroup = userProjectService.selectByProjectGroupIdAndUserId(
                projectGroup.getId(),
                Long.valueOf(user.getCode()));
        projectDetails.setCreator(new UserMemberVO(
                Long.valueOf(user.getCode()),
                user.getRealName(),
                userProjectGroup.getMemberRole(), null, null));
        //设置项目的成员信息
        List<UserProjectGroup> userProjectGroups = userProjectService.selectByProjectGroupId(projectGroup.getId());
        List<UserMemberVO> members = new ArrayList<>();
        for (UserProjectGroup userProject : userProjectGroups) {
            User member = userService.selectByUserId(userProject.getUserId());
            //设置项目组组长
            if (userProject.getMemberRole().intValue() == MemberRole.PROJECT_GROUP_LEADER.getValue()) {
                projectDetails.setLeader(new UserMemberVO(
                        member.getId(),
                        member.getRealName(),
                        userProject.getMemberRole(), null, null));
            }
            UserMemberVO userMemberVO = new UserMemberVO();
            userMemberVO.setUserId(member.getId());
            userMemberVO.setUserName(member.getRealName());
            userMemberVO.setMemberRole(userProject.getMemberRole());
            members.add(userMemberVO);
        }
        projectDetails.setMembers(members);
        //设置项目资金详情
        List<Funds> fundsDetails = fundsService.getFundsDetails(projectGroup.getId());
        int applyAmount = 0, agreeAmount = 0;
        for (Funds fundsDetail : fundsDetails) {
            applyAmount += fundsDetail.getAmount();
            if (fundsDetail.getStatus().intValue() == FundsStatus.AGREED.getValue()) {
                agreeAmount += fundsDetail.getAmount();
            }
        }
        projectDetails.setTotalApplyFundsAmount(applyAmount);
        projectDetails.setTotalAgreeFundsAmount(agreeAmount);
        projectDetails.setFundsDetails(fundsDetails);
        List<ProjectFile> projectFiles = projectFileService.getProjectAllFiles(projectGroup.getId());
        projectDetails.setProjectFiles(projectFiles);
        return projectDetails;
    }

    @Override
    public Result agreeJoin(JoinForm[] joinForm) {
        for (JoinForm form : joinForm) {
            User user = userService.selectByUserId(form.getUserId());
            if (user == null) {
                return Result.error(CodeMsg.USER_NO_EXIST);
            }
            ProjectGroup projectGroup = selectByProjectGroupId(form.getProjectGroupId());
            if (projectGroup == null) {
                return Result.error(CodeMsg.PROJECT_GROUP_NOT_EXIST);
            }

            //数量限制
            Integer amountOfSelected = userProjectGroupMapper.selectStuCount(projectGroup.getId(), JoinStatus.JOINED.getValue());
            if (amountOfSelected >= projectGroup.getFitPeopleNum()) {
                throw new GlobalException(CodeMsg.PROJECT_USER_MAX_ERROR);
            }

            UserProjectGroup userProjectGroup = userProjectService
                    .selectByProjectGroupIdAndUserId(
                            form.getProjectGroupId(),
                            Long.valueOf(user.getCode()));
            //未申请用户不得加入
            if (userProjectGroup == null) {
                return Result.error(CodeMsg.USER_NOT_APPLYING);
            }
            //已加入用户不能再次加入
            if (userProjectGroup.getStatus().intValue() == JoinStatus.JOINED.getValue()) {
                return Result.error(CodeMsg.USER_HAD_JOINED);
            }
            //一倍拒绝的用户无法再次加入该项目
            if (userProjectGroup.getStatus().intValue() == JoinStatus.UN_PASS.getValue()) {
                return Result.error(CodeMsg.USER_HAD_BEEN_REJECTED);
            }
            userProjectGroup.setStatus(JoinStatus.JOINED.getValue());
            if (!userProjectService.update(userProjectGroup)) {
                return Result.error(CodeMsg.UPDATE_ERROR);
            }
        }
        return Result.success();
    }

    private Result updateProjectStatus(Long projectGroupId, Integer projectStatus) {
        ProjectGroup projectGroup = selectByProjectGroupId(projectGroupId);
        if (projectGroup == null) {
            throw new GlobalException(CodeMsg.PROJECT_GROUP_NOT_EXIST);
        }
        projectGroup.setStatus(projectStatus);

        //更新状态
        if (update(projectGroup)) {
            return Result.success();
        }
        return Result.error(CodeMsg.UPDATE_ERROR);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result agreeEstablish(List<ProjectCheckForm> list) {
        return setProjectStatusAndRecord(list, OperationType.AGREE, OperationUnit.FUNCTIONAL_DEPARTMENT);
    }

    /**
     * 同意中期检查
     *
     * @param list
     * @return
     */
    @Override
    public Result agreeIntermediateInspectionProject(List<ProjectCheckForm> list) {
        return setProjectStatusAndRecord(list, OperationType.OFFLINE_CHECK, OperationUnit.FUNCTIONAL_DEPARTMENT);
    }

    @Override
    public Result agreeToBeConcludingProject(List<ProjectCheckForm> list) {
        return setProjectStatusAndRecord(list, OperationType.CONCLUSION, OperationUnit.FUNCTIONAL_DEPARTMENT);
    }

    /**
     * 学院同意普通项目结题
     * @param list
     * @return
     */
    @Override
    public Result agreeCollegePassedTheExamination(List<ProjectCheckForm> list) {
        return setProjectStatusAndRecord(list, OperationType.COLLEGE_PASSED_THE_EXAMINATION, OperationUnit.LAB_ADMINISTRATOR);
    }


    /**
     * 复核通过
     *
     * @param list
     * @param operationType
     * @param operationUnit
     * @return
     */
    private Result setProjectStatusAndRecord(List<ProjectCheckForm> list, OperationType operationType, OperationUnit operationUnit) {
        List<OperationRecord> operationRecordS = new LinkedList<>();
        for (ProjectCheckForm projectCheckForm : list) {
            Result result = updateProjectStatus(projectCheckForm.getProjectId(), ProjectStatus.ESTABLISH.getValue());
            if (result.getCode() != 0) {
                throw new GlobalException(CodeMsg.UPDATE_ERROR);
            }
            OperationRecord operationRecord = new OperationRecord();

            operationRecord.setOperationType(operationType.getValue());
            operationRecord.setOperationUnit(operationUnit.getValue());
            operationRecord.setOperationReason(projectCheckForm.getReason());
            operationRecord.setRelatedId(projectCheckForm.getProjectId());
            operationRecordS.add(operationRecord);
            setOperationExecutor(operationRecord);
        }
        recordMapper.multiInsert(operationRecordS);
        return Result.success();
    }

    @Override
    public Result getApplyForm(Long projectGroupId) {

        ProjectGroup projectGroup = selectByProjectGroupId(projectGroupId);
        if (projectGroup == null) {
            return Result.error(CodeMsg.PROJECT_GROUP_NOT_EXIST);
        }
        List<User> users = userService.selectProjectJoinedUsers(projectGroupId);
        if (projectGroup.getProjectType().intValue() == ProjectType.KEY.getValue()) {
            ApplyKeyFormInfoVO applyKeyFormInfoVO = convertUtil.addUserDetailVO(users, ApplyKeyFormInfoVO.class);
            applyKeyFormInfoVO.setFundsDetails(fundsService.getFundsDetails(projectGroupId));
            applyKeyFormInfoVO.setCreatorName(userMapper.selectByUserCode(String.valueOf(projectGroup.getCreatorId())).getRealName());
            BeanUtils.copyProperties(projectGroup, applyKeyFormInfoVO);
            applyKeyFormInfoVO.setId(projectGroup.getId());
            return Result.success(applyKeyFormInfoVO);
        } else {
            ApplyGeneralFormInfoVO applyGeneralFormInfoVO = convertUtil.addUserDetailVO(users, ApplyGeneralFormInfoVO.class);
            BeanUtils.copyProperties(projectGroup, applyGeneralFormInfoVO);
            applyGeneralFormInfoVO.setId(projectGroup.getId());
            applyGeneralFormInfoVO.setCreatorName(userMapper.selectByUserCode(String.valueOf(projectGroup.getCreatorId())).getRealName());
            return Result.success(applyGeneralFormInfoVO);
        }
    }

//    @Override
//    public Result appendCreateApply(AppendApplyForm appendApplyForm) {
//        User currentUser = getUserService.getCurrentUser();
//        //获取用户所在的用户项目组信息
//        UserProjectGroup userProjectGroup = userProjectService.selectByProjectGroupIdAndUserId(
//                appendApplyForm.getProjectGroupId(), Long.valueOf(currentUser.getCode());
//        if (userProjectGroup == null) {
//            return Result.error(CodeMsg.USER_NOT_IN_GROUP);
//        }
//
//        if (userProjectGroup.getStatus() < 5){
//            throw new GlobalException(CodeMsg.FUNDS_NOT_EXIST);
//        }
//
//        //拒绝普通用户进行该项操作
//        if (userProjectGroup.getMemberRole().intValue() == MemberRole.NORMAL_MEMBER.getValue()) {
//            Result.error(CodeMsg.PERMISSION_DENNY);
//        }
//        FundForm[] fundsForms = appendApplyForm.getFundForms();
//        for (FundForm fundsForm : fundsForms) {
//            //资金id不为空进行更新操作
//            if (fundsForm.getFundsId() != null) {
//                Funds funds = fundsService.selectById(fundsForm.getFundsId());
//
//                if (funds == null) {
//                    return Result.error(CodeMsg.FUNDS_NOT_EXIST);
//                }
//                //申请通过的资金无进行更新操作
//                if (funds.getStatus().intValue() == FundsStatus.AGREED.getValue()) {
//                    return Result.error(CodeMsg.FUNDS_AGREE_CANT_CHANGE);
//                }
//                BeanUtils.copyProperties(fundsForm, funds);
//                funds.setUpdateTime(new Date());
//                if (!fundsService.update(funds)) {
//                    return Result.error(CodeMsg.UPDATE_ERROR);
//                }
//            } else {
//                //添加资金信息
//                Funds funds = new Funds();
//                BeanUtils.copyProperties(fundsForm, funds);
//                funds.setProjectGroupId(appendApplyForm.getProjectGroupId());
//                funds.setApplicantId(Long.valueOf(currentUser.getCode());
//                funds.setStatus(FundsStatus.APPLYING.getValue());
//                funds.setCreateTime(new Date());
//                funds.setUpdateTime(new Date());
//                if (!fundsService.insert(funds)) {
//                    return Result.error(CodeMsg.ADD_ERROR);
//                }
//            }
//        }
//        return Result.success();
//    }

    @Override
    public Result getPendingApprovalProjectByLabAdministrator() {
        return getCheckInfo(ProjectStatus.DECLARE);
    }

    @Override
    public Result getPendingApprovalProjectBySecondaryUnit() {
        return getCheckInfo(ProjectStatus.LAB_ALLOWED_AND_REPORTED);
    }

    @Override
    public Result getPendingApprovalProjectByFunctionalDepartment() {
        return getCheckInfo(ProjectStatus.SECONDARY_UNIT_ALLOWED_AND_REPORTED);
    }

    /**
     * 职能部门获取待结题项目
     *
     * @return
     */
    @Override
    public Result getToBeConcludingProject() {
        return getCheckInfo(ProjectStatus.ESTABLISH);
    }

    /**
     * 职能部门获取内定项目
     * @return
     */
    @Override
    public Result getFunctionCreateCommonApply() {
        User currentUser = getUserService.getCurrentUser();

        List<CheckProjectVO> checkProjectVOs = selectFunctionCreateCommonApply();
        log.info(checkProjectVOs.size()+"***********");

        for (CheckProjectVO checkProjectVO : checkProjectVOs) {
            List<UserProjectGroup> userProjectGroups = userProjectService.selectByProjectGroupId(checkProjectVO.getId());
            checkProjectVO.setNumberOfTheSelected(userProjectGroupMapper.getMemberAmountOfProject(checkProjectVO.getId(), null));
            List<UserMemberVO> guidanceTeachers = new ArrayList<>();
            List<UserMemberVO> memberStudents = new ArrayList<>();
            for (UserProjectGroup userProjectGroup : userProjectGroups) {
                UserMemberVO userMemberVO = new UserMemberVO();
                User user = userService.selectByUserId(userProjectGroup.getUserId());
                userMemberVO.setUserId(Long.valueOf(user.getCode()));
                userMemberVO.setUserName(user.getRealName());
                userMemberVO.setMemberRole(userProjectGroup.getMemberRole());
                //设置负责人(项目组长)
                switch (userProjectGroup.getMemberRole()) {
                    case 1:
                        guidanceTeachers.add(userMemberVO);
                        break;
                    case 2:
                        checkProjectVO.setGroupLeaderPhone(user.getMobilePhone());
                        memberStudents.add(userMemberVO);
                        break;
                    case 3:
                        memberStudents.add(userMemberVO);
                        break;
                    default:
                        break;
                }
                //设置立项申请文件的id
                ProjectFile applyProjectFile = projectFileService.getAimNameProjectFile(
                        userProjectGroup.getProjectGroupId(),
                        uploadConfig.getApplyFileName());
                if (applyProjectFile != null) {
                    checkProjectVO.setApplyFileId(applyProjectFile.getId());
                }
                checkProjectVO.setMemberStudents(memberStudents);
                checkProjectVO.setGuidanceTeachers(guidanceTeachers);
            }
        }
        return Result.success(checkProjectVOs);
    }

    /**
     * 根据关键字查询
     * @param Keyword
     * @return
     */
    @Override
    public Result selectByKeyword(String Keyword) {
        List<SelectByKeywordProjectVO> projectVOS = (List<SelectByKeywordProjectVO>) redisUtil.get("select"+Keyword);
        if(projectVOS == null || projectVOS.size()==0){
            projectVOS = projectGroupMapper.selectByKeyword(Keyword);
            if(projectVOS != null &&projectVOS.size()!=0){
                redisUtil.set("select"+Keyword,projectVOS,300);
                log.info("存入redis");
            }
        }
        return Result.success(projectVOS);
    }

    @Override
    public Result selectKeyProjectByKeyword(SelectByKeywordForm Keyword) {
        List<SelectByKeywordProjectVO> projectVOS = (List<SelectByKeywordProjectVO>) redisUtil.get("selectKey"+Keyword);
        if(projectVOS == null || projectVOS.size()==0){
            projectVOS = projectGroupMapper.keyProjectSelectByKeyword(Keyword.getKeyword());
            if(projectVOS != null &&projectVOS.size()!=0){
                redisUtil.set("select"+Keyword,projectVOS,300);
                log.info("存入redis");
            }
        }
        return Result.success(projectVOS);
    }

    @Override
    public Result getIntermediateInspectionProject() {
        //ESTABLISH
//        return getCheckInfo(ProjectStatus.MID_TERM_INSPECTION);
        return getCheckInfo(ProjectStatus.ESTABLISH);
    }

    @Override
    public Result collegeGetsTheItemsToBeCompleted() {
        //TODO 应该加入时间校验
        return getCheckInfo(ProjectStatus.ESTABLISH);
    }

    @Override
    public Result getMidTermReturnProject() {
        return getCheckInfo(ProjectStatus.INTERIM_RETURN_MODIFICATION);
    }


    private Result getReportInfo(Integer role) {

        User currentUser = getUserService.getCurrentUser();

        Integer projectStatus;
        switch (role) {
            //二级部门(学院领导)
            case 5:
                projectStatus = ProjectStatus.SECONDARY_UNIT_ALLOWED.getValue();
                break;
            //实验室主任
            case 4:
                projectStatus = ProjectStatus.LAB_ALLOWED.getValue();
                break;
            default:
                throw new GlobalException(CodeMsg.PROJECT_CURRENT_STATUS_ERROR);
        }
        //获取待上报的普通项目
        List<CheckProjectVO> checkProjectVOs = projectGroupMapper.selectApplyOrderByTime(projectStatus, ProjectType.GENERAL.getValue(), currentUser.getInstitute());
        for (CheckProjectVO checkProjectVO : checkProjectVOs) {
            List<UserProjectGroup> userProjectGroups = userProjectService.selectByProjectGroupId(checkProjectVO.getId());
            List<UserMemberVO> guidanceTeachers = new ArrayList<>();
            //如果是老师则加入数组
            for (UserProjectGroup userProjectGroup : userProjectGroups
            ) {
                if (userProjectGroup.getMemberRole().equals(MemberRole.GUIDANCE_TEACHER.getValue())) {
                    UserMemberVO userMemberVO = new UserMemberVO();
                    userMemberVO.setMemberRole(userProjectGroup.getMemberRole());
                    userMemberVO.setUserId(userProjectGroup.getUserId());
                    userMemberVO.setUserName(userMapper.selectByUserCode(String.valueOf(userProjectGroup.getUserId())).getRealName());
                    guidanceTeachers.add(userMemberVO);
                }
            }
            checkProjectVO.setGuidanceTeachers(guidanceTeachers);
            checkProjectVO.setNumberOfTheSelected(userProjectGroupMapper.selectStuCount(checkProjectVO.getId(), JoinStatus.JOINED.getValue()));
        }
        return Result.success(checkProjectVOs);
    }

    private Result getCheckInfo(ProjectStatus projectStatus) {
        Integer status = projectStatus.getValue();
        Integer projectType = ProjectType.GENERAL.getValue();
        //如果是实验室进行审批，则返回所有项目
        if (projectStatus == ProjectStatus.DECLARE) {
            projectType = null;
        }

        User currentUser = getUserService.getCurrentUser();

        List<CheckProjectVO> checkProjectVOs = projectGroupMapper.selectApplyOrderByTime(status, projectType, currentUser.getInstitute());
        for (CheckProjectVO checkProjectVO : checkProjectVOs) {
            List<UserProjectGroup> userProjectGroups = userProjectService.selectByProjectGroupId(checkProjectVO.getId());
            checkProjectVO.setNumberOfTheSelected(userProjectGroupMapper.getMemberAmountOfProject(checkProjectVO.getId(), null));
            List<UserMemberVO> guidanceTeachers = new ArrayList<>();
            List<UserMemberVO> memberStudents = new ArrayList<>();
            for (UserProjectGroup userProjectGroup : userProjectGroups) {
                UserMemberVO userMemberVO = new UserMemberVO();
                User user = userService.selectByUserId(userProjectGroup.getUserId());
                userMemberVO.setUserId(Long.valueOf(user.getCode()));
                userMemberVO.setUserName(user.getRealName());
                userMemberVO.setMemberRole(userProjectGroup.getMemberRole());
                //设置负责人(项目组长)
                switch (userProjectGroup.getMemberRole()) {
                    case 1:
                        guidanceTeachers.add(userMemberVO);
                        break;
                    case 2:
                        checkProjectVO.setGroupLeaderPhone(user.getMobilePhone());
                        memberStudents.add(userMemberVO);
                        break;
                    case 3:
                        memberStudents.add(userMemberVO);
                        break;
                    default:
                        break;
                }
                //设置立项申请文件的id
                ProjectFile applyProjectFile = projectFileService.getAimNameProjectFile(
                        userProjectGroup.getProjectGroupId(),
                        uploadConfig.getApplyFileName());
                if (applyProjectFile != null) {
                    checkProjectVO.setApplyFileId(applyProjectFile.getId());
                }
                checkProjectVO.setMemberStudents(memberStudents);
                checkProjectVO.setGuidanceTeachers(guidanceTeachers);
            }
        }
        return Result.success(checkProjectVOs);
    }

    /**
     * 验证项目状态
     *
     * @param projectIdList
     * @param status        正确的项目状态
     * @return
     */
    private boolean checkProjectStatus(List<Long> projectIdList, Integer status) {
        int count = projectGroupMapper.selectSpecifiedProjectList(projectIdList, status);
        return count == projectIdList.size();
    }


    @Transactional(rollbackFor = GlobalException.class)
    public Result reportToHigherUnit(List<Long> projectGroupIdList, ProjectStatus
            rightProjectStatus, OperationUnit operationUnit) {
        List<OperationRecord> list = new LinkedList<>();
        if (!checkProjectStatus(projectGroupIdList, rightProjectStatus.getValue())) {
            throw new GlobalException(CodeMsg.PROJECT_CURRENT_STATUS_ERROR);
        }

        User user = getUserService.getCurrentUser();

        //存入操作历史
        for (Long projectId : projectGroupIdList
        ) {
            OperationRecord operationRecord = new OperationRecord();

            operationRecord.setRelatedId(projectId);
            operationRecord.setOperationReason(null);
            operationRecord.setOperationUnit(operationUnit.getValue());
            operationRecord.setOperationType(OperationType.REPORT.getValue());
            operationRecord.setOperationCollege(user.getInstitute());
            operationRecord.setOperationExecutorId(Long.valueOf(user.getCode()));

            list.add(operationRecord);
        }

        recordMapper.multiInsert(list);
        ProjectStatus newProjectStatus;
        if (rightProjectStatus == ProjectStatus.LAB_ALLOWED) {
            newProjectStatus = ProjectStatus.LAB_ALLOWED_AND_REPORTED;
            //如果是二级单位操作
        } else {
            newProjectStatus = ProjectStatus.SECONDARY_UNIT_ALLOWED_AND_REPORTED;
        }
        projectGroupMapper.updateProjectStatusOfList(projectGroupIdList, newProjectStatus.getValue());
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = GlobalException.class)
    public Result reportToCollegeLeader(List<ProjectCheckForm> formList) {
        //时间限制验证
        timeLimitService.validTime(TimeLimitType.LAB_REPORT_LIMIT);

        User user = getUserService.getCurrentUser();
        List<Long> projectGroupIdList = new LinkedList<>();
        for (ProjectCheckForm projectCheckForm : formList) {
            projectGroupIdList.add(projectCheckForm.getProjectId());
        }


        //验证项目状态
        if (!checkProjectStatus(projectGroupIdList, ProjectStatus.LAB_ALLOWED.getValue())) {
            throw new GlobalException(CodeMsg.PROJECT_CURRENT_STATUS_ERROR);
        }

        projectGroupMapper.updateProjectStatusOfList(projectGroupIdList, ProjectStatus.LAB_ALLOWED_AND_REPORTED.getValue());


        List<OperationRecord> list = new ArrayList<>();
        //添加历史记录
        for (ProjectCheckForm form : formList
        ) {
            //验证项目成员数量限制
            Integer amount = userProjectGroupMapper.selectStuCount(form.getProjectId(), JoinStatus.JOINED.getValue());
            if (amount < 3 || amount > 6) {
                throw new GlobalException(CodeMsg.PROJECT_FIT_PEOPLE_ERROR);
            }

            OperationRecord operationRecord = new OperationRecord();
            operationRecord.setRelatedId(form.getProjectId());
            operationRecord.setOperationReason(form.getReason());
            operationRecord.setOperationType(OperationType.REPORT.getValue());
            operationRecord.setOperationUnit(OperationUnit.LAB_ADMINISTRATOR.getValue());
            operationRecord.setOperationExecutorId(Long.valueOf(user.getCode()));
            operationRecord.setOperationCollege(user.getInstitute());

            list.add(operationRecord);

            projectGroupMapper.updateProjectStatus(form.getProjectId(), ProjectStatus.LAB_ALLOWED_AND_REPORTED.getValue());
        }
        //存储上报记录信息
        recordMapper.multiInsert(list);

        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result reportToFunctionalDepartment(List<Long> projectGroupIdList) {
        //数量限制
        User user = getUserService.getCurrentUser();
        Integer college = user.getInstitute();
        if (college == null) {
            throw new GlobalException(CodeMsg.COLLEGE_TYPE_NULL_ERROR);
        }

        //生成项目编号
        for (Long id : projectGroupIdList) {
            String serialNumber = projectGroupMapper.selectByPrimaryKey(id).getSerialNumber();
            //计算编号并在数据库中插入编号
            projectGroupMapper.updateProjectSerialNumber(id, SerialNumberUtil.getSerialNumberOfProject(college, ProjectType.GENERAL.getValue(), serialNumber));
        }


        AmountAndTypeVO amountAndTypeVO = amountLimitMapper.getAmountAndTypeVOByCollegeAndProjectType(college, ProjectType.GENERAL.getValue(), RoleType.SECONDARY_UNIT.getValue());
        Integer currentAmount = projectGroupMapper.getCountOfSpecifiedStatusAndProjectProject(ProjectStatus.SECONDARY_UNIT_ALLOWED_AND_REPORTED.getValue(), college);
        if (currentAmount + projectGroupIdList.size() > amountAndTypeVO.getMaxAmount()) {
            throw new GlobalException(CodeMsg.PROJECT_AMOUNT_LIMIT);
        }

        //时间限制
        timeLimitService.validTime(TimeLimitType.SECONDARY_UNIT_REPORT_LIMIT);
        return reportToHigherUnit(projectGroupIdList, ProjectStatus.SECONDARY_UNIT_ALLOWED, OperationUnit.SECONDARY_UNIT);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result ensureOrNotModify(ConfirmForm confirmForm) {
        Integer result = confirmForm.getResult();
        Long projectId = confirmForm.getProjectId();
        //确认修改
        if (recordMapper.selectDesignatedTypeListByRelatedIdAndType
                (OperationType.AGREE.getValue(), projectId).size() == 0) {
            throw new GlobalException(CodeMsg.PROJECT_NOT_MODIFY_BY_FUNCTION_DEPARTMENT);
        }
        //如果项目通过
        if (result.equals(OperationType.AGREE.getValue())) {
            updateProjectStatus(projectId, ProjectStatus.ESTABLISH.getValue());
        } else if (result.equals(OperationType.REJECT.getValue())) {
            updateProjectStatus(projectId, ProjectStatus.ESTABLISH_FAILED.getValue());
        }
        return Result.success();
    }

    @Override
    public Result getProjectDetailById(Long projectId) {
        List<ProjectHistoryInfo> list = recordMapper.selectAllByProjectId(projectId);
        return Result.success(list);
    }

    @Override
    public Result approveProjectApplyByLabAdministrator(List<ProjectCheckForm> list) {
        //时间限制验证
        timeLimitService.validTime(TimeLimitType.LAB_CHECK_LIMIT);
        return approveProjectApply(list, RoleType.LAB_ADMINISTRATOR.getValue());
    }


    @Override
    public Result approveProjectApplyBySecondaryUnit(List<ProjectCheckForm> list) {
        //时间限制验证
        timeLimitService.validTime(TimeLimitType.SECONDARY_UNIT_CHECK_LIMIT);

        User user = getUserService.getCurrentUser();
        Integer college = user.getInstitute();
        if (college == null) {
            throw new GlobalException(CodeMsg.PARAM_CANT_BE_NULL);
        }
        return approveProjectApply(list, RoleType.SECONDARY_UNIT.getValue());
    }

    @Override
    public Result midTermReviewPassed(List<ProjectCheckForm> list) {
        return setProjectStatusAndRecord(list, OperationType.MIDTERM_REVIEW_PASSED, OperationUnit.FUNCTIONAL_DEPARTMENT);
    }


    @Override
    public Result conditionallyQueryOfProject(QueryConditionForm form) {
        return conditionallyQueryOfCheckedProject(form);
    }

    @Override
    public Result getHistoricalProjectInfo(HistoryQueryProjectInfo info) {
        User user = getUserService.getCurrentUser();
        Integer college = user.getInstitute();
        //职能部门不需要学院信息
        if (info.getOperationUnit().equals(OperationUnit.FUNCTIONAL_DEPARTMENT.getValue())) {
            college = null;
        }

        List<ProjectGroup> list;

        //判断是否为已通过的  筛选出大于当前状态的
        if (info.getOperationType().equals(OperationType.AGREE.getValue())
                || info.getOperationType().equals(OperationType.REPORT.getValue())) {
            //排除立项失败的
            Integer status = 0;
            if (info.getOperationUnit().equals(OperationUnit.LAB_ADMINISTRATOR.getValue())) {
                if (info.getOperationType().equals(OperationType.AGREE.getValue())) {
                    status = ProjectStatus.LAB_ALLOWED.getValue();
                } else {
                    status = ProjectStatus.LAB_ALLOWED_AND_REPORTED.getValue();
                }
            } else if (info.getOperationUnit().equals(OperationUnit.SECONDARY_UNIT.getValue())) {
                if (info.getOperationType().equals(OperationType.AGREE.getValue())) {
                    status = ProjectStatus.SECONDARY_UNIT_ALLOWED.getValue();
                } else {
                    status = ProjectStatus.SECONDARY_UNIT_ALLOWED_AND_REPORTED.getValue();
                }
            } else if (info.getOperationUnit().equals(OperationUnit.FUNCTIONAL_DEPARTMENT.getValue())) {
                college = null;
                //职能部门获取已经通过的，只要是立项的即可，驳回的状态为-3，会被直接筛选掉
                status = ProjectStatus.ESTABLISH.getValue();
            }
            list = projectGroupMapper.selectGeneralPassedProjectList(college, status);
        } else {
            list = projectGroupMapper.selectGeneralRejectedProjectList(college);
        }

        for (ProjectGroup projectGroup : list
        ) {
            projectGroup.setNumberOfTheSelected(userProjectGroupMapper.selectStuCount(projectGroup.getId(), JoinStatus.JOINED.getValue()));
            projectGroup.setGuidanceTeachers(userProjectGroupMapper.selectUserMemberVOListByMemberRoleAndProjectId(MemberRole.GUIDANCE_TEACHER.getValue(), projectGroup.getId(), JoinStatus.JOINED.getValue()));
        }
        return Result.success(list);
    }

    @Override
    public Result getAllOpenTopicByCondition(QueryConditionForm form) {
        //先查询出符合条件的ID，在进行条件查询
        List<Long> projectIdList = projectGroupMapper.conditionQuery(form);
        if (projectIdList.isEmpty()) {
            return Result.success(null);
        }
        //查询已选学生数量
        List<OpenTopicInfo> list = projectGroupMapper.getAllOpenTopic(projectIdList);
        for (OpenTopicInfo info : list
        ) {
            info.setAmountOfSelected(userProjectGroupMapper.selectStuCount(info.getId(), JoinStatus.JOINED.getValue()));
        }
        return Result.success(list);
    }

    private Result conditionallyQueryOfCheckedProject(QueryConditionForm form) {
        List<Long> projectIdList = projectGroupMapper.conditionQuery(form);
        if (projectIdList.isEmpty()) {
            return Result.success(null);
        }
        List<ProjectGroup> list = projectGroupMapper.selectAllByList(projectIdList);
        for (ProjectGroup projectGroup : list
        ) {
            Long id = projectGroup.getId();
            projectGroup.setNumberOfTheSelected(userProjectGroupMapper.getMemberAmountOfProject(id, null));
            projectGroup.setGuidanceTeachers(userProjectGroupMapper.selectUserMemberVOListByMemberRoleAndProjectId(null, id, JoinStatus.JOINED.getValue()));
        }
        return Result.success(list);
    }

    @Override
    public Result getToBeReportedProjectByLabLeader() {
        User user = getUserService.getCurrentUser();
        if (user == null) {
            throw new GlobalException(CodeMsg.AUTHENTICATION_ERROR);
        }
        return getReportInfo(RoleType.LAB_ADMINISTRATOR.getValue());
    }

    @Override
    public Result getToBeReportedProjectBySecondaryUnit() {
        User user = getUserService.getCurrentUser();
        if (user == null) {
            throw new GlobalException(CodeMsg.AUTHENTICATION_ERROR);
        }
        return getReportInfo(RoleType.SECONDARY_UNIT.getValue());
    }


    @Transactional(rollbackFor = GlobalException.class)
    public Result approveProjectApply(List<ProjectCheckForm> formList, Integer role) {
        User user = getUserService.getCurrentUser();
        if (user == null) {
            throw new GlobalException(CodeMsg.AUTHENTICATION_ERROR);
        }
        Integer operationUnit;
        //当前状态
        Integer projectStatus = null;
        //将要被更新成的状态
        Integer updateProjectStatus = null;
        switch (role) {
            //如果是实验室主任
            case 4:
                operationUnit = OperationUnit.LAB_ADMINISTRATOR.getValue();
                projectStatus = ProjectStatus.DECLARE.getValue();
                updateProjectStatus = ProjectStatus.LAB_ALLOWED.getValue();
                break;
            //如果是二级单位
            case 5:
                operationUnit = OperationUnit.SECONDARY_UNIT.getValue();
                projectStatus = ProjectStatus.LAB_ALLOWED_AND_REPORTED.getValue();
                updateProjectStatus = ProjectStatus.SECONDARY_UNIT_ALLOWED.getValue();
                break;
            default:
                //超管执行操作
                operationUnit = -5;
        }
        List<OperationRecord> list = new LinkedList<>();
        for (ProjectCheckForm form : formList
        ) {
            OperationRecord operationRecord = new OperationRecord();

            operationRecord.setRelatedId(form.getProjectId());
            operationRecord.setOperationReason(form.getReason());
            operationRecord.setOperationType(OperationType.AGREE.getValue());
            operationRecord.setOperationUnit(operationUnit);
            operationRecord.setOperationCollege(user.getInstitute());
            operationRecord.setOperationExecutorId(Long.valueOf(user.getCode()));


            list.add(operationRecord);
            //当角色是实验室主任的时候,项目状态不是
            ProjectGroup projectGroup = selectByProjectGroupId(form.getProjectId());
            if (role == 4 && !projectGroup.getStatus().equals(projectStatus)) {
                throw new GlobalException("项目编号为" + projectGroup.getId() + "的项目非申报状态", CodeMsg.PROJECT_STATUS_IS_NOT_DECLARE.getCode());
            }
            //如果不是实验室上报状态,抛出异常
            if (role.equals(RoleType.SECONDARY_UNIT.getValue())) {
                if (!projectGroup.getStatus().equals(projectStatus)) {
                    throw new GlobalException("项目编号为" + projectGroup.getId() + "的项目非实验室审核通过", CodeMsg.PROJECT_CURRENT_STATUS_ERROR.getCode());
                }
            }
            //根据不同角色设置不同的项目状态
            updateProjectStatus(form.getProjectId(), updateProjectStatus);

        }
        recordMapper.multiInsert(list);
        return Result.success();
    }

    /**
     * 有报错
     *
     * @return
     */
    @Override
    public Result getAllOpenTopic() {
        //筛选  学生如果不在要求的时间内，项目不显示
        User currentUser = getUserService.getCurrentUser();
        Integer college = currentUser.getInstitute();
        TimeLimit timeLimit = timeLimitService.getTimeLimitByTypeAndCollege(TimeLimitType.JOIN_APPLY_LIMIT, college);
        //不在时间范围内
        if (timeLimit.getEndTime().before(new Date()) || timeLimit.getStartTime().after(new Date())) {
            //身份为学生，不可见
            if (userRoleService.validContainsUserRole(RoleType.NORMAL_STU)) {
                return Result.success();
            }
        }

        List<OpenTopicInfo> list = projectGroupMapper.getAllOpenTopic(null);
        for (OpenTopicInfo info : list
        ) {
            info.setAmountOfSelected(userProjectGroupMapper.selectStuCount(info.getId(), JoinStatus.JOINED.getValue()));
        }
        return Result.success(list);
    }

    @Override
    public List getJoinInfo() {
        User currentUser = getUserService.getCurrentUser();
        //检测用户是不是老师--后期可省略
        if (currentUser == null) {
            throw new GlobalException(CodeMsg.AUTHENTICATION_ERROR);
        }

        Role role = roleMapper.selectByUserId(Long.valueOf(currentUser.getCode()));
        if (role.getId() < (RoleType.MENTOR.getValue()).longValue()) {
            throw new GlobalException(CodeMsg.PERMISSION_DENNY);
        }
        List<JoinUnCheckVO> joinUnCheckVOS = new ArrayList<>();

        //获取当前教师参与申报的项目组
        List<ProjectGroup> projectGroups = selectByUserIdAndProjectStatus(Long.valueOf(currentUser.getCode()), null, JoinStatus.JOINED.getValue());
        for (int i = 0; i < projectGroups.size(); i++) {
            if (projectGroups.get(i) == null) {
                i++;
            }
            if (projectGroups.get(i).getIsOpenTopic().equals(OpenTopicType.NOT_OPEN_TOPIC.getValue())) {
                if (i != projectGroups.size() - 1) {
                    i++;
                }
            }
            ProjectGroup projectGroup = projectGroups.get(i);
            List<UserProjectGroup> userProjectGroups = userProjectService.selectByProjectAndStatus(projectGroup.getId(), null);
            for (UserProjectGroup userProjectGroup : userProjectGroups) {
                JoinUnCheckVO joinUnCheckVO = getJoinUnCheckVO(userProjectGroup, projectGroup);
                joinUnCheckVOS.add(joinUnCheckVO);
            }
        }
        return joinUnCheckVOS;
    }

    @Override
    public Result getApplyingJoinInfoByCondition(MemberQueryCondition condition) {
        User currentUser = getUserService.getCurrentUser();
        //检测用户
        if (currentUser == null) {
            throw new GlobalException(CodeMsg.AUTHENTICATION_ERROR);
        }
        if (condition == null) {
            throw new GlobalException(CodeMsg.PARAM_CANT_BE_NULL);
        }
        List<JoinUnCheckVO> joinUnCheckVOS = new ArrayList<>();
        if (condition.getId() == null) {
            List<ProjectGroup> projectGroups = selectByUserIdAndProjectStatus(Long.valueOf(currentUser.getCode()), ProjectStatus.LAB_ALLOWED.getValue(), JoinStatus.JOINED.getValue());
            for (int i = 0; i < projectGroups.size(); i++) {
                if (projectGroups.get(i) == null) {
                    break;
                }
                if (projectGroups.get(i).getIsOpenTopic().equals(OpenTopicType.NOT_OPEN_TOPIC.getValue())) {
                    i++;
                }
                ProjectGroup projectGroup = projectGroups.get(i);
                List<UserProjectGroup> userProjectGroups = userProjectService.selectByProjectAndStatus(projectGroup.getId(), condition.getStatus());
                for (UserProjectGroup userProjectGroup : userProjectGroups) {
                    JoinUnCheckVO joinUnCheckVO = getJoinUnCheckVO(userProjectGroup, projectGroup);
                    joinUnCheckVOS.add(joinUnCheckVO);
                }
            }
            //编号，状态同时存在
        } else {
            List<UserProjectGroup> userProjectGroups = userProjectService.selectByProjectAndStatus(condition.getId(), condition.getStatus());
            ProjectGroup projectGroup = projectGroupMapper.selectByPrimaryKey(condition.getId());
            for (UserProjectGroup userProjectGroup : userProjectGroups) {
                JoinUnCheckVO joinUnCheckVO = getJoinUnCheckVO(userProjectGroup, projectGroup);
                joinUnCheckVOS.add(joinUnCheckVO);
            }
        }
        return Result.success(joinUnCheckVOS);
    }

    /**
     * 添加学生到项目组
     *
     * @param joinForm
     * @return
     */
    @Override
    public Result addStudentToProject(JoinForm joinForm) {
        User user = getUserService.getCurrentUser();
        Long userId = Long.valueOf(user.getCode());

        //判断用户信息是否完整
        User addUser = userMapper.selectByUserCode(joinForm.getUserId().toString());
        if (addUser.getMobilePhone() == null) {
            throw new GlobalException(CodeMsg.ADD_USER_INFO_NOT_COMPLETE);
        }

        ProjectGroup projectGroup = selectByProjectGroupId(joinForm.getProjectGroupId());
        Integer status = projectGroup.getStatus();

        //验证项目状态
        if (!status.equals(ProjectStatus.LAB_ALLOWED.getValue()) && !status.equals(ProjectStatus.REJECT_MODIFY.getValue())
        ) {
            int SubordinateCollege = projectGroupMapper.selectSubordinateCollege(joinForm.getProjectGroupId());
            if (SubordinateCollege != 0) {
                throw new GlobalException(CodeMsg.CURRENT_PROJECT_STATUS_ERROR);
            }
        }

        UserProjectGroup userProjectGroupOfCurrentUser = userProjectGroupMapper.selectByProjectGroupIdAndUserId(joinForm.getProjectGroupId(), userId);
        if (userProjectGroupOfCurrentUser == null || !userProjectGroupOfCurrentUser.getMemberRole().equals(MemberRole.GUIDANCE_TEACHER.getValue())) {
            int SubordinateCollege = projectGroupMapper.selectSubordinateCollege(joinForm.getProjectGroupId());
            if (SubordinateCollege != 0) {
                throw new GlobalException(CodeMsg.USER_NOT_IN_GROUP);
            }
        }

        //判断用户是否存在
        if (userMapper.selectByUserCode(String.valueOf(joinForm.getUserId())) == null) {
            throw new GlobalException(CodeMsg.USER_NO_EXIST);
        }

        //判断用户时候已经加入
        if (userProjectGroupMapper.selectByProjectGroupIdAndUserId(joinForm.getProjectGroupId(), joinForm.getUserId()) != null) {
            throw new GlobalException(CodeMsg.USER_HAD_JOINED);
        }

        //不得超过最大数量限制
        Integer amount = userProjectGroupMapper.selectStuCount(joinForm.getProjectGroupId(), JoinStatus.JOINED.getValue());

        //数量限制判断
        if (projectGroup.getFitPeopleNum() <= amount) {
            throw new GlobalException(CodeMsg.PROJECT_USER_MAX_ERROR);
        }

        UserProjectGroup userProjectGroup = new UserProjectGroup();
        userProjectGroup.setUserId(joinForm.getUserId());
        userProjectGroup.setProjectGroupId(joinForm.getProjectGroupId());
        userProjectGroup.setMemberRole(MemberRole.NORMAL_MEMBER.getValue());
        userProjectGroup.setStatus(JoinStatus.JOINED.getValue());
        userProjectGroup.setJoinTime(new Date());
        userProjectGroupMapper.insert(userProjectGroup);
        return Result.success();
    }

    @Override
    public Result removeStudentFromProject(JoinForm joinForm) {
        //验证项目状态
        Integer status = projectGroupMapper.selectByPrimaryKey(joinForm.getProjectGroupId()).getStatus();
        if (!status.equals(ProjectStatus.LAB_ALLOWED.getValue()) && !status.equals(ProjectStatus.REJECT_MODIFY.getValue())

        ) {
            throw new GlobalException(CodeMsg.CURRENT_PROJECT_STATUS_ERROR);
        }

        //验证人数限制
        Integer amount = userProjectGroupMapper.selectStuCount(joinForm.getProjectGroupId(), JoinStatus.JOINED.getValue());
        ProjectGroup projectGroup = projectGroupMapper.selectByPrimaryKey(joinForm.getProjectGroupId());
        if (amount <= projectGroup.getFitPeopleNum()) {
            throw new GlobalException(CodeMsg.FIT_PEOPLE_LIMIT_ERROR);
        }


        User user = getUserService.getCurrentUser();
        Long userId = Long.valueOf(user.getCode());
        UserProjectGroup userProjectGroupOfCurrentUser = userProjectGroupMapper.selectByProjectGroupIdAndUserId(joinForm.getProjectGroupId(), userId);
        if (userProjectGroupOfCurrentUser == null || !userProjectGroupOfCurrentUser.getMemberRole().equals(MemberRole.PROJECT_GROUP_LEADER.getValue())) {
            throw new GlobalException(CodeMsg.USER_NOT_IN_GROUP);
        }
        UserProjectGroup userProjectGroupOfJoinUser = userProjectGroupMapper.selectByProjectGroupIdAndUserId(joinForm.getProjectGroupId(), joinForm.getUserId());
        if (userProjectGroupOfJoinUser == null) {
            throw new GlobalException(CodeMsg.USER_HAD_JOINED_CANT_REJECT);
        }
        userProjectGroupMapper.deleteByPrimaryKey(userProjectGroupOfCurrentUser.getId());
        return Result.success();
    }

    private JoinUnCheckVO getJoinUnCheckVO(UserProjectGroup userProjectGroup, ProjectGroup projectGroup) {
        User user = userService.selectByUserId(userProjectGroup.getUserId());
        JoinUnCheckVO joinUnCheckVO = new JoinUnCheckVO();
        joinUnCheckVO.setId(projectGroup.getId());
        joinUnCheckVO.setMemberRole(userProjectGroup.getMemberRole());
        joinUnCheckVO.setSerialNumber(projectGroup.getSerialNumber());
        joinUnCheckVO.setProjectName(projectGroup.getProjectName());
        joinUnCheckVO.setProjectType(projectGroup.getProjectType());
        joinUnCheckVO.setPersonJudge(userProjectGroup.getPersonalJudge());
        joinUnCheckVO.setTechnicalRole(userProjectGroup.getTechnicalRole());
        joinUnCheckVO.setApplyTime(userProjectGroup.getJoinTime());
        joinUnCheckVO.setExperimentType(projectGroup.getExperimentType());
        joinUnCheckVO.setUserDetailVO(convertUtil.convertUserDetailVO(user));
        joinUnCheckVO.setStatus(userProjectGroup.getStatus());

        return joinUnCheckVO;
    }

    @Override
    public Result rejectJoin(JoinForm[] joinForm) {
        for (JoinForm form : joinForm) {
            UserProjectGroup userProjectGroup = userProjectService.selectByProjectGroupIdAndUserId(form.getProjectGroupId(), form.getUserId());
            if (userProjectGroup == null) {
                return Result.error(CodeMsg.USER_NOT_APPLYING);
            }
            if (userProjectGroup.getStatus() != JoinStatus.APPLYING.getValue().intValue()) {
                return Result.error(CodeMsg.USER_HAD_JOINED_CANT_REJECT);
            }
            userProjectGroup.setStatus(JoinStatus.UN_PASS.getValue());
            if (!userProjectService.update(userProjectGroup)) {
                return Result.error(CodeMsg.UPDATE_ERROR);
            }
        }
        return Result.success();
    }

    @Override
    public List<SelectProjectVO> selectByProjectName(String name) {
        List<SelectProjectVO> list = (List<SelectProjectVO>) redisService.getList(ProjectGroupKey.getByFuzzyName, name);
        if (list == null || list.size() == 0) {
            list = projectGroupMapper.selectByFuzzyName(name);
            if (list != null) {
                redisService.setList(ProjectGroupKey.getByFuzzyName, name, list);
            }
        }
        return list;
    }

    @Override
    @Transactional(rollbackFor = GlobalException.class)
    public Result changeKeyProjectToGeneral(List<ProjectCheckForm> formList) {

        User user = getUserService.getCurrentUser();

        List<OperationRecord> list = new ArrayList<>();

        for (ProjectCheckForm form : formList
        ) {
            Integer status = keyProjectStatusMapper.getStatusByProjectId(form.getProjectId());
            //验证当前状态
            if (!ProjectStatus.GUIDE_TEACHER_ALLOWED.getValue().equals(status)) {
                throw new GlobalException(CodeMsg.CURRENT_PROJECT_STATUS_ERROR);
            }
            //批量插入数据
            OperationRecord operationRecord = new OperationRecord();
            operationRecord.setRelatedId(form.getProjectId());
            operationRecord.setOperationReason(form.getReason());
            operationRecord.setOperationUnit(OperationUnit.LAB_ADMINISTRATOR.getValue());
            operationRecord.setOperationType(OperationType.REPORT.getValue());
            operationRecord.setOperationCollege(user.getInstitute());
            operationRecord.setOperationExecutorId(Long.valueOf(user.getCode()));

            //删除重点项目状态
            int result = keyProjectStatusMapper.deleteByProjectId(form.getProjectId());
            if (result != 1) {
                throw new GlobalException(CodeMsg.CURRENT_MODIFY_PROJECT_TYPE_ERROR);
            }

            //修改状态
            updateProjectStatus(form.getProjectId(), ProjectStatus.SECONDARY_UNIT_ALLOWED.getValue());

            projectGroupMapper.updateProjectType(form.getProjectId(), ProjectType.GENERAL.getValue());

            list.add(operationRecord);
        }
        recordMapper.multiInsert(list);
        return Result.success();
    }

    /**
     * 中期打回
     *
     * @param list
     * @return
     */
    @Override
    public Result midTermKeyProjectHitBack(List<ProjectCheckForm> list) {
        return ProjectHitBack(list, OperationUnit.FUNCTIONAL_DEPARTMENT, OperationType.INTERIM_RETURN);
    }

    @Transactional(rollbackFor = GlobalException.class)
    public Result ProjectHitBack(List<ProjectCheckForm> formList, OperationUnit operationUnit, OperationType operationType) {
        User user = getUserService.getCurrentUser();
        List<OperationRecord> list = new LinkedList<>();
        for (ProjectCheckForm form : formList) {
            ProjectGroup projectGroup = projectGroupMapper.selectByPrimaryKey(form.getProjectId());
            Integer status = projectGroup.getStatus();
            if (!status.equals(ProjectStatus.ESTABLISH.getValue())) {
                throw new GlobalException(CodeMsg.CURRENT_PROJECT_STATUS_ERROR);
            }
            //批量插入数据
            OperationRecord operationRecord = new OperationRecord();

            operationRecord.setRelatedId(form.getProjectId());
            operationRecord.setOperationReason("中期退回修改");
            operationRecord.setOperationUnit(operationUnit.getValue());
            operationRecord.setOperationType(operationType.getValue());
            operationRecord.setOperationCollege(user.getInstitute());
            operationRecord.setOperationExecutorId(Long.valueOf(user.getCode()));
            updateProjectStatus(form.getProjectId(), ProjectStatus.INTERIM_RETURN_MODIFICATION.getValue());
            list.add(operationRecord);

            //发送消息
            HitBackMessage hitBackMessage = new HitBackMessage();
            hitBackMessage.setReceiveUserId(userProjectGroupMapper.getProjectLeader(form.getProjectId(), MemberRole.PROJECT_GROUP_LEADER.getValue()).getUserId());
            hitBackMessage.setContent("项目名:" + projectGroup.getProjectName() + "  意见:" + form.getReason());
            hitBackMessage.setSender(user.getRealName());
            Date date = new Date();
            hitBackMessage.setSendTime(date);
            hitBackMessage.setIsRead(false);
            log.info(hitBackMessage.toString());
            hitBackMessageMapper.insert(hitBackMessage);

        }
        recordMapper.multiInsert(list);

        return Result.success();
    }

    @Override
    public Result rejectProjectApplyByLabAdministrator(List<ProjectCheckForm> formList) {
        return rejectProjectApply(formList, OperationUnit.LAB_ADMINISTRATOR, OperationType.REJECT);
    }

    @Override
    public Result rejectProjectReportByLabAdministrator(List<ProjectCheckForm> formList) {
        return rejectProjectApply(formList, OperationUnit.LAB_ADMINISTRATOR, OperationType.REPORT_REJECT);
    }

    @Override
    public Result rejectProjectApplyBySecondaryUnit(List<ProjectCheckForm> formList) {
        return rejectProjectApply(formList, OperationUnit.SECONDARY_UNIT, OperationType.REJECT);
    }

    @Override
    public Result rejectProjectApplyByFunctionalDepartment(List<ProjectCheckForm> formList) {
        return rejectProjectApply(formList, OperationUnit.FUNCTIONAL_DEPARTMENT, OperationType.REJECT);
    }

    @Override
    public Result rejectIntermediateInspectionProject(List<ProjectCheckForm> list) {
        return rejectProjectApply(list, OperationUnit.FUNCTIONAL_DEPARTMENT, OperationType.OFFLINE_CHECK_REJECT);
    }


    @Override
    public Result rejectToBeConcludingProject(List<ProjectCheckForm> list) {
        return rejectProjectApply(list, OperationUnit.FUNCTIONAL_DEPARTMENT, OperationType.CONCLUSION_REJECT);
    }



    /**
     * 因为是批量操作  所以就最好将拒绝和同意分开
     *
     * @param formList 项目拒绝信息集合
     * @return
     */
    @Transactional(rollbackFor = GlobalException.class)
    Result rejectProjectApply(List<ProjectCheckForm> formList, OperationUnit operationUnit, OperationType operationType) {
        User user = getUserService.getCurrentUser();
        List<OperationRecord> list = new LinkedList<>();
        Integer rightProjectStatus;
        switch (operationUnit.getValue()) {
            //不验证实验室的状态
            case 5:
                rightProjectStatus = ProjectStatus.LAB_ALLOWED_AND_REPORTED.getValue();
                break;
            case 6:
                rightProjectStatus = ProjectStatus.SECONDARY_UNIT_ALLOWED_AND_REPORTED.getValue();
                break;
            default:
                rightProjectStatus = ProjectStatus.ESTABLISH_FAILED.getValue();
                break;
        }

        for (ProjectCheckForm form : formList
        ) {
            Integer status = projectGroupMapper.selectByPrimaryKey(form.getProjectId()).getStatus();
            //验证当前状态
            /**
             * 有改动 可能存在bug
             */
            if (!rightProjectStatus.equals(status) && operationUnit != OperationUnit.LAB_ADMINISTRATOR && operationUnit != OperationUnit.FUNCTIONAL_DEPARTMENT) {
                throw new GlobalException(CodeMsg.CURRENT_PROJECT_STATUS_ERROR);
            }
            //批量插入数据
            OperationRecord operationRecord = new OperationRecord();

            operationRecord.setRelatedId(form.getProjectId());
            operationRecord.setOperationReason(form.getReason());
            operationRecord.setOperationUnit(operationUnit.getValue());
            operationRecord.setOperationType(operationType.getValue());
            operationRecord.setOperationCollege(user.getInstitute());
            operationRecord.setOperationExecutorId(Long.valueOf(user.getCode()));
            //修改状态
            if (operationUnit == OperationUnit.LAB_ADMINISTRATOR ||
                    operationUnit == OperationUnit.MENTOR) {
                updateProjectStatus(form.getProjectId(), ProjectStatus.REJECT_MODIFY.getValue());
            } else {
                updateProjectStatus(form.getProjectId(), ProjectStatus.ESTABLISH_FAILED.getValue());
            }
            list.add(operationRecord);
        }
        recordMapper.multiInsert(list);
        return Result.success();
    }

    private void setOperationExecutor(OperationRecord operationRecord) {
        User user = getUserService.getCurrentUser();
        Long id = Long.valueOf(user.getCode());
        operationRecord.setOperationExecutorId(id);
    }


    @Value(value = "${file.ip-address}")
    private String ipAddress;

    /**
     * 获取项目详情
     *
     * @param projectId
     * @return
     */
    @Override
    public Result getProjectGroupDetailVOByProjectId(Long projectId) {
        if (projectId == null) {
            throw new GlobalException(CodeMsg.PARAM_CANT_BE_NULL);
        }
        ProjectGroupDetailVO detail = projectGroupMapper.getProjectGroupDetailVOByProjectId(projectId);
        //设置状态
        if (detail.getKeyProjectStatus() != null) {
            detail.setStatus(detail.getKeyProjectStatus());
        }
        ProjectFile file = projectFileMapper.selectByProjectGroupIdAndMaterialType(projectId, MaterialType.APPLY_MATERIAL.getValue(),null);
        if (file == null) {
            detail.setApplyurl(null);
        } else {
            String fileName = file.getFileName();
            String url = ipAddress + "/apply/" + fileName;
            detail.setApplyurl(url);
        }
        return Result.success(detail);
    }

    @Override
    public Result deleteMemberFromProject(Long projectId, Long userId) {
        User currentUser = getUserService.getCurrentUser();
        Long currentUserId = Long.valueOf(currentUser.getCode());
        if (userProjectGroupMapper.selectByProjectGroupIdAndUserId(projectId, currentUserId) == null
                || !userProjectGroupMapper.selectByProjectGroupIdAndUserId(projectId, currentUserId).getMemberRole()
                .equals(MemberRole.GUIDANCE_TEACHER.getValue())) {
            int SubordinateCollege = projectGroupMapper.selectSubordinateCollege(projectId);
            if (SubordinateCollege != 0) {
                throw new GlobalException(CodeMsg.PERMISSION_DENNY);
            }
        }
        UserProjectGroup userProjectGroup = userProjectGroupMapper.selectByProjectGroupIdAndUserId(projectId, userId);
        if (userProjectGroup == null) {
            throw new GlobalException(CodeMsg.USER_GROUP_NOT_EXIST);
        }
        userProjectGroupMapper.deleteByPrimaryKey(userProjectGroup.getId());
        return Result.success();
    }
}
