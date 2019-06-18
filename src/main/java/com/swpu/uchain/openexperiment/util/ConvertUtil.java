package com.swpu.uchain.openexperiment.util;

import com.swpu.uchain.openexperiment.DTO.AttachmentFileDTO;
import com.swpu.uchain.openexperiment.VO.file.AttachmentFileVO;
import com.swpu.uchain.openexperiment.VO.permission.RoleInfoVO;
import com.swpu.uchain.openexperiment.VO.permission.RoleVO;
import com.swpu.uchain.openexperiment.VO.project.ApplyGeneralFormInfoVO;
import com.swpu.uchain.openexperiment.VO.project.ApplyKeyFormInfoVO;
import com.swpu.uchain.openexperiment.VO.user.UserDetailVO;
import com.swpu.uchain.openexperiment.VO.user.UserManageInfo;
import com.swpu.uchain.openexperiment.VO.user.UserVO;
import com.swpu.uchain.openexperiment.dao.AclMapper;
import com.swpu.uchain.openexperiment.dao.RoleMapper;
import com.swpu.uchain.openexperiment.domain.Acl;
import com.swpu.uchain.openexperiment.domain.Role;
import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.enums.UserType;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-23
 * @Description:
 * VO转换工具类
 */
public class ConvertUtil {
    public static List<String> fromAclsToUrls(List<Acl> acls){
        List<String> urls = new ArrayList<>();
        for (Acl acl : acls) {
            urls.add(acl.getUrl());
        }
        return urls;
    }

    public static String getTechnicalRole(int type){
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

    public static <T> T addUserDetailVO(List<User> users,Class<T> clazz){
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

    public static List<AttachmentFileVO> getAttachmentFileVOS(List<AttachmentFileDTO> attachmentFileDTOS) {
        List<AttachmentFileVO> attachmentFileVOS = new ArrayList<>();
        for (AttachmentFileDTO attachmentFileDTO : attachmentFileDTOS) {
            AttachmentFileVO attachmentFileVO = new AttachmentFileVO();
            attachmentFileVO.setFileId(attachmentFileDTO.getFileId());
            BeanUtils.copyProperties(attachmentFileDTO, attachmentFileVO);
            attachmentFileVO.setUploadUser(new UserVO(attachmentFileDTO.getUploadUserId(), attachmentFileDTO.getUserName()));
        }
        return attachmentFileVOS;
    }

    public static List<UserManageInfo> convertUsers(List<User> users, RoleMapper roleMapper){
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

    public static List<RoleInfoVO> convertRoles(List<Role> roles, AclMapper aclMapper){
        List<RoleInfoVO> roleList = new ArrayList<>();
        roles.forEach(role -> {
            RoleInfoVO roleInfoVO = convertOneRoleInfo(role, aclMapper);
            roleList.add(roleInfoVO);
        });
        return roleList;
    }

    public static RoleInfoVO convertOneRoleInfo(Role role, AclMapper aclMapper) {
        RoleInfoVO roleInfoVO = new RoleInfoVO();
        BeanUtils.copyProperties(role, roleInfoVO);
        List<Acl> acls = aclMapper.selectByRoleId(role.getId());
        roleInfoVO.setAcls(acls);
        return roleInfoVO;
    }

    public static UserDetailVO convertUserDetailVO(User user) {
        UserDetailVO userDetailVO = new UserDetailVO();
        BeanUtils.copyProperties(user, userDetailVO);
        return userDetailVO;
    }

    public static Long[] parseIds(String str){
        String[] split = str.split(",");
        Long[] ids = new Long[split.length];
        for (int i = 0; i < split.length; i++) {
            long l = Long.parseLong(split[i]);
            ids[i] = l;
        }
        return ids;
    }
}
