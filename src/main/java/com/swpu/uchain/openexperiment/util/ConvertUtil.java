package com.swpu.uchain.openexperiment.util;

import com.alibaba.druid.sql.visitor.functions.Char;
import com.swpu.uchain.openexperiment.DTO.AttachmentFileDTO;
import com.swpu.uchain.openexperiment.VO.MessageVO;
import com.swpu.uchain.openexperiment.VO.file.AttachmentFileVO;
import com.swpu.uchain.openexperiment.VO.permission.RoleInfoVO;
import com.swpu.uchain.openexperiment.VO.permission.RoleVO;
import com.swpu.uchain.openexperiment.VO.project.ApplyGeneralFormInfoVO;
import com.swpu.uchain.openexperiment.VO.project.ApplyKeyFormInfoVO;
import com.swpu.uchain.openexperiment.DTO.ProjectHistoryInfo;
import com.swpu.uchain.openexperiment.VO.user.UserDetailVO;
import com.swpu.uchain.openexperiment.VO.user.UserManageInfo;
import com.swpu.uchain.openexperiment.VO.user.UserVO;
import com.swpu.uchain.openexperiment.dao.AclMapper;
import com.swpu.uchain.openexperiment.dao.RoleMapper;
import com.swpu.uchain.openexperiment.domain.Acl;
import com.swpu.uchain.openexperiment.domain.Message;
import com.swpu.uchain.openexperiment.domain.Role;
import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.enums.OperationType;
import com.swpu.uchain.openexperiment.enums.UserType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-23
 * @Description:
 * VO转换工具类
 */
@Service
public class ConvertUtil {

    private AclMapper aclMapper;

    private RoleMapper roleMapper;

    @Autowired
    public ConvertUtil(AclMapper aclMapper, RoleMapper roleMapper) {
        this.aclMapper = aclMapper;
        this.roleMapper = roleMapper;
    }

    public  List<String> fromAclsToUrls(List<Acl> acls){
        List<String> urls = new ArrayList<>();
        for (Acl acl : acls) {
            urls.add(acl.getUrl());
        }
        return urls;
    }

    public static String getStrMemberRole(Integer memberRole) {
        switch (memberRole) {
            case 1:
                return "指导教师";
            case 2:
                return "项目组组长";
            case 3:
                return "普通成员";
            default:
                return "未知";
        }
    }

    public static String getStrExperimentType(Integer experimentType) {
        switch (experimentType) {
            case 1:
                return "科研";
            case 2:
                return "科技活动";
            case 3:
                return "自选课题";
            case 4:
                return "计算机应用";
            case 5:
                return "人文素质";
            default:
                return "未知";
        }
    }

    public static Character getCharsuggestGroupType(Integer suggestGroupType){
        switch (suggestGroupType){
            case 1:
                return  'A';

            case 2:
                return  'B';

            case 3:
                return  'C';

            case 4:
                return  'D';

            case 5:
                return  'E';

            case 6:
                return  'F';
            default:
                return null;
        }
    }

    public  String getTechnicalRole(int type){
        switch (type){
            case 1:
                return UserType.STUDENT.getMessage();
            case 2:
                return UserType.LECTURER.getMessage();
            case 3:
                return UserType.PROFESS.getMessage();
            case 4:
                return UserType.ASSOCIATE_PROFESSOR.getMessage();
            default:
                return null;
        }
    }

    public  <T> T addUserDetailVO(List<User> users,Class<T> clazz){
        List<UserDetailVO> guideTeachers = new ArrayList<>();
        List<UserDetailVO> stuMembers = new ArrayList<>();
        for (User user : users) {
            UserDetailVO userDetailVO = convertUserDetailVO(user);
            if (user.getUserType().intValue() == UserType.STUDENT.getValue()){
                stuMembers.add(userDetailVO);
            }else {
                guideTeachers.add(userDetailVO);
            }
        }
        if (clazz == ApplyKeyFormInfoVO.class){
            ApplyKeyFormInfoVO applyKeyFormInfoVO = new ApplyKeyFormInfoVO();
            applyKeyFormInfoVO.setStuMembers(stuMembers);
            applyKeyFormInfoVO.setGuideTeachers(guideTeachers);
            return (T) applyKeyFormInfoVO;
        }else {
            ApplyGeneralFormInfoVO applyGeneralFormInfoVO = new ApplyGeneralFormInfoVO();
            applyGeneralFormInfoVO.setStuMembers(stuMembers);
            applyGeneralFormInfoVO.setGuideTeachers(guideTeachers);
            return (T) applyGeneralFormInfoVO;
        }
    }

    public  List<AttachmentFileVO> getAttachmentFileVOS(List<AttachmentFileDTO> attachmentFileDTOS) {
        List<AttachmentFileVO> attachmentFileVOS = new ArrayList<>();
        for (AttachmentFileDTO attachmentFileDTO : attachmentFileDTOS) {
            AttachmentFileVO attachmentFileVO = new AttachmentFileVO();
            attachmentFileVO.setFileId(attachmentFileDTO.getFileId());
            BeanUtils.copyProperties(attachmentFileDTO, attachmentFileVO);
            attachmentFileVO.setUploadUser(new UserVO(attachmentFileDTO.getUploadUserId(), attachmentFileDTO.getUserName()));
        }
        return attachmentFileVOS;
    }

    public  List<UserManageInfo> convertUsers(List<User> users){
        List<UserManageInfo> userList = new ArrayList<>();
        users.forEach(user -> {
            UserManageInfo userManageInfo = new UserManageInfo();
            BeanUtils.copyProperties(user, userManageInfo);
            List<RoleVO> roles = roleMapper.getRoles(Long.valueOf(user.getCode()));
            userManageInfo.setRoles(roles);
            userList.add(userManageInfo);
        });
        return userList;
    }

    public  RoleInfoVO convertRole(Role role){
        return convertOneRoleInfo(role);
    }

    public  List<RoleInfoVO> convertRoles(List<Role> roles){
        List<RoleInfoVO> roleList = new ArrayList<>();
        roles.forEach(role -> {
            RoleInfoVO roleInfoVO = convertOneRoleInfo(role);
            roleList.add(roleInfoVO);
        });
        return roleList;
    }

    public  RoleInfoVO convertOneRoleInfo(Role role) {
        RoleInfoVO roleInfoVO = new RoleInfoVO();
        BeanUtils.copyProperties(role, roleInfoVO);
        List<Acl> acls = aclMapper.selectByRoleId(role.getId());
        roleInfoVO.setAcls(acls);
        return roleInfoVO;
    }

    public UserDetailVO convertUserDetailVO(User user) {
        UserDetailVO userDetailVO = new UserDetailVO();
        BeanUtils.copyProperties(user, userDetailVO);
        return userDetailVO;
    }

    public  Long[] parseIds(String str){
        String[] split = str.split(",");
        Long[] ids = new Long[split.length];
        for (int i = 0; i < split.length; i++) {
            long l = Long.parseLong(split[i]);
            ids[i] = l;
        }
        return ids;
    }
//
//    public static List<ProjectHistoryInfo> getConvertedProjectHistoryInfo(List<ProjectHistoryInfo> list){
//        for (ProjectHistoryInfo info:list
//             ) {
//            //将具体操作转化成文字
//            String operationUnit = operationUnitToWord(info.getOperationContent());
//            info.setOperationContent(operationUnit);
//
//            String operationType = operationTypeToWord(info.getOperationType());
//            info.setOperationType(operationType);
//
//        }
//        return list;
//    }

//    public static List<MessageVO> projectHistoryInfoListToMessageList(List<ProjectHistoryInfo> list){
//        List<MessageVO> voList = new LinkedList<>();
//        for (ProjectHistoryInfo info:list
//             ) {
//            MessageVO messageVO = new MessageVO();
//            messageVO.setSendTime(info.getOperationTime());
//            messageVO.setReadStatus(info.getReadStatus());
//            messageVO.setContent(info.getReason());
//            messageVO.setId(info.getId());
//            //将具体操作转化成文字
//            String operationUnit =  operationUnitToWord(info.getOperationContent());
//
//
//            String operationType = operationTypeToWord(info.getOperationType());
//            messageVO.setTitle("项目编号为"+info.getProjectId()+" 的项目"+operationType+" : "+operationUnit);
//            voList.add(messageVO);
//        }
//        return voList;
//    }
//
//    private static String operationTypeToWord(String operationType){
//        switch (operationType){
//            case "1":
//                operationType = OperationType.PROJECT_OPERATION_TYPE1.getTips();
//                break;
//            case "2":
//                operationType = OperationType.PROJECT_OPERATION_TYPE2.getTips();
//                break;
//            case "3":
//                operationType = OperationType.PROJECT_OPERATION_TYPE3.getTips();
//                break;
//            case "11":
//                operationType = OperationType.PROJECT_MODIFY_TYPE1.getTips();
//                break;
//            case "21":
//                operationType = OperationType.PROJECT_REPORT_TYPE1.getTips();
//                break;
//            case "22":
//                operationType = OperationType.PROJECT_REPORT_TYPE2.getTips();
//                break;
//            default:
//        }
//        return operationType;
//    }

    private static String operationUnitToWord(String operationUnit){
        switch (operationUnit){
            case "1":
                operationUnit = "通过";
                break;
            case "2":
                operationUnit = "不通过";
                break;
            default:
        }
        return operationUnit;
    }

    public static Integer getIntCollege(String strCollege){
        int result;
        switch (strCollege){
            case "马克思主义学院":
                result = 1;
                break;
            case "艺术学院":
                result = 2;
                break;
            case "化学化工学院":
                result = 3;
                break;
            case "地球科学与技术学院":
                result = 4;
                break;
            case "石油与天然气工程学院":
                result = 5;
                break;
            case "电气信息学院":
                result = 6;
                break;
            case "经济管理学院":
                result = 7;
                break;
            case "体育学院":
                result = 8;
                break;
            case "机电工程学院":
                result = 9;
                break;
            case "材料科学与工程学院":
                result = 10;
                break;
            case "理学院":
                result = 11;
                break;
            case "土木工程与建筑学院":
                result = 12;
                break;
            case "法学院":
                result = 13;
                break;
            case "外国语学院":
                result = 14;
                break;
            case "计算机科学学院":
                result = 15;
                break;
            default:
                result = -1;
        }
        return result;
    }

    public static String getStrCollege(int intCollege){
        String result;
        switch (intCollege){
            case 1:
                result = "马克思主义学院";
                break;
            case 2:
                result = "艺术学院";
                break;
            case 3:
                result = "化学化工学院";
                break;
            case 4:
                result = "地球科学与技术学院";
                break;
            case 5:
                result = "石油与天然气工程学院";
                break;
            case 6:
                result = "电气信息学院";
                break;
            case 7:
                result = "经济管理学院";
                break;
            case 8:
                result = "体育学院";
                break;
            case 9:
                result = "机电工程学院";
                break;
            case 10:
                result = "材料科学与工程学院";
                break;
            case 11:
                result = "理学院";
                break;
            case 12:
                result = "土木工程与建筑学院";
                break;
            case 13:
                result = "法学院";
                break;
            case 14:
                result = "外国语学院";
                break;
            case 15:
                result = "计算机科学学院";
                break;
            default:
                result = "无";
        }
        return result;
    }


}
