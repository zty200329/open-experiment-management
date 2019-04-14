package com.swpu.uchain.openexperiment.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.swpu.uchain.openexperiment.VO.project.*;
import com.swpu.uchain.openexperiment.VO.user.UserMemberVO;
import com.swpu.uchain.openexperiment.config.CountConfig;
import com.swpu.uchain.openexperiment.config.UploadConfig;
import com.swpu.uchain.openexperiment.dao.ProjectGroupMapper;
import com.swpu.uchain.openexperiment.domain.*;
import com.swpu.uchain.openexperiment.enums.*;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import com.swpu.uchain.openexperiment.form.funds.FundsForm;
import com.swpu.uchain.openexperiment.form.project.AppendApplyForm;
import com.swpu.uchain.openexperiment.form.project.CreateProjectApplyForm;
import com.swpu.uchain.openexperiment.form.project.JoinForm;
import com.swpu.uchain.openexperiment.form.project.UpdateProjectApplyForm;
import com.swpu.uchain.openexperiment.redis.RedisService;
import com.swpu.uchain.openexperiment.redis.key.ProjectGroupKey;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.*;
import com.swpu.uchain.openexperiment.util.ConvertUtil;
import com.swpu.uchain.openexperiment.util.CountUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
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
    //TODO,答辩小组Service
    @Autowired
    private ProjectFileService projectFileService;
    @Autowired
    private ProjectService projectService;
    @Autowired
    private FundsService fundsService;
    @Autowired
    private CountConfig countConfig;
    @Autowired
    private UploadConfig uploadConfig;
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
        projectGroup.setUpdateTime(new Date());
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
    public Result addProjectGroup(ProjectGroup projectGroup) {
        projectGroup.setCreateTime(new Date());
        projectGroup.setUpdateTime(new Date());
        if (insert(projectGroup)){
            return Result.success();
        }
        return Result.error(CodeMsg.ADD_ERROR);
    }

    @Override
    public List<ProjectGroup> selectByUserIdAndProjectStatus(Long userId, Integer projectStatus){
        //获取当前用户参与的所有项目
        List<ProjectGroup> projectGroups = (List<ProjectGroup>) redisService.getList(
                ProjectGroupKey.getByUserIdAndStatus,
                userId + "_" + projectStatus);
        if (projectGroups == null || projectGroups.size() == 0){
            projectGroups = projectGroupMapper.selectByUserIdAndStatus(userId, projectStatus);
            if (projectGroups != null && projectGroups.size() != 0){
                redisService.setList(ProjectGroupKey.getByUserIdAndStatus, userId + "_" + projectStatus, projectGroups);
            }
        }
        return projectGroups;
    }

    @Override
    @Transactional
    public Result applyCreateProject(CreateProjectApplyForm createProjectApplyForm, MultipartFile file) {
        User currentUser = userService.getCurrentUser();
        if (currentUser.getUserType().intValue() == UserType.STUDENT.getValue()){
            Result.error(CodeMsg.STUDENT_CANT_APPLY);
        }
        ProjectGroup projectGroup = projectGroupMapper.selectByName(createProjectApplyForm.getProjectName());
        if (projectGroup != null){
            return Result.error(CodeMsg.PROJECT_GROUP_HAD_EXIST);
        }
        projectGroup = new ProjectGroup();
        BeanUtils.copyProperties(createProjectApplyForm, projectGroup);
        projectGroup.setStatus(ProjectStatus.DECLARE.getValue());
        projectGroup.setCreatorId(currentUser.getId());
        Result result = addProjectGroup(projectGroup);
        if (result.getCode() != 0){
            throw new GlobalException(CodeMsg.ADD_PROJECT_GROUP_ERROR);
        }
        //对文件上传的处理,1.获取文件名,2.保存文件,3.维护数据库
        result = projectFileService.uploadApplyDoc(file, projectGroup.getId());
        if (result.getCode() != 0){
            throw new GlobalException(CodeMsg.UPLOAD_ERROR);
        }
        String[] stuCodes = null;
        if (createProjectApplyForm.getStuCodes() != null){
            stuCodes = createProjectApplyForm.getStuCodes().split(",");
        }
        String[] teacherCodes = createProjectApplyForm.getTeacherCodes().split(",");
        addStuAndTeacherJoin(stuCodes, teacherCodes, projectGroup.getId());
        return Result.success();
    }

    @Override
    @Transactional
    public Result applyUpdateProject(UpdateProjectApplyForm updateProjectApplyForm, MultipartFile file) {
        ProjectGroup projectGroup = selectByProjectGroupId(updateProjectApplyForm.getProjectGroupId());
        if (projectGroup == null){
            return Result.error(CodeMsg.PROJECT_GROUP_NOT_EXIST);
        }
        User currentUser = userService.getCurrentUser();
        UserProjectGroup userProjectGroup = userProjectService.selectByProjectGroupIdAndUserId(updateProjectApplyForm.getProjectGroupId(), currentUser.getId());
        if (userProjectGroup == null){
            return Result.error(CodeMsg.USER_NOT_IN_GROUP);
        }
        if (projectGroup.getStatus() != ProjectStatus.DECLARE.getValue().intValue()
                || projectGroup.getStatus() != ProjectStatus.REJECT_MODIFY.getValue().intValue()){
            return Result.error(CodeMsg.PROJECT_GROUP_INFO_CANT_CHANGE);
        }
        //更新项目组基本信息
        BeanUtils.copyProperties(updateProjectApplyForm, projectGroup);
        update(projectGroup);
        //对文件上传的处理,1.获取文件名,2.保存文件,3.维护数据库
        Result result = projectFileService.uploadApplyDoc(file, projectGroup.getId());
        if (result.getCode() != 0){
            throw new GlobalException(CodeMsg.UPLOAD_ERROR);
        }
        userProjectService.deleteByProjectGroupId(projectGroup.getId());
        String[] stuCodes = null;
        if (updateProjectApplyForm.getStuCodes() != null) {
            stuCodes = updateProjectApplyForm.getStuCodes().split(",");
        }
        String[] teacherCodes = updateProjectApplyForm.getTeacherCodes().split(",");
        addStuAndTeacherJoin(stuCodes, teacherCodes, projectGroup.getId());
        return Result.success();
    }

    public void addStuAndTeacherJoin(String[] stuCodes, String[] teacherCodes, Long projectGroupId){
        Result result = userService.createUserJoin(
                teacherCodes,
                projectGroupId,
                UserType.LECTURER);
        if (result.getCode() != 0){
            throw new GlobalException(CodeMsg.ADD_USER_JOIN_ERROR);
        }
        result = userService.createUserJoin(
                stuCodes,
                projectGroupId,
                UserType.STUDENT);
        if (result.getCode() != 0){
            throw new GlobalException(CodeMsg.ADD_USER_JOIN_ERROR);
        }
    }

    @Override
    public Result getCurrentUserProjects(Integer projectStatus) {
        User currentUser = userService.getCurrentUser();
        if (userService == null){
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

    public ProjectDetails getProjectDetails(ProjectGroup projectGroup){
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
            if (userProject.getMemberRole().intValue() == MemberRole.PROJECT_GROUP_LEADER.getValue()){
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
            if (fundsDetail.getStatus().intValue() == FundsStatus.AGREED.getValue()){
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

    public Result updateProjectStatus(Long projectGroupId, Integer projectStatus){
        ProjectGroup projectGroup = selectByProjectGroupId(projectGroupId);
        if (projectGroup == null){
            return Result.error(CodeMsg.PROJECT_GROUP_NOT_EXIST);
        }
        projectGroup.setStatus(projectStatus);
        if (update(projectGroup)) {
            return Result.success();
        }
        return Result.error(CodeMsg.UPDATE_ERROR);
    }

    @Override
    @Transactional
    public Result agreeEstablish(Long projectGroupId) {
        Result result = updateProjectStatus(projectGroupId, ProjectStatus.ESTABLISH.getValue());
        if (result.getCode() != 0){
            return result;
        }
        result = fundsService.agreeFunds(projectGroupId);
        if (result.getCode() != 0){
            return result;
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
        if (projectGroup.getProjectType().intValue() == ProjectType.KEY.getValue()){
            ApplyKeyFormInfoVO applyKeyFormInfoVO = ConvertUtil.addUserDetailVO(users, ApplyKeyFormInfoVO.class);
            applyKeyFormInfoVO.setFundsDetails(fundsService.getFundsDetails(projectGroupId));
            BeanUtils.copyProperties(projectGroup, applyKeyFormInfoVO);
            applyKeyFormInfoVO.setProjectGroupId(projectGroup.getId());
            return Result.success(applyKeyFormInfoVO);
        }else {
            ApplyGeneralFormInfoVO applyGeneralFormInfoVO = ConvertUtil.addUserDetailVO(users, ApplyGeneralFormInfoVO.class);
            BeanUtils.copyProperties(projectGroup, applyGeneralFormInfoVO);
            applyGeneralFormInfoVO.setProjectGroupId(projectGroup.getId());
            return Result.success(applyGeneralFormInfoVO);
        }
    }

    @Override
    public Result appendCreateApply(AppendApplyForm appendApplyForm) {
        User currentUser = userService.getCurrentUser();
        UserProjectGroup userProjectGroup = userProjectService.selectByProjectGroupIdAndUserId(
                appendApplyForm.getProjectGroupId(),
                currentUser.getId());
        if (userProjectGroup == null){
            return Result.error(CodeMsg.USER_NOT_IN_GROUP);
        }
        if (userProjectGroup.getMemberRole().intValue() == MemberRole.NORMAL_MEMBER.getValue()){
            Result.error(CodeMsg.PERMISSION_DENNY);
        }
        FundsForm[] fundsForms = appendApplyForm.getFundsForms();
        for (FundsForm fundsForm : fundsForms) {
            //资金id不为空进行更新操作
            if (fundsForm.getFundsId() != null){
                Funds funds = fundsService.selectById(fundsForm.getFundsId());
                if (funds == null){
                    return Result.error(CodeMsg.FUNDS_NOT_EXIST);
                }
                //申请通过的资金无进行更新操作
                if (funds.getStatus().intValue() == FundsStatus.AGREED.getValue()){
                    return Result.error(CodeMsg.FUNDS_AGREE_CANT_CHANGE);
                }
                BeanUtils.copyProperties(fundsForm, funds);
                funds.setUpdateTime(new Date());
                if (!fundsService.update(funds)) {
                    return Result.error(CodeMsg.UPDATE_ERROR);
                }
            }else {
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
    public Result getCheckInfo(Integer pageNum, Integer projectStatus) {
        PageHelper.startPage(pageNum, countConfig.getCheckProject());
        List<CheckProjectVO> checkProjectVOS = projectGroupMapper.selectApplyOrderByTime(projectStatus);
        for (CheckProjectVO checkProjectVO : checkProjectVOS) {
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
                switch (userProjectGroup.getMemberRole()){
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
                if (applyProjectFile != null){
                    checkProjectVO.setApplyFileId(applyProjectFile.getId());
                }
                List<Funds> fundsDetails = fundsService.getFundsDetails(userProjectGroup.getProjectGroupId());
                checkProjectVO.setFundsApplyAmount(CountUtil.countFundsTotalAmount(fundsDetails));
                checkProjectVO.setMemberStudents(memberStudents);
                checkProjectVO.setGuidanceTeachers(guidanceTeachers);
            }
        }
        PageInfo<CheckProjectVO> pageInfo = new PageInfo<>(checkProjectVOS);
        return Result.success(pageInfo);
    }

    @Override
    public Result rejectModifyApply(Long projectGroupId) {
        //TODO,可能会补充站内消息模块功能
        return updateProjectStatus(projectGroupId, ProjectStatus.REJECT_MODIFY.getValue());
    }

    @Override
    public Result reportToCollegeLeader(Long projectGroupId) {
        //TODO,可能会补充站内消息模块功能
        return updateProjectStatus(projectGroupId, ProjectStatus.REPORT_COLLEGE_LEADER.getValue());
    }

    @Override
    public void generateEstablishExcel() {
        //TODO,使用workbook生成总览表
    }

    @Override
    public void generateConclusionExcel() {
        //TODO,使用workbook生成总览表
    }

    @Override
    public List getJoinInfo() {
        User currentUser = userService.getCurrentUser();
        //检测学生无法拥有检查看审批列表的功能
        if (currentUser.getUserType().intValue() == UserType.STUDENT.getValue()){
            throw new GlobalException(CodeMsg.PERMISSION_DENNY);
        }
        List<JoinUnCheckVO> joinUnCheckVOS = new ArrayList<>();
        //获取当前教师参与申报的项目组
        List<ProjectGroup> projectGroups = projectService.selectByUserIdAndProjectStatus(currentUser.getId(), ProjectStatus.DECLARE.getValue());
        projectGroups.forEach(projectGroup -> {
            List<UserProjectGroup> userProjectGroups = userProjectService.selectByProjectAndStatus(projectGroup.getId(), JoinStatus.APPLYING.getValue());
            for (UserProjectGroup userProjectGroup : userProjectGroups) {
                JoinUnCheckVO joinUnCheckVO = new JoinUnCheckVO();
                User user = userService.selectByUserId(userProjectGroup.getUserId());
                joinUnCheckVO.setProjectGroupId(projectGroup.getId());
                joinUnCheckVO.setProjectName(projectGroup.getProjectName());
                joinUnCheckVO.setPersonJudge(userProjectGroup.getPersonalJudge());
                joinUnCheckVO.setTechnicalRole(userProjectGroup.getTechnicalRole());
                joinUnCheckVO.setUserDetailVO(ConvertUtil.convertUserDetailVO(user));
                joinUnCheckVOS.add(joinUnCheckVO);
            }
        });
        return joinUnCheckVOS;
    }

    @Override
    public Result rejectJoin(JoinForm joinForm) {
        UserProjectGroup userProjectGroup = userProjectService.selectByProjectGroupIdAndUserId(joinForm.getProjectGroupId(), joinForm.getUserId());
        if (userProjectGroup == null){
            return Result.error(CodeMsg.USER_NOT_APPLYING);
        }
        if (userProjectGroup.getStatus() == JoinStatus.JOINED.getValue().intValue()){
            return Result.error(CodeMsg.USER_HAD_JOINED_CANT_REJECT);
        }
        userProjectGroup.setStatus(JoinStatus.UN_PASS.getValue());
        if (userProjectService.update(userProjectGroup)) {
            return Result.success();
        }
        return Result.error(CodeMsg.UPDATE_ERROR);
    }

    @Override
    public List<SelectProjectVO> selectByProjectName(String name) {
        List<SelectProjectVO> list = (List<SelectProjectVO>) redisService.getList(ProjectGroupKey.getByFuzzyName, name);
        if (list == null || list.size() == 0){
            list = projectGroupMapper.selectByFuzzyName(name);
            if (list != null){
                redisService.setList(ProjectGroupKey.getByFuzzyName, name, list);
            }
        }
        return list;
    }

}
