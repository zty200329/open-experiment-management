package com.swpu.uchain.openexperiment.util;

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
            List<RoleVO> roles = roleMapper.getRoles(user.getId());
            userManageInfo.setRoles(roles);
            userList.add(userManageInfo);
        });
        return userList;
    }

    public  RoleInfoVO convertRoles(Role role){
            RoleInfoVO roleInfoVO = convertOneRoleInfo(role);
        return roleInfoVO;
    }

    public  RoleInfoVO convertOneRoleInfo(Role role) {
        RoleInfoVO roleInfoVO = new RoleInfoVO();
        BeanUtils.copyProperties(role, roleInfoVO);
        List<Acl> acls = aclMapper.selectByRoleId(role.getId());
        roleInfoVO.setAcls(acls);
        return roleInfoVO;
    }

    public  UserDetailVO convertUserDetailVO(User user) {
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

    public static List<ProjectHistoryInfo> getConvertedProjectHistoryInfo(List<ProjectHistoryInfo> list){
        for (ProjectHistoryInfo info:list
             ) {
            //将具体操作转化成文字
            String operationContent = operationContentToWord(info.getOperationContent());
            info.setOperationContent(operationContent);

            String operationType = operationTypeToWord(info.getOperationType());
            info.setOperationType(operationType);

        }
        return list;
    }

    public static List<MessageVO> projectHistoryInfoListToMessageList(List<ProjectHistoryInfo> list){
        List<MessageVO> voList = new LinkedList<>();
        for (ProjectHistoryInfo info:list
             ) {
            MessageVO messageVO = new MessageVO();
            messageVO.setSendTime(info.getOperationTime());
            messageVO.setReadStatus(info.getReadStatus());
            messageVO.setContent(info.getReason());
            messageVO.setId(info.getId());
            //将具体操作转化成文字
            String operationContent =  operationContentToWord(info.getOperationContent());


            String operationType = operationTypeToWord(info.getOperationType());
            messageVO.setTitle("项目编号为"+info.getProjectId()+" 的项目"+operationType+" : "+operationContent);
            voList.add(messageVO);
        }
        return voList;
    }

    private static String operationTypeToWord(String operationType){
        switch (operationType){
            case "1":
                operationType = OperationType.PROJECT_OPERATION_TYPE1.getTips();
                break;
            case "2":
                operationType = OperationType.PROJECT_OPERATION_TYPE2.getTips();
                break;
            case "3":
                operationType = OperationType.PROJECT_OPERATION_TYPE3.getTips();
                break;
            case "11":
                operationType = OperationType.PROJECT_MODIFY_TYPE1.getTips();
                break;
            case "21":
                operationType = OperationType.PROJECT_REPORT_TYPE1.getTips();
                break;
            case "22":
                operationType = OperationType.PROJECT_REPORT_TYPE2.getTips();
                break;
            default:
        }
        return operationType;
    }

    private static String operationContentToWord(String operationContent){
        switch (operationContent){
            case "1":
                operationContent = "通过";
                break;
            case "2":
                operationContent = "不通过";
                break;
            default:
        }
        return operationContent;
    }


}
