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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
}
