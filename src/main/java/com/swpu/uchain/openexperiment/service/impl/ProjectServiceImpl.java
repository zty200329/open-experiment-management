package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.DTO.ConclusionDTO;
import com.swpu.uchain.openexperiment.DTO.OperationRecord;
import com.swpu.uchain.openexperiment.DTO.ProjectHistoryInfo;
import com.swpu.uchain.openexperiment.VO.project.*;
import com.swpu.uchain.openexperiment.VO.user.UserMemberVO;
import com.swpu.uchain.openexperiment.config.UploadConfig;
import com.swpu.uchain.openexperiment.mapper.*;
import com.swpu.uchain.openexperiment.domain.*;
import com.swpu.uchain.openexperiment.enums.*;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import com.swpu.uchain.openexperiment.form.member.MemberQueryCondition;
import com.swpu.uchain.openexperiment.form.project.*;
import com.swpu.uchain.openexperiment.form.query.QueryConditionForm;
import com.swpu.uchain.openexperiment.form.query.HistoryQueryProjectInfo;
import com.swpu.uchain.openexperiment.redis.RedisService;
import com.swpu.uchain.openexperiment.redis.key.ProjectGroupKey;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.*;
import com.swpu.uchain.openexperiment.util.ConvertUtil;
import com.swpu.uchain.openexperiment.util.SerialNumberUtil;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.*;


/**
 * @author panghu
 */
@Service
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
    private OperationRecordMapper recordMapper;
    private MessageRecordMapper messageRecordMapper;
    private UserMapper userMapper;
    private KeyProjectStatusMapper keyProjectStatusMapper;

    @Autowired
    public ProjectServiceImpl(UserService userService, ProjectGroupMapper projectGroupMapper,
                              RedisService redisService, UserProjectService userProjectService,
                              ProjectFileService projectFileService, FundsService fundsService,
                              UploadConfig uploadConfig,
                              ConvertUtil convertUtil, GetUserService getUserService,
                              OperationRecordMapper recordMapper,
                              MessageRecordMapper messageRecordMapper, RoleMapper roleMapper,
                              UserProjectGroupMapper userProjectGroupMapper, UserMapper userMapper,
                              KeyProjectStatusMapper keyProjectStatusMapper) {
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
        this.messageRecordMapper = messageRecordMapper;
        this.roleMapper = roleMapper;
        this.userMapper = userMapper;
        this.keyProjectStatusMapper = keyProjectStatusMapper;
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
    public List<ProjectGroup> selectByUserIdAndProjectStatus(Long userId, Integer projectStatus) {
        //获取当前用户参与的所有项目
        return projectGroupMapper.selectByUserIdAndStatus(userId, projectStatus);
    }

    /**
     * 指导教师填写申请立项书
     *
     * @param form 申请立项表单
     * @return 申请立项操作结果
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result applyCreateProject(CreateProjectApplyForm form) {
        System.err.println(form.toString());
        User currentUser = getUserService.getCurrentUser();

        Integer college = currentUser.getInstitute();
        if (college == null){
            throw new GlobalException(CodeMsg.COLLEGE_TYPE_NULL_ERROR);
        }
        //获取这是该院第几个项目
        String maxSerialNumber = projectGroupMapper.getIndexByCollege(college);

        String serialNumber = SerialNumberUtil.getSerialNumberOfProject(college,form.getProjectType(),maxSerialNumber);

        //判断用户类型
        if (currentUser.getUserType().intValue() == UserType.STUDENT.getValue()) {
            Result.error(CodeMsg.STUDENT_CANT_APPLY);
        }
        ProjectGroup projectGroup = projectGroupMapper.selectByName(form.getProjectName());
        if (projectGroup != null) {
            return Result.error(CodeMsg.PROJECT_GROUP_HAD_EXIST);
        }

        //开放选题时,不进行学生选择
        if (form.getIsOpenTopic().equals(OpenTopicType.OPEN_TOPIC_ALL.getValue()) && form.getStuCodes() != null) {
            throw new GlobalException(CodeMsg.TOPIC_IS_NOT_OPEN);
        }

        //时间设置出错
        if (form.getStartTime().getTime() >= form.getEndTime().getTime()) {
            throw new GlobalException(CodeMsg.TIME_DEFINE_ERROR);
        }

        projectGroup = new ProjectGroup();
        BeanUtils.copyProperties(form, projectGroup);
        projectGroup.setStatus(ProjectStatus.DECLARE.getValue());
        //设置申请人
        projectGroup.setCreatorId(Long.valueOf(currentUser.getCode()));
        projectGroup.setSubordinateCollege(college);
        projectGroup.setSerialNumber(serialNumber);
        //插入数据
        Result result = addProjectGroup(projectGroup);
        if (result.getCode() != 0) {
            throw new GlobalException(CodeMsg.ADD_PROJECT_GROUP_ERROR);
        }

        String[] teacherCodes = form.getTeacherCodes();
        String[] stuCodes = form.getStuCodes();
        System.err.println(Arrays.toString(stuCodes));
        boolean isTeacherExist = false;
        for (String teacherCode : teacherCodes) {
            if (teacherCode.equals(currentUser.getCode())) {
                isTeacherExist = true;
            }
        }
        if (!isTeacherExist) {
            throw new GlobalException(CodeMsg.LEADING_TEACHER_CONTAINS_ERROR);
        }
        userProjectService.addStuAndTeacherJoin(stuCodes, teacherCodes, projectGroup.getId());
        //记录申请信息
        OperationRecord operationRecord = new OperationRecord();
        operationRecord.setRelatedId(projectGroup.getId());
        operationRecord.setOperationType(OperationType.REPORT.getValue());
        operationRecord.setOperationUnit(OperationUnit.MENTOR.getValue());
        operationRecord.setOperationExecutorId(Long.valueOf(currentUser.getCode()));
        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result applyUpdateProject(UpdateProjectApplyForm updateProjectApplyForm) {

        //TODO 时间限制

        ProjectGroup projectGroup = selectByProjectGroupId(updateProjectApplyForm.getProjectGroupId());
        if (projectGroup == null) {
            return Result.error(CodeMsg.PROJECT_GROUP_NOT_EXIST);
        }
        User currentUser = getUserService.getCurrentUser();
        UserProjectGroup userProjectGroup = userProjectService.selectByProjectGroupIdAndUserId(updateProjectApplyForm.getProjectGroupId(), Long.valueOf(currentUser.getCode()));
        if (userProjectGroup == null) {
            return Result.error(CodeMsg.USER_NOT_IN_GROUP);
        }
        //状态不允许修改
        if (projectGroup.getStatus() != ProjectStatus.DECLARE.getValue().intValue()) {
            return Result.error(CodeMsg.PROJECT_GROUP_INFO_CANT_CHANGE);
        }
        //更新项目组基本信息
        BeanUtils.copyProperties(updateProjectApplyForm, projectGroup);
        update(projectGroup);
        userProjectService.deleteByProjectGroupId(projectGroup.getId());
        String[] stuCodes = new String[updateProjectApplyForm.getStuCodes().length];
        String[] teacherCodes = new String[updateProjectApplyForm.getStuCodes().length];
        for (int i = 0; i < updateProjectApplyForm.getStuCodes().length; i++) {
            stuCodes[i] = updateProjectApplyForm.getStuCodes()[i].toString();
        }
        for (int i = 0; i < updateProjectApplyForm.getTeacherCodes().length; i++) {
            teacherCodes[i] = updateProjectApplyForm.getTeacherCodes()[i].toString();
        }

        userProjectService.addStuAndTeacherJoin(stuCodes, teacherCodes, projectGroup.getId());
        //修改项目状态,重新开始申报
        updateProjectStatus(projectGroup.getId(), ProjectStatus.ESTABLISH.getValue());

        //将之前的历史数据设置为不可见
        OperationRecord operationRecord = new OperationRecord();
        operationRecord.setOperationType(OperationType.MODIFY.getValue());
        operationRecord.setOperationUnit(OperationUnit.MENTOR.getValue());
        //设置执行人
        setOperationExecutor(operationRecord);
        recordMapper.insert(operationRecord);
        return Result.success();
    }

    @Override
    public Result getCurrentUserProjects(Integer projectStatus) {
        User currentUser = getUserService.getCurrentUser();
        if (currentUser == null) {
            throw new GlobalException(CodeMsg.AUTHENTICATION_ERROR);
        }
        List<ProjectGroup> projectGroups = selectByUserIdAndProjectStatus(Long.valueOf(currentUser.getCode()), projectStatus);
        //设置当前用户的所有项目VO
        List<MyProjectVO> projectVOS = new ArrayList<>();
        for (ProjectGroup projectGroup : projectGroups) {
            Integer numberOfSelectedStu = userProjectGroupMapper.selectStuCount(projectGroup.getId(), null);
            MyProjectVO myProjectVO = new MyProjectVO();
            Integer status = keyProjectStatusMapper.getStatusByProjectId(projectGroup.getId());
            if (status != null){
                myProjectVO.setStatus(status);
            }
            BeanUtils.copyProperties(projectGroup, myProjectVO);
            myProjectVO.setId(projectGroup.getId());
            myProjectVO.setNumberOfTheSelected(numberOfSelectedStu);
            myProjectVO.setProjectDetails(getProjectDetails(projectGroup));
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
                userProjectGroup.getMemberRole()));
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
                        userProject.getMemberRole()));
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
    public Result agreeEstablish(List<ProjectCheckForm> projectGroupIdList) {
        List<OperationRecord> operationRecordS = new LinkedList<>();
        for (ProjectCheckForm projectCheckForm : projectGroupIdList) {
            Result result = updateProjectStatus(projectCheckForm.getProjectId(), ProjectStatus.ESTABLISH.getValue());
            if (result.getCode() != 0) {
                throw new GlobalException(CodeMsg.UPDATE_ERROR);
            }

            OperationRecord operationRecord = new OperationRecord();
            operationRecord.setOperationType(OperationType.AGREE.getValue());
            operationRecord.setOperationUnit(OperationUnit.FUNCTIONAL_DEPARTMENT.getValue());
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
        //TODO 身份验证
        return getCheckInfo(ProjectStatus.DECLARE);
    }

    @Override
    public Result getPendingApprovalProjectBySecondaryUnit() {
        //TODO 身份验证

        return getCheckInfo(ProjectStatus.LAB_ALLOWED_AND_REPORTED);
    }

    @Override
    public Result getPendingApprovalProjectByFunctionalDepartment() {
        //TODO 身份验证
        return getCheckInfo(ProjectStatus.SECONDARY_UNIT_ALLOWED_AND_REPORTED);
    }

    @Override
    public Result getToBeConcludingProject() {
        //TODO 身份验证
        return getCheckInfo(ProjectStatus.ESTABLISH);
    }

    @Override
    public Result getIntermediateInspectionProject() {
        //TODO 身份验证
        return getCheckInfo(ProjectStatus.MID_TERM_INSPECTION);
    }

    private Result getReportInfo(Integer role) {
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
        List<CheckProjectVO> checkProjectVOs = projectGroupMapper.selectApplyOrderByTime(projectStatus);
        for (CheckProjectVO checkProjectVO : checkProjectVOs) {
            List<UserProjectGroup> userProjectGroups = userProjectService.selectByProjectGroupId(checkProjectVO.getId());
            List<UserMemberVO> guidanceTeachers = new ArrayList<>();
            for (UserProjectGroup userProjectGroup:userProjectGroups
                 ) {
                UserMemberVO userMemberVO = new UserMemberVO();
                userMemberVO.setMemberRole(userProjectGroup.getMemberRole());
                userMemberVO.setUserId(userProjectGroup.getUserId());
                userMemberVO.setUserName(userMapper.selectByUserCode(String.valueOf(userProjectGroup.getUserId())).getRealName());
                guidanceTeachers.add(userMemberVO);
            }
            checkProjectVO.setGuidanceTeachers(guidanceTeachers);
            checkProjectVO.setNumberOfTheSelected(userProjectGroupMapper.getMemberAmountOfProject(checkProjectVO.getId(),null));
        }
        return Result.success(checkProjectVOs);
    }

        private Result getCheckInfo (ProjectStatus projectStatus){
        Integer status = projectStatus.getValue();
            List<CheckProjectVO> checkProjectVOs = projectGroupMapper.selectApplyOrderByTime(status);
            for (CheckProjectVO checkProjectVO : checkProjectVOs) {
                List<UserProjectGroup> userProjectGroups = userProjectService.selectByProjectGroupId(checkProjectVO.getId());
                checkProjectVO.setNumberOfTheSelected(userProjectGroupMapper.getMemberAmountOfProject(checkProjectVO.getId(),null));
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

        private boolean checkProjectStatus (List < Long > projectIdList, Integer status){
            int count = projectGroupMapper.selectSpecifiedProjectList(projectIdList, status);
            return count == projectIdList.size();
        }


        @Transactional(rollbackFor = Exception.class)
        public Result reportToHigherUnit (List < Long > projectGroupIdList, ProjectStatus
            rightProjectStatus, OperationUnit operationUnit){
            List<OperationRecord> list = new LinkedList<>();
            if (!checkProjectStatus(projectGroupIdList, rightProjectStatus.getValue())) {
                throw new GlobalException(CodeMsg.PROJECT_CURRENT_STATUS_ERROR);
            }
            for (Long projectId : projectGroupIdList
            ) {
                OperationRecord operationRecord = new OperationRecord();
                operationRecord.setRelatedId(projectId);
                operationRecord.setOperationUnit(operationUnit.getValue());
                operationRecord.setOperationType(OperationType.REPORT.getValue());
                setOperationExecutor(operationRecord);

                list.add(operationRecord);
            }

            recordMapper.multiInsert(list);
            ProjectStatus newProjectStatus;
            if (rightProjectStatus == ProjectStatus.LAB_ALLOWED){
                newProjectStatus = ProjectStatus.LAB_ALLOWED_AND_REPORTED;
            //如果是二级单位操作
            }else {
                newProjectStatus = ProjectStatus.SECONDARY_UNIT_ALLOWED_AND_REPORTED;
            }
            projectGroupMapper.updateProjectStatusOfList(projectGroupIdList, newProjectStatus.getValue());
            return Result.success();
        }

        @Override
        @Transactional(rollbackFor = GlobalException.class)
        public Result reportToCollegeLeader (List < Long > projectGroupIdList) {
            return reportToHigherUnit(projectGroupIdList, ProjectStatus.LAB_ALLOWED, OperationUnit.LAB_ADMINISTRATOR);
        }

        @Override
        @Transactional(rollbackFor = Exception.class)
        public Result reportToFunctionalDepartment (List < Long > projectGroupIdList) {
            return reportToHigherUnit(projectGroupIdList, ProjectStatus.SECONDARY_UNIT_ALLOWED, OperationUnit.SECONDARY_UNIT);
        }


        @Override
        @Transactional(rollbackFor = Exception.class)
        public Result ensureOrNotModify (ConfirmForm confirmForm){
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
    public Result getProjectDetailById (Long projectId){
        List<ProjectHistoryInfo> list = recordMapper.selectAllByProjectId(projectId);
        return Result.success(list);
    }

    @Override
    public Result approveProjectApplyByLabAdministrator (List < ProjectCheckForm > list) {
        return approveProjectApply(list, RoleType.LAB_ADMINISTRATOR.getValue());
    }

    @Override
    public Result approveProjectApplyBySecondaryUnit (List < ProjectCheckForm > list) {
        return approveProjectApply(list, RoleType.SECONDARY_UNIT.getValue());
    }

    @Override
    public Result conditionallyQueryOfProject(QueryConditionForm form){
        return conditionallyQueryOfCheckedProject(form);
    }

    @Override
    public Result getHistoricalProjectInfo(HistoryQueryProjectInfo info) {

        // TODO 权限验证

        List<ProjectGroup> list = projectGroupMapper.selectHistoricalInfoByUnitAndOperation(info.getOperationUnit(),info.getOperationType());
        for (ProjectGroup projectGroup:list
             ) {
            projectGroup.setNumberOfTheSelected(userProjectGroupMapper.getMemberAmountOfProject(projectGroup.getId(),null));
            projectGroup.setGuidanceTeachers(userProjectGroupMapper.selectUserMemberVOListByMemberRoleAndProjectId(MemberRole.GUIDANCE_TEACHER.getValue(),projectGroup.getId()));
        }
        return Result.success(list);
    }

    private Result conditionallyQueryOfCheckedProject(QueryConditionForm form) {
        List<Long>  projectIdList = projectGroupMapper.conditionQuery(form);
        if (projectIdList.isEmpty()){
            return Result.success(null);
        }
        List<ProjectGroup> list = projectGroupMapper.selectAllByList(projectIdList);
        for (ProjectGroup projectGroup:list
             ) {
            Long id = projectGroup.getId();
            projectGroup.setNumberOfTheSelected(userProjectGroupMapper.getMemberAmountOfProject(id,null));
            projectGroup.setGuidanceTeachers(userProjectGroupMapper.selectUserMemberVOListByMemberRoleAndProjectId(null,id));
        }
        return Result.success(list);
    }

    @Override
        public Result getToBeReportedProjectByLabLeader () {
            User user = getUserService.getCurrentUser();
            if (user == null) {
                throw new GlobalException(CodeMsg.AUTHENTICATION_ERROR);
            }
            return getReportInfo(RoleType.LAB_ADMINISTRATOR.getValue());
        }

        @Override
        public Result getToBeReportedProjectBySecondaryUnit () {
            User user = getUserService.getCurrentUser();
            if (user == null) {
                throw new GlobalException(CodeMsg.AUTHENTICATION_ERROR);
            }
            return getReportInfo(RoleType.SECONDARY_UNIT.getValue());
        }


        @Transactional(rollbackFor = GlobalException.class)
        public Result approveProjectApply (List < ProjectCheckForm > formList, Integer role){
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
            OperationRecord operationRecord = new OperationRecord();
            for (ProjectCheckForm form : formList
            ) {
                operationRecord.setRelatedId(form.getProjectId());
                operationRecord.setOperationReason(form.getReason());
                operationRecord.setOperationType(OperationType.AGREE.getValue());
                operationRecord.setOperationUnit(operationUnit);
                operationRecord.setOperationExecutorId(Long.valueOf(user.getCode()));
                //当角色是实验室主任的时候,项目状态不是
                ProjectGroup projectGroup = selectByProjectGroupId(form.getProjectId());
//            if (role == 4 && !projectGroup.getStatus().equals(projectStatus)){
//                throw new GlobalException("项目编号为"+projectGroup.getId()+"的项目非申报状态",CodeMsg.PROJECT_STATUS_IS_NOT_DECLARE.getCode());
//            }
                //如果不是实验室上报状态,抛出异常
//            if (role == 5 && !projectGroup.getStatus().equals(projectStatus)){
//                throw new GlobalException("项目编号为"+projectGroup.getId()+"的项目非实验室上报状态",CodeMsg.PROJECT_STATUS_IS_NOT_LAB_ALLOWED_AND_REPORTED.getCode());
//            }
                //根据不同角色设置不同的项目状态
                updateProjectStatus(form.getProjectId(), updateProjectStatus);
                list.add(operationRecord);
            }
            recordMapper.multiInsert(list);
            return Result.success();
        }

        @Override
        public Result getAllOpenTopic () {
            List<OpenTopicInfo> list= projectGroupMapper.getAllOpenTopic();
            for (OpenTopicInfo info:list
                 ) {
                info.setAmountOfSelected(userProjectGroupMapper.getMemberAmountOfProject(info.getId(),null));
            }
            return Result.success(list);
        }

        @Override
        public void generateEstablishExcel (HttpServletResponse response){

            User user = getUserService.getCurrentUser();
            //获取管理人员所管理的学院
            if (user == null){
                throw new GlobalException(CodeMsg.AUTHENTICATION_ERROR);
            }
            Integer college = user.getInstitute();
            List<ProjectTableInfo> list = projectGroupMapper.getProjectTableInfoListByCollegeAndList(college);
            // 1.创建HSSFWorkbook，一个HSSFWorkbook对应一个Excel文件
            XSSFWorkbook wb = new XSSFWorkbook();
            // 2.在workbook中添加一个sheet,对应Excel文件中的sheet(工作栏)
            XSSFSheet sheet = wb.createSheet("workSheet");

            sheet.setPrintGridlines(true);
            //3.1设置字体居中
            XSSFCellStyle cellStyle = wb.createCellStyle();
            //自动换行
            cellStyle.setWrapText(true);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            //当前行的位置
            int index = 0;

            //序号
            XSSFRow title = sheet.createRow(index);
            sheet.setColumnWidth(0, 256 * 150);
            title.setHeight((short) (16 * 50));
            title.createCell(index++).setCellValue("西南石油大学第"+getYear()/100+"期("+getYear()+"-"+(getYear()+1)+"年度)课外开放实验项目立项一览表");

            XSSFRow info = sheet.createRow(index);
            info.createCell(0).setCellValue("单位：（盖章）");
            sheet.setColumnWidth(0, 256 * 20);
            info.createCell(3).setCellValue("填报时间");
            sheet.setColumnWidth(index, 256 * 20);
            index++;

            // 4.设置表头，即每个列的列名
            String[] head = {"院/中心", "序号", "项目名称", "实验类型", "实验时数", "指导教师", "负责学生"
                    , "专业年级", "开始时间", "结束时间", "开放\r\n实验室", "实验室地点", "负责学生\r\n电话", "申请经费（元）", "建议\r\n评审分组"};
            // 4.1创建表头行
            XSSFRow row = sheet.createRow(index++);

            //创建行中的列
            for (int i = 0; i < head.length; i++) {

                // 给列写入数据,创建单元格，写入数据
                row.setHeight((short) (16*40));
                row.createCell(i).setCellValue(head[i]);

            }

            //写入数据
            for (ProjectTableInfo projectTableInfo : list) {
                //创建行
                // 创建行

                row = sheet.createRow(index++);

                //设置行高
                row.setHeight((short) (16 * 22));
                // 序号
                row.createCell(0).setCellValue(ConvertUtil.getStrCollege(projectTableInfo.getCollege()));
                row.createCell(1).setCellValue(projectTableInfo.getId());
                row.createCell(2).setCellValue(projectTableInfo.getProjectName());
                row.createCell(3).setCellValue(projectTableInfo.getExperimentType());
                row.createCell(4).setCellValue(projectTableInfo.getTotalHours());
                row.createCell(5).setCellValue(projectTableInfo.getLeadTeacher());
                row.createCell(6).setCellValue(projectTableInfo.getLeadStudent());
                row.createCell(7).setCellValue(projectTableInfo.getGradeAndMajor());
                row.createCell(8).setCellValue(projectTableInfo.getStartTime());
                row.createCell(9).setCellValue(projectTableInfo.getEndTime());
                row.createCell(10).setCellValue(projectTableInfo.getLabName());
                row.createCell(11).setCellValue(projectTableInfo.getAddress());
                row.createCell(12).setCellValue(projectTableInfo.getLeadStudentPhone());
                row.createCell(13).setCellValue(projectTableInfo.getApplyFunds());
                row.createCell(14).setCellValue(projectTableInfo.getSuggestGroupType());

            }

            sheet.createRow(index++).createCell(0).setCellValue("注1：本表由学院（中心）汇总填报。注2：建议评审分组填A-F,数据来源立项申请表");
            index++;

            XSSFRow end = sheet.createRow(index);
            end.createCell(0).setCellValue("主管院长签字:");
            end.createCell(3).setCellValue("经办人");
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + "test" + ".xlsx");
            try {
                OutputStream os = response.getOutputStream();
                wb.write(os);
                os.flush();
                os.close();
            } catch (IOException e) {
                throw new GlobalException(CodeMsg.DOWNLOAD_ERROR);
            }
        }

        @Override
        public void generateConclusionExcel (HttpServletResponse response){
            User user = getUserService.getCurrentUser();
            Integer college = user.getInstitute();
            // TODO  区分学院
            // 1.创建HSSFWorkbook，一个HSSFWorkbook对应一个Excel文件
            XSSFWorkbook wb = new XSSFWorkbook();
            // 2.在workbook中添加一个sheet,对应Excel文件中的sheet(工作栏)
            XSSFSheet sheet = wb.createSheet("workSheet");

            sheet.setPrintGridlines(true);
            //3.1设置字体居中
            XSSFCellStyle cellStyle = wb.createCellStyle();
            //自动换行
            cellStyle.setWrapText(true);
            cellStyle.setAlignment(HorizontalAlignment.CENTER);
            //当前行的位置
            int index = 0;
            //序号
            XSSFRow title = sheet.createRow(index);
            sheet.setColumnWidth(0, 256 * 150);
            title.setHeight((short) (16 * 50));
            title.createCell(index++).setCellValue("西南石油大学第"+getYear()/100+"期（"+getYear()+"-"+(getYear()+1)+"年度）课外开放实验普通项目结题验收一览表");


            XSSFRow info = sheet.createRow(index);
            sheet.setColumnWidth(0,256*40);
            info.createCell(0).setCellValue("单位：（盖章）");


            // 4.1创建表头行
            XSSFRow row = sheet.createRow(index++);
            String[] columns = {"序号","学院","开放实验室","项目编号","实验类型","实验时数"
                    ,"指导教师","教师公号","学生姓名","学生学号","组员职责","专业年级","起止时间","验收时间","验收结果","备注"};
            //创建行中的列
            sheet.setColumnWidth(0,256*20);
            for (int i = 0; i < columns.length; i++) {

                // 给列写入数据,创建单元格，写入数据
                row.setHeight((short) (16*40));
                row.createCell(i).setCellValue(columns[i]);
            }

            //写入数据
            List<ConclusionDTO> list = projectGroupMapper.selectConclusionDTOs(null);
            for (ConclusionDTO conclusion:list
                 ) {
                // 创建行
                row = sheet.createRow(++index);

                //设置行高
                row.setHeight((short) (16 * 22));
                // 序号
                row.createCell(1).setCellValue(ConvertUtil.getStrCollege(conclusion.getCollege()));
                row.createCell(2).setCellValue(conclusion.getLabName());
                row.createCell(3).setCellValue(conclusion.getId());
                row.createCell(4).setCellValue(ConvertUtil.getStrExperimentType(conclusion.getExperimentType()));
                row.createCell(5).setCellValue(conclusion.getTotalHours());
                row.createCell(6).setCellValue(conclusion.getGuideTeacherName());
                row.createCell(7).setCellValue(conclusion.getGuideTeacherId());
                row.createCell(8).setCellValue(conclusion.getUserName());
                row.createCell(9).setCellValue(conclusion.getUserId());
                row.createCell(10).setCellValue(ConvertUtil.getStrMemberRole(conclusion.getUserRole()));
                row.createCell(11).setCellValue(conclusion.getMajorAndGrade());
                row.createCell(12).setCellValue(conclusion.getStartTimeAndEndTime());

            }

            sheet.createRow(++index).createCell(0).setCellValue("注1：本表由学院（中心）汇总填报。注2：建议评审分组填A-F,数据来源立项申请表");
            index++;

            XSSFRow end = sheet.createRow(index);
            end.createCell(0).setCellValue("主管院长签字:");
            end.createCell(3).setCellValue("经办人");
            response.setContentType("application/vnd.ms-excel;charset=utf-8");
            response.setHeader("Content-disposition", "attachment;filename=" + "Conclusion" + ".xlsx");
            try {
                OutputStream os = response.getOutputStream();
                wb.write(os);
                os.flush();
                os.close();
            } catch (IOException e) {
                throw new GlobalException(CodeMsg.DOWNLOAD_ERROR);
            }

        }

        private int getYear(){
            Calendar calendar = Calendar.getInstance();
            return calendar.get(Calendar.YEAR);
        }

        @Override
        public List getJoinInfo () {
            User currentUser = getUserService.getCurrentUser();
            //检测用户是不是老师--后期可省略
            if (currentUser == null) {
                throw new GlobalException(CodeMsg.AUTHENTICATION_ERROR);
            }

            Role role = roleMapper.selectByUserId(Long.valueOf(currentUser.getCode()));
            if (role.getId() != (RoleType.MENTOR.getValue()).longValue()) {
                throw new GlobalException(CodeMsg.PERMISSION_DENNY);
            }
            List<JoinUnCheckVO> joinUnCheckVOS = new ArrayList<>();
            //获取当前教师参与申报的项目组
            List<ProjectGroup> projectGroups = selectByUserIdAndProjectStatus(Long.valueOf(currentUser.getCode()), ProjectStatus.LAB_ALLOWED.getValue());
            for (int i = 0; i < projectGroups.size(); i++) {
                if (projectGroups.get(i) == null) {
                    i++;
                }
                if (projectGroups.get(i).getIsOpenTopic().equals(OpenTopicType.NOT_OPEN_TOPIC.getValue())) {
                    if (i != projectGroups.size()-1) {
                        i++;
                    }
                }
                ProjectGroup projectGroup = projectGroups.get(i);
                List<UserProjectGroup> userProjectGroups = userProjectService.selectByProjectAndStatus(projectGroup.getId(),null);
                for (UserProjectGroup userProjectGroup : userProjectGroups) {
                    JoinUnCheckVO joinUnCheckVO = getJoinUnCheckVO(userProjectGroup,projectGroup);
                    joinUnCheckVOS.add(joinUnCheckVO);
                }
            }
            return joinUnCheckVOS;
        }

        @Override
        public Result getApplyingJoinInfoByCondition(MemberQueryCondition condition){
            User currentUser = getUserService.getCurrentUser();
            //检测用户
            if (currentUser == null) {
                throw new GlobalException(CodeMsg.AUTHENTICATION_ERROR);
            }
            if (condition == null){
                throw new GlobalException(CodeMsg.PARAM_CANT_BE_NULL);
            }
            List<JoinUnCheckVO> joinUnCheckVOS = new ArrayList<>();
            if (condition.getId() == null){
                List<ProjectGroup> projectGroups = selectByUserIdAndProjectStatus(Long.valueOf(currentUser.getCode()), ProjectStatus.LAB_ALLOWED.getValue());
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
                        JoinUnCheckVO joinUnCheckVO = getJoinUnCheckVO(userProjectGroup,projectGroup);
                        joinUnCheckVOS.add(joinUnCheckVO);
                    }
                }
            //编号，状态同时存在
            }else {
                List<UserProjectGroup> userProjectGroups = userProjectService.selectByProjectAndStatus(condition.getId(), condition.getStatus());
                ProjectGroup projectGroup = projectGroupMapper.selectByPrimaryKey(condition.getId());
                for (UserProjectGroup userProjectGroup : userProjectGroups) {
                    JoinUnCheckVO joinUnCheckVO = getJoinUnCheckVO(userProjectGroup,projectGroup);
                    joinUnCheckVOS.add(joinUnCheckVO);
                }
            }
            return Result.success(joinUnCheckVOS);
        }

    @Override
    public Result addStudentToProject(JoinForm joinForm) {
        User user = getUserService.getCurrentUser();
        Long userId = Long.valueOf(user.getCode());
        UserProjectGroup userProjectGroupOfCurrentUser = userProjectGroupMapper.selectByProjectGroupIdAndUserId(joinForm.getProjectGroupId(),userId);
        if (userProjectGroupOfCurrentUser == null || !userProjectGroupOfCurrentUser.getMemberRole().equals(MemberRole.GUIDANCE_TEACHER.getValue())){
            throw new GlobalException(CodeMsg.USER_NOT_IN_GROUP);
        }

        if (userMapper.selectByUserCode(String.valueOf(joinForm.getUserId())) == null){
            throw new GlobalException(CodeMsg.USER_NO_EXIST);
        }

        if (userProjectGroupMapper.selectByProjectGroupIdAndUserId(joinForm.getProjectGroupId(),joinForm.getUserId())!=null){
            throw new GlobalException(CodeMsg.USER_HAD_JOINED);
        }

        UserProjectGroup userProjectGroup = new UserProjectGroup();
        userProjectGroup.setUserId(joinForm.getUserId());
        userProjectGroup.setProjectGroupId(joinForm.getProjectGroupId());
        userProjectGroup.setMemberRole(MemberRole.NORMAL_MEMBER.getValue());
        userProjectGroup.setStatus(JoinStatus.JOINED.getValue());
        userProjectGroupMapper.insert(userProjectGroup);
        return Result.success();
    }

    @Override
    public Result removeStudentFromProject(JoinForm joinForm) {
        User user = getUserService.getCurrentUser();
        Long userId = Long.valueOf(user.getCode());
        UserProjectGroup userProjectGroupOfCurrentUser = userProjectGroupMapper.selectByProjectGroupIdAndUserId(joinForm.getProjectGroupId(),userId);
        if (userProjectGroupOfCurrentUser == null || !userProjectGroupOfCurrentUser.getMemberRole().equals(MemberRole.PROJECT_GROUP_LEADER.getValue())){
            throw new GlobalException(CodeMsg.USER_NOT_IN_GROUP);
        }
        UserProjectGroup userProjectGroupOfJoinUser = userProjectGroupMapper.selectByProjectGroupIdAndUserId(joinForm.getProjectGroupId(),joinForm.getUserId());
        if (userProjectGroupOfJoinUser == null){
            throw new GlobalException(CodeMsg.USER_HAD_JOINED_CANT_REJECT);
        }
        userProjectGroupMapper.deleteByPrimaryKey(userProjectGroupOfCurrentUser.getId());
        return Result.success();
    }

    private JoinUnCheckVO getJoinUnCheckVO(UserProjectGroup userProjectGroup,ProjectGroup projectGroup){
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
        public Result rejectJoin (JoinForm[]joinForm){
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
        public List<SelectProjectVO> selectByProjectName (String name){
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
        public Result rejectProjectApplyByLabAdministrator (List < ProjectCheckForm > formList) {
            //TODO 身份验证

            return rejectProjectApply(formList, RoleType.LAB_ADMINISTRATOR.getValue());
        }

        @Override
        public Result rejectProjectApplyBySecondaryUnit (List < ProjectCheckForm > formList) {
            //TODO 身份验证

            return rejectProjectApply(formList, RoleType.SECONDARY_UNIT.getValue());
        }

        @Override
        public Result rejectProjectApplyByFunctionalDepartment (List < ProjectCheckForm > formList) {
            //TODO 身份验证

            return rejectProjectApply(formList, RoleType.FUNCTIONAL_DEPARTMENT.getValue());
        }

        /**
         *  因为是批量操作  所以就最好将拒绝和同意分开
         * @param formList 项目拒绝信息集合
         * @return
         */
        @Transactional(rollbackFor = Exception.class)
        public Result rejectProjectApply (List < ProjectCheckForm > formList, Integer role){
            User user = getUserService.getCurrentUser();
            Integer operationUnit;
            Integer rightProjectStatus;
            //这里强制转化不会出现什么问题,问题在于前期将RoleID设置为Long
            switch (role) {
                //如果是实验室主任
                case 4:
                    operationUnit = OperationUnit.LAB_ADMINISTRATOR.getValue();
                    rightProjectStatus = ProjectStatus.DECLARE.getValue();
                    break;
                //二级单位
                case 5:
                    operationUnit = OperationUnit.SECONDARY_UNIT.getValue();
                    rightProjectStatus = ProjectStatus.LAB_ALLOWED_AND_REPORTED.getValue();
                    break;
                //职能部门
                case 6:
                    operationUnit = OperationUnit.FUNCTIONAL_DEPARTMENT.getValue();
                    rightProjectStatus = ProjectStatus.SECONDARY_UNIT_ALLOWED_AND_REPORTED.getValue();
                    break;
                default:
                    //超管执行操作
                    operationUnit = -5;
                    rightProjectStatus = ProjectStatus.REJECT_MODIFY.getValue();
            }
            List<OperationRecord> list = new LinkedList<>();
            OperationRecord operationRecord = new OperationRecord();
            for (ProjectCheckForm form : formList
            ) {
                operationRecord.setRelatedId(form.getProjectId());
                operationRecord.setOperationReason(form.getReason());
                operationRecord.setOperationUnit(OperationType.REJECT.getValue());
                operationRecord.setOperationType(operationUnit);
                operationRecord.setOperationExecutorId(Long.valueOf(user.getCode()));
                //修改状态
                ProjectGroup projectGroup = selectByProjectGroupId(form.getProjectId());
                //如果项目和对应状态不一致
//            if (projectGroup.getStatus().equals(rightProjectStatus)){
//                throw new GlobalException(CodeMsg.PROJECT_HAS_BEEN_REJECTED);
//            }
                updateProjectStatus(form.getProjectId(), ProjectStatus.REJECT_MODIFY.getValue());
                list.add(operationRecord);
            }
            recordMapper.multiInsert(list);
            return Result.success();
        }

        private void setOperationExecutor (OperationRecord operationRecord){
            User user = getUserService.getCurrentUser();
            Long id = Long.valueOf(user.getCode());
            operationRecord.setOperationExecutorId(id);
        }

        @Async
        public void sendMessage (Message message){
            messageRecordMapper.insert(message);
        }

        @Override
        public Result getProjectGroupDetailVOByProjectId (Long projectId){
            if (projectId == null) {
                throw new GlobalException(CodeMsg.PARAM_CANT_BE_NULL);
            }
            return Result.success(projectGroupMapper.getProjectGroupDetailVOByProjectId(projectId));
        }

}
