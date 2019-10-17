package com.swpu.uchain.openexperiment.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.swpu.uchain.openexperiment.DTO.OperationRecordDTO;
import com.swpu.uchain.openexperiment.DTO.ProjectHistoryInfo;
import com.swpu.uchain.openexperiment.domain.Message;
import com.swpu.uchain.openexperiment.VO.project.*;
import com.swpu.uchain.openexperiment.VO.user.UserMemberVO;
import com.swpu.uchain.openexperiment.config.CountConfig;
import com.swpu.uchain.openexperiment.config.UploadConfig;
import com.swpu.uchain.openexperiment.dao.*;
import com.swpu.uchain.openexperiment.domain.*;
import com.swpu.uchain.openexperiment.enums.*;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import com.swpu.uchain.openexperiment.form.funds.FundsForm;
import com.swpu.uchain.openexperiment.form.project.*;
import com.swpu.uchain.openexperiment.form.user.StuMember;
import com.swpu.uchain.openexperiment.form.user.TeacherMember;
import com.swpu.uchain.openexperiment.redis.RedisService;
import com.swpu.uchain.openexperiment.redis.key.ProjectGroupKey;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.*;
import com.swpu.uchain.openexperiment.util.ConvertUtil;
import com.swpu.uchain.openexperiment.util.CountUtil;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.xssf.usermodel.*;
import org.aspectj.apache.bcel.classfile.Code;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;


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
    private CountConfig countConfig;
    private UploadConfig uploadConfig;
    private ConvertUtil convertUtil;
    private GetUserService getUserService;
    private UserProjectGroupMapper userProjectGroupMapper;
    private RoleMapper roleMapper;
    private OperationRecordMapper recordMapper;
    private MessageRecordMapper messageRecordMapper;

    @Autowired
    public ProjectServiceImpl(UserService userService, ProjectGroupMapper projectGroupMapper,
                              RedisService redisService, UserProjectService userProjectService,
                              ProjectFileService projectFileService, FundsService fundsService,
                              CountConfig countConfig, UploadConfig uploadConfig,
                              ConvertUtil convertUtil, GetUserService getUserService,
                              OperationRecordMapper recordMapper,
                              MessageRecordMapper messageRecordMapper,RoleMapper roleMapper,
                              UserProjectGroupMapper userProjectGroupMapper) {
        this.userService = userService;
        this.projectGroupMapper = projectGroupMapper;
        this.redisService = redisService;
        this.userProjectService = userProjectService;
        this.projectFileService = projectFileService;
        this.fundsService = fundsService;
        this.countConfig = countConfig;
        this.uploadConfig = uploadConfig;
        this.convertUtil = convertUtil;
        this.getUserService = getUserService;
        this.userProjectGroupMapper = userProjectGroupMapper;
        this.recordMapper = recordMapper;
        this.messageRecordMapper = messageRecordMapper;
        this.roleMapper = roleMapper;
    }

    @Value("${upload.material_dir}")
    private String materialDir;

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
        ProjectGroup projectGroup = redisService.get(ProjectGroupKey.getByProjectGroupId, projectGroupId + "", ProjectGroup.class);
        if (projectGroup == null) {
            projectGroup = projectGroupMapper.selectByPrimaryKey(projectGroupId);
            if (projectGroup != null) {
                redisService.set(ProjectGroupKey.getByProjectGroupId, projectGroupId + "", projectGroup);
            }
        }
        return projectGroup;
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
        List<ProjectGroup> projectGroups = (List<ProjectGroup>) redisService.getList(
                ProjectGroupKey.getByUserIdAndStatus,
                userId + "_" + projectStatus);
        if (projectGroups == null || projectGroups.size() == 0) {
            projectGroups = projectGroupMapper.selectByUserIdAndStatus(userId, projectStatus);
            if (projectGroups != null && projectGroups.size() != 0) {
                redisService.setList(ProjectGroupKey.getByUserIdAndStatus, userId + "_" + projectStatus, projectGroups);
            }
        }
        return projectGroups;
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
        User currentUser = getUserService.getCurrentUser();

        Integer college =  currentUser.getInstitute();
        //判断用户类型
        if (currentUser.getUserType().intValue() == UserType.STUDENT.getValue()) {
            Result.error(CodeMsg.STUDENT_CANT_APPLY);
        }
        ProjectGroup projectGroup = projectGroupMapper.selectByName(form.getProjectName());
        if (projectGroup != null) {
            return Result.error(CodeMsg.PROJECT_GROUP_HAD_EXIST);
        }

        //当不开放选题时,不进行学生选择
        if (form.getIsOpenTopic().equals(OpenTopicType.NOT_OPEN_TOPIC.getValue()) && form.getStuCodes().length != 0){
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
        //插入数据
        Result result = addProjectGroup(projectGroup);
        if (result.getCode() != 0) {
            throw new GlobalException(CodeMsg.ADD_PROJECT_GROUP_ERROR);
        }

        String[] teacherCodes = form.getTeacherCodes();
        String[] stuCodes = form.getStuCodes();
        boolean isTeacherExist = false;
        for (String teacherCode : teacherCodes) {
            if (teacherCode.equals(currentUser.getCode())) {
                isTeacherExist = true;
            }
        }
        if (!isTeacherExist){
            throw new GlobalException(CodeMsg.LEADING_TEACHER_CONTAINS_ERROR);
        }
        userProjectService.addStuAndTeacherJoin(stuCodes,teacherCodes,projectGroup.getId());

        redisService.deleteFuzzyKey(ProjectGroupKey.getByUserIdAndStatus, currentUser.getId() + "");

        return Result.success();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result applyUpdateProject(UpdateProjectApplyForm updateProjectApplyForm) {
        ProjectGroup projectGroup = selectByProjectGroupId(updateProjectApplyForm.getProjectGroupId());
        if (projectGroup == null) {
            return Result.error(CodeMsg.PROJECT_GROUP_NOT_EXIST);
        }
        User currentUser = getUserService.getCurrentUser();
        UserProjectGroup userProjectGroup = userProjectService.selectByProjectGroupIdAndUserId(updateProjectApplyForm.getProjectGroupId(), currentUser.getId());
        if (userProjectGroup == null) {
            return Result.error(CodeMsg.USER_NOT_IN_GROUP);
        }
        //状态不允许修改
        if (projectGroup.getStatus() != ProjectStatus.DECLARE.getValue().intValue()
                || projectGroup.getStatus() != ProjectStatus.REJECT_MODIFY.getValue().intValue()) {
            return Result.error(CodeMsg.PROJECT_GROUP_INFO_CANT_CHANGE);
        }
        //更新项目组基本信息
        BeanUtils.copyProperties(updateProjectApplyForm, projectGroup);
        update(projectGroup);
        //对文件上传的处理,1.获取文件名,2.保存文件,3.维护数据库
//        Result result = projectFileService.uploadApplyDoc(file, projectGroup.getId());
//        if (result.getCode() != 0){
//            throw new GlobalException(CodeMsg.UPLOAD_ERROR);
//        }
        userProjectService.deleteByProjectGroupId(projectGroup.getId());
        String[] stuCodes = updateProjectApplyForm.getStuCodes();
        String[] teacherCodes = updateProjectApplyForm.getTeacherCodes();
        userProjectService.addStuAndTeacherJoin(stuCodes, teacherCodes, projectGroup.getId());
        //修改项目状态,重新开始申报
        updateProjectStatus(projectGroup.getId(),ProjectStatus.DECLARE.getValue());

        //将之前的历史数据设置为不可见
        //type传入为空则更新所有
        recordMapper.setNotVisibleByProjectId(projectGroup.getId(),null);
        return Result.success();
    }

    @Override
    public Result getCurrentUserProjects(Integer projectStatus) {
        User currentUser = getUserService.getCurrentUser();
        if (userService == null) {
            throw new GlobalException(CodeMsg.AUTHENTICATION_ERROR);
        }
        List<ProjectGroup> projectGroups = selectByUserIdAndProjectStatus(currentUser.getId(), projectStatus);
        //设置当前用户的所有项目VO
        List<MyProjectVO> myProjectVOS = new ArrayList<>();
        for (ProjectGroup projectGroup : projectGroups) {
            MyProjectVO myProjectVO = new MyProjectVO();
            BeanUtils.copyProperties(projectGroup, myProjectVO);
            myProjectVO.setProjectGroupId(projectGroup.getId());
            myProjectVO.setProjectDetails(getProjectDetails(projectGroup));
            myProjectVOS.add(myProjectVO);
        }
        return Result.success(myProjectVOS);
    }

    public ProjectDetails getProjectDetails(ProjectGroup projectGroup) {
        ProjectDetails projectDetails = new ProjectDetails();
        projectDetails.setLabName(projectGroup.getLabName());
        projectDetails.setAddress(projectGroup.getAddress());
        //设置创建人,即项目负责人
        User user = userService.selectByUserId(projectGroup.getCreatorId());
        UserProjectGroup userProjectGroup = userProjectService.selectByProjectGroupIdAndUserId(
                projectGroup.getId(),
                user.getId());
        projectDetails.setCreator(new UserMemberVO(
                user.getId(),
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
        List<OperationRecordDTO> operationRecordDTOS = new LinkedList<>();
        for (ProjectCheckForm projectCheckForm : projectGroupIdList) {
            Result result = updateProjectStatus(projectCheckForm.getProjectId(), ProjectStatus.ESTABLISH.getValue());
            if (result.getCode() != 0) {
                throw new GlobalException(CodeMsg.UPDATE_ERROR);
            }
            result = fundsService.agreeFunds(projectCheckForm.getProjectId());
            if (result.getCode() != 0) {
                throw new GlobalException(CodeMsg.UPDATE_ERROR);
            }
            OperationRecordDTO operationRecordDTO = new OperationRecordDTO();
            operationRecordDTO.setOperationType(OperationType.PROJECT_OPERATION_TYPE3.getValue().toString());
            operationRecordDTO.setOperationContent(CheckResultType.PASS.getValue());
            operationRecordDTO.setOperationReason(projectCheckForm.getReason());
            operationRecordDTO.setRelatedId(projectCheckForm.getProjectId());
            operationRecordDTOS.add(operationRecordDTO);
            setOperationExecutor(operationRecordDTO);
        }
        recordMapper.multiInsert(operationRecordDTOS);
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
            BeanUtils.copyProperties(projectGroup, applyKeyFormInfoVO);
            applyKeyFormInfoVO.setProjectGroupId(projectGroup.getId());
            return Result.success(applyKeyFormInfoVO);
        } else {
            ApplyGeneralFormInfoVO applyGeneralFormInfoVO = convertUtil.addUserDetailVO(users, ApplyGeneralFormInfoVO.class);
            BeanUtils.copyProperties(projectGroup, applyGeneralFormInfoVO);
            applyGeneralFormInfoVO.setProjectGroupId(projectGroup.getId());
            return Result.success(applyGeneralFormInfoVO);
        }
    }

    @Override
    public Result appendCreateApply(AppendApplyForm appendApplyForm) {
        User currentUser = getUserService.getCurrentUser();
        //获取用户所在的用户项目组信息
        UserProjectGroup userProjectGroup = userProjectService.selectByProjectGroupIdAndUserId(
                appendApplyForm.getProjectGroupId(), currentUser.getId());
        if (userProjectGroup == null) {
            return Result.error(CodeMsg.USER_NOT_IN_GROUP);
        }

        if (userProjectGroup.getStatus() < 5){
            throw new GlobalException(CodeMsg.FUNDS_NOT_EXIST);
        }

        //拒绝普通用户进行该项操作
        if (userProjectGroup.getMemberRole().intValue() == MemberRole.NORMAL_MEMBER.getValue()) {
            Result.error(CodeMsg.PERMISSION_DENNY);
        }
        FundsForm[] fundsForms = appendApplyForm.getFundsForms();
        for (FundsForm fundsForm : fundsForms) {
            //资金id不为空进行更新操作
            if (fundsForm.getFundsId() != null) {
                Funds funds = fundsService.selectById(fundsForm.getFundsId());

                if (funds == null) {
                    return Result.error(CodeMsg.FUNDS_NOT_EXIST);
                }
                //申请通过的资金无进行更新操作
                if (funds.getStatus().intValue() == FundsStatus.AGREED.getValue()) {
                    return Result.error(CodeMsg.FUNDS_AGREE_CANT_CHANGE);
                }
                BeanUtils.copyProperties(fundsForm, funds);
                funds.setUpdateTime(new Date());
                if (!fundsService.update(funds)) {
                    return Result.error(CodeMsg.UPDATE_ERROR);
                }
            } else {
                //添加资金信息
                Funds funds = new Funds();
                BeanUtils.copyProperties(fundsForm, funds);
                funds.setProjectGroupId(appendApplyForm.getProjectGroupId());
                funds.setApplicantId(currentUser.getId());
                funds.setStatus(FundsStatus.APPLYING.getValue());
                funds.setCreateTime(new Date());
                funds.setUpdateTime(new Date());
                if (!fundsService.insert(funds)) {
                    return Result.error(CodeMsg.ADD_ERROR);
                }
            }
        }
        return Result.success();
    }

    @Override
    public Result getPendingApprovalProjectByLabAdministrator(Integer pageNum) {
        //TODO 身份验证
        return getCheckInfo(pageNum,RoleType.LAB_ADMINISTRATOR.getValue());
    }

    @Override
    public Result getPendingApprovalProjectBySecondaryUnit(Integer pageNum) {
        //TODO 身份验证

        return getCheckInfo(pageNum, RoleType.SECONDARY_UNIT.getValue());
    }

    @Override
    public Result getPendingApprovalProjectByFunctionalDepartment(Integer pageNum) {
        //TODO 身份验证

        return getCheckInfo(pageNum, RoleType.FUNCTIONAL_DEPARTMENT.getValue());
    }

    public Result getCheckInfo(Integer pageNum, Integer role) {
        //获取工号,并通过工号获取角色,再通过角色判定操作类型(只针对于审核这一步)
        Integer projectStatus;
        //这里强制转化不会出现什么问题,问题在于前期将RoleID设置为Long
        switch (role) {
            //职能部门
            case 6:
                projectStatus = ProjectStatus.SECONDARY_UNIT_ALLOWED_AND_REPORTED.getValue();
                break;
            //二级部门(学院领导)
            case 5:
                projectStatus = ProjectStatus.LAB_ALLOWED_AND_REPORTED.getValue();
                break;
            //实验室主任
            case 4:
                projectStatus = ProjectStatus.DECLARE.getValue();
                break;
            default:
                //超管执行操作
                projectStatus = -1;
        }
        PageHelper.startPage(pageNum, countConfig.getCheckProject());
        List<CheckProjectVO> checkProjectVOs = projectGroupMapper.selectApplyOrderByTime(projectStatus);
        for (CheckProjectVO checkProjectVO : checkProjectVOs) {
            List<UserProjectGroup> userProjectGroups = userProjectService.selectByProjectGroupId(checkProjectVO.getProjectGroupId());
            List<UserMemberVO> guidanceTeachers = new ArrayList<>();
            List<UserMemberVO> memberStudents = new ArrayList<>();
            for (UserProjectGroup userProjectGroup : userProjectGroups) {
                UserMemberVO userMemberVO = new UserMemberVO();
                User user = userService.selectByUserId(userProjectGroup.getUserId());
                userMemberVO.setUserId(user.getId());
                userMemberVO.setUserName(user.getRealName());
                userMemberVO.setMemberRole(userProjectGroup.getMemberRole());
                //设置负责人(项目组长)电话
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
                List<Funds> fundsDetails = fundsService.getFundsDetails(userProjectGroup.getProjectGroupId());
                checkProjectVO.setFundsApplyAmount(CountUtil.countFundsTotalAmount(fundsDetails));
                checkProjectVO.setMemberStudents(memberStudents);
                checkProjectVO.setGuidanceTeachers(guidanceTeachers);
            }
        }
        PageInfo<CheckProjectVO> pageInfo = new PageInfo<>(checkProjectVOs);
        return Result.success(pageInfo);
    }


    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result reportToCollegeLeader(Long projectGroupId) {
        if (!ProjectStatus.LAB_ALLOWED.getValue().equals(projectGroupMapper.selectByPrimaryKey(projectGroupId).getStatus())){
            throw new GlobalException(CodeMsg.CURRENT_PROJECT_STATUS_ERROR);
        }
        OperationRecordDTO operationRecordDTO = new OperationRecordDTO();
        operationRecordDTO.setRelatedId(projectGroupId);
        operationRecordDTO.setOperationContent(CheckResultType.PASS.getValue());
        operationRecordDTO.setOperationType(OperationType.PROJECT_REPORT_TYPE1.getValue().toString());
        setOperationExecutor(operationRecordDTO);
        recordMapper.insert(operationRecordDTO);
        return updateProjectStatus(projectGroupId, ProjectStatus.LAB_ALLOWED_AND_REPORTED.getValue());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result reportToFunctionalDepartment(Long projectGroupId) {
        if (!ProjectStatus.SECONDARY_UNIT_ALLOWED.getValue().equals(projectGroupMapper.selectByPrimaryKey(projectGroupId).getStatus())){
            throw new GlobalException(CodeMsg.CURRENT_PROJECT_STATUS_ERROR);
        }

        OperationRecordDTO operationRecordDTO = new OperationRecordDTO();
        operationRecordDTO.setRelatedId(projectGroupId);
        operationRecordDTO.setOperationContent(CheckResultType.PASS.getValue());
        operationRecordDTO.setOperationType(OperationType.PROJECT_REPORT_TYPE2.getValue().toString());
        setOperationExecutor(operationRecordDTO);
        recordMapper.insert(operationRecordDTO);
        return updateProjectStatus(projectGroupId, ProjectStatus.SECONDARY_UNIT_ALLOWED_AND_REPORTED.getValue());
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public Result ensureOrNotModify(ConfirmForm confirmForm) {
        Integer result = confirmForm.getResult();
        Long projectId = confirmForm.getProjectId();
        //确认修改
        if (recordMapper.selectDesignatedTypeListByRelatedIdAndType
                (OperationType.PROJECT_MODIFY_TYPE1.getValue(),projectId).size() == 0){
            throw new GlobalException(CodeMsg.PROJECT_NOT_MODIFY_BY_FUNCTION_DEPARTMENT);
        }
        //如果项目通过
        if (result.toString().equals(CheckResultType.PASS.getValue())){
            updateProjectStatus(projectId,ProjectStatus.ESTABLISH.getValue());
        }else if (result.toString().equals(CheckResultType.REJECTED.getValue())){
            updateProjectStatus(projectId,ProjectStatus.ESTABLISH_FAILED.getValue());
        }
        recordMapper.setNotVisibleByProjectId(projectId,OperationType.PROJECT_MODIFY_TYPE1.getValue());
        return Result.success();
    }

    @Override
    public Result getProjectDetailById(Long projectId) {
        List<ProjectHistoryInfo> list = recordMapper.selectAllByProjectId(projectId);
        return Result.success(ConvertUtil.getConvertedProjectHistoryInfo(list));
    }

    @Override
    public Result approveProjectApplyByLabAdministrator(List<ProjectCheckForm> list) {
        return checkProjectApply(list,RoleType.LAB_ADMINISTRATOR.getValue());
    }

    @Override
    public Result approveProjectApplyBySecondaryUnit(List<ProjectCheckForm> list) {
        return checkProjectApply(list,RoleType.SECONDARY_UNIT.getValue());
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Result createKeyApply(KeyProjectApplyForm form) {
        String projectName = form.getProjectName();
        ProjectGroup projectGroup = projectGroupMapper.selectByPrimaryProjectName(projectName);
        if (projectGroup == null){
            throw new GlobalException(CodeMsg.PROJECT_GROUP_NOT_EXIST);
        }
        //更新项目状态为等待知道老师审核
        updateProjectStatus(projectGroup.getId(),ProjectStatus.TO_DE_CONFIRMED.getValue());
        List<StuMember> stuMemberList = form.getMembers();

        Long projectId = projectGroup.getId();

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

    @Transactional(rollbackFor = Exception.class)
    public Result checkProjectApply(List<ProjectCheckForm> formList,Integer role) {
        User user = getUserService.getCurrentUser();
        String checkOperationType;
        Integer projectStatus = null;
        switch (role) {
            //如果是实验室主任
            case 4:
                checkOperationType = OperationType.PROJECT_OPERATION_TYPE1.getValue().toString();
                projectStatus = ProjectStatus.LAB_ALLOWED.getValue();
                break;
            case 5:
                checkOperationType = OperationType.PROJECT_OPERATION_TYPE2.getValue().toString();
                projectStatus = ProjectStatus.SECONDARY_UNIT_ALLOWED.getValue();
                break;
            default:
                //超管执行操作
                checkOperationType = "-1";
        }
        List<OperationRecordDTO> list = new LinkedList<>();
        OperationRecordDTO operationRecordDTO = new OperationRecordDTO();
        for (ProjectCheckForm form : formList
        ) {
            operationRecordDTO.setRelatedId(form.getProjectId());
            operationRecordDTO.setOperationReason(form.getReason());
            operationRecordDTO.setOperationContent(CheckResultType.PASS.getValue());
            operationRecordDTO.setOperationType(checkOperationType);
            operationRecordDTO.setOperationExecutorId(Long.valueOf(user.getCode()));
            //当角色是实验室主任的时候,项目状态不是
            ProjectGroup projectGroup = selectByProjectGroupId(form.getProjectId());
            if (role == 4 && !projectGroup.getStatus().equals(projectStatus)){
                throw new GlobalException(CodeMsg.PROJECT_STATUS_IS_NOT_DECLARE);
            }
            //如果不是实验室上报状态,抛出异常
            if (role == 5 && !projectGroup.getStatus().equals(projectStatus)){
                throw new GlobalException(CodeMsg.PROJECT_STATUS_IS_NOT_LAB_ALLOWED_AND_REPORTED);
            }
            //更具不同角色设置不同的项目状态
            updateProjectStatus(form.getProjectId(), projectStatus);
            list.add(operationRecordDTO);
        }
        recordMapper.multiInsert(list);
        return Result.success();
    }

    @Override
    public Result getAllOpenTopic() {
        return Result.success(projectGroupMapper.getAllOpenTopic());
    }

    /**
     * 指导老师获取待审批的项目信息
     * @return
     */
    @Override
    public Result getPendingReviewByLeadTeacher() {
        User user = getUserService.getCurrentUser();
        if (user == null){
            throw new GlobalException(CodeMsg.AUTHENTICATION_ERROR);
        }
        List<ProjectGroup> list = projectGroupMapper.selectByUserIdAndStatus(Long.valueOf(user.getCode()),ProjectStatus.TO_DE_CONFIRMED.getValue());
        return Result.success(list);
    }

        @Override
    public Result getPendingReviewByLabLeader() {
        //传入用户为空,则获取所有的指定状态的项目
        return Result.success(projectGroupMapper.selectByCollegeIdAndStatus(null,ProjectStatus.DECLARE.getValue()));
    }

    @Override
    public void generateEstablishExcel(HttpServletResponse response) {
        User user = getUserService.getCurrentUser();
        //获取管理人员所管理的学院
        Integer college = user.getInstitute();
        List<ProjectTableInfo> list = projectGroupMapper.getProjectTableInfoListByCollege(college);
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
        sheet.setColumnWidth(0,256*150);
        title.setHeight((short) (16*50));
        title.createCell(index++).setCellValue("西南石油大学第__期(20___-20___年度)课外开放实验项目立项一览表");

        XSSFRow info = sheet.createRow(index);
        info.createCell(0).setCellValue("单位：（盖章）");
        sheet.setColumnWidth(0,256*20);
        info.createCell(3).setCellValue("填报时间");
        sheet.setColumnWidth(index,256*20);
        index++;

        // 4.设置表头，即每个列的列名
        String[] head = {"院/中心","序号","项目名称","实验类型","实验时数","指导教师","负责学生"
                ,"专业年级","开始时间","结束时间","开放实验室","实验室地点","负责学生电话","申请经费（元）","建议评审分组"};
        // 4.1创建表头行
        XSSFRow row = sheet.createRow(index++);

        //创建行中的列
        for (int i = 0; i < head.length; i++) {

            // 给列写入数据,创建单元格，写入数据
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
            row.createCell(1).setCellValue(projectTableInfo.getProjectId());
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
        response.setHeader("Content-disposition", "attachment;filename="+"test"+".xlsx");
        try {
            OutputStream os = response.getOutputStream();
            wb.write(os);
            os.flush();
            os.close();
        }catch (IOException e){
            throw new GlobalException(CodeMsg.DOWNLOAD_ERROR);
        }
    }

    @Override
    public void generateConclusionExcel(HttpServletResponse response) {

    }

    @Override
    public List getJoinInfo() {
        User currentUser = getUserService.getCurrentUser();
        //检测用户是不是老师--后期可省略

        Role role = roleMapper.selectByUserId(Long.valueOf(currentUser.getCode()));
        if (role.getId() != (RoleType.MENTOR.getValue()).longValue()) {
            throw new GlobalException(CodeMsg.PERMISSION_DENNY);
        }
        List<JoinUnCheckVO> joinUnCheckVOS = new ArrayList<>();
        //获取当前教师参与申报的项目组
        List<ProjectGroup> projectGroups = selectByUserIdAndProjectStatus(currentUser.getId(), ProjectStatus.DECLARE.getValue());
        projectGroups.forEach(projectGroup -> {
            List<UserProjectGroup> userProjectGroups = userProjectService.selectByProjectAndStatus(projectGroup.getId(), JoinStatus.APPLYING.getValue());
            for (UserProjectGroup userProjectGroup : userProjectGroups) {
                JoinUnCheckVO joinUnCheckVO = new JoinUnCheckVO();
                User user = userService.selectByUserId(userProjectGroup.getUserId());
                joinUnCheckVO.setProjectGroupId(projectGroup.getId());
                joinUnCheckVO.setProjectName(projectGroup.getProjectName());
                joinUnCheckVO.setPersonJudge(userProjectGroup.getPersonalJudge());
                joinUnCheckVO.setTechnicalRole(userProjectGroup.getTechnicalRole());
                joinUnCheckVO.setUserDetailVO(convertUtil.convertUserDetailVO(user));
                joinUnCheckVOS.add(joinUnCheckVO);
            }
        });
        return joinUnCheckVOS;
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
    public Result rejectProjectApplyByLabAdministrator(List<ProjectCheckForm> formList) {
        //TODO 身份验证

        return rejectProjectApply(formList,RoleType.LAB_ADMINISTRATOR.getValue());
    }

    @Override
    public Result rejectProjectApplyBySecondaryUnit(List<ProjectCheckForm> formList) {
        //TODO 身份验证

        return rejectProjectApply(formList,RoleType.SECONDARY_UNIT.getValue());
    }

    @Override
    public Result rejectProjectApplyByFunctionalDepartment(List<ProjectCheckForm> formList) {
        //TODO 身份验证

        return rejectProjectApply(formList,RoleType.FUNCTIONAL_DEPARTMENT.getValue());
    }

    /**
     *  因为是批量操作  所以就最好将拒绝和同意分开
     * @param formList 项目拒绝信息集合
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    public Result rejectProjectApply(List<ProjectCheckForm> formList,Integer role) {
        User user = getUserService.getCurrentUser();
        String checkOperationType;
        Integer rightProjectStatus;
        //这里强制转化不会出现什么问题,问题在于前期将RoleID设置为Long
        switch (role) {
            //如果是实验室主任
            case 4:
                checkOperationType = OperationType.PROJECT_OPERATION_TYPE1.getValue().toString();
                rightProjectStatus = ProjectStatus.DECLARE.getValue();
                break;
            //二级单位
            case 5:
                checkOperationType = OperationType.PROJECT_OPERATION_TYPE2.getValue().toString();
                rightProjectStatus = ProjectStatus.LAB_ALLOWED_AND_REPORTED.getValue();
                break;
            //职能部门
            case 6:
                checkOperationType = OperationType.PROJECT_OPERATION_TYPE3.getValue().toString();
                rightProjectStatus = ProjectStatus.SECONDARY_UNIT_ALLOWED_AND_REPORTED.getValue();
                break;
            default:
                //超管执行操作
                checkOperationType = "-1";
                rightProjectStatus = ProjectStatus.REJECT_MODIFY.getValue();
        }
        List<OperationRecordDTO> list = new LinkedList<>();
        OperationRecordDTO operationRecordDTO = new OperationRecordDTO();
        for (ProjectCheckForm form : formList
        ) {
            operationRecordDTO.setRelatedId(form.getProjectId());
            operationRecordDTO.setOperationReason(form.getReason());
            operationRecordDTO.setOperationContent(CheckResultType.REJECTED.getValue());
            operationRecordDTO.setOperationType(checkOperationType);
            operationRecordDTO.setOperationExecutorId(Long.valueOf(user.getCode()));
            //修改状态
            ProjectGroup projectGroup = selectByProjectGroupId(form.getProjectId());
            //如果项目和对应状态不一致
            if (projectGroup.getStatus().equals(rightProjectStatus)){
                throw new GlobalException(CodeMsg.PROJECT_HAS_BEEN_REJECTED);
            }
            updateProjectStatus(form.getProjectId(), ProjectStatus.REJECT_MODIFY.getValue());
            list.add(operationRecordDTO);
        }
        recordMapper.multiInsert(list);
        return Result.success();
    }

    private void setOperationExecutor(OperationRecordDTO operationRecordDTO){
        User user = getUserService.getCurrentUser();
        Long id = Long.valueOf(user.getCode());
        operationRecordDTO.setRelatedId(id);
    }

    @Async
    public void sendMessage(Message message){
        messageRecordMapper.insert(message);
    }

    @Override
    public Result getProjectGroupDetailVOByProjectId(Long projectId) {
        if(projectId == null){
            throw new GlobalException(CodeMsg.PROJECT_GROUP_NOT_EXIST);
        }
        return Result.success(projectGroupMapper.getProjectGroupDetailVOByProjectId(projectId));
    }
}
