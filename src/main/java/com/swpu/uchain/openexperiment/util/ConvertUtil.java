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
import com.swpu.uchain.openexperiment.enums.ExperimentType;
import com.swpu.uchain.openexperiment.mapper.AclMapper;
import com.swpu.uchain.openexperiment.mapper.RoleMapper;
import com.swpu.uchain.openexperiment.domain.Acl;
import com.swpu.uchain.openexperiment.domain.Role;
import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.enums.UserType;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-23
 * @Description: VO转换工具类
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

    public List<String> fromAclsToUrls(List<Acl> acls) {
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

    public static String getStringSuggestGroupType(Integer suggestGroupType) {
        switch (suggestGroupType) {
            case 1:
                return "A";

            case 2:
                return "B";

            case 3:
                return "C";

            case 4:
                return "E";

            case 5:
                return "F";
            case 6:
                return "D";
            default:
                return "";
        }
    }

    public String getTechnicalRole(int type) {
        switch (type) {
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

    public <T> T addUserDetailVO(List<User> users, Class<T> clazz) {
        List<UserDetailVO> guideTeachers = new ArrayList<>();
        List<UserDetailVO> stuMembers = new ArrayList<>();
        for (User user : users) {
            UserDetailVO userDetailVO = convertUserDetailVO(user);
            if (user.getUserType().intValue() == UserType.STUDENT.getValue()) {
                stuMembers.add(userDetailVO);
            } else {
                guideTeachers.add(userDetailVO);
            }
        }
        if (clazz == ApplyKeyFormInfoVO.class) {
            ApplyKeyFormInfoVO applyKeyFormInfoVO = new ApplyKeyFormInfoVO();
            applyKeyFormInfoVO.setStuMembers(stuMembers);
            applyKeyFormInfoVO.setGuideTeachers(guideTeachers);
            return (T) applyKeyFormInfoVO;
        } else {
            ApplyGeneralFormInfoVO applyGeneralFormInfoVO = new ApplyGeneralFormInfoVO();
            applyGeneralFormInfoVO.setStuMembers(stuMembers);
            applyGeneralFormInfoVO.setGuideTeachers(guideTeachers);
            return (T) applyGeneralFormInfoVO;
        }
    }

    public List<AttachmentFileVO> getAttachmentFileVOS(List<AttachmentFileDTO> attachmentFileDTOS) {
        List<AttachmentFileVO> attachmentFileVOS = new ArrayList<>();
        for (AttachmentFileDTO attachmentFileDTO : attachmentFileDTOS) {
            AttachmentFileVO attachmentFileVO = new AttachmentFileVO();
            attachmentFileVO.setFileId(attachmentFileDTO.getFileId());
            BeanUtils.copyProperties(attachmentFileDTO, attachmentFileVO);
            attachmentFileVO.setUploadUser(new UserVO(attachmentFileDTO.getUploadUserId(), attachmentFileDTO.getUserName()));
        }
        return attachmentFileVOS;
    }

    public List<UserManageInfo> convertUsers(List<User> users) {
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

    public RoleInfoVO convertRole(Role role) {
        return convertOneRoleInfo(role);
    }

    public List<RoleInfoVO> convertRoles(List<Role> roles) {
        List<RoleInfoVO> roleList = new ArrayList<>();
        roles.forEach(role -> {
            RoleInfoVO roleInfoVO = convertOneRoleInfo(role);
            roleList.add(roleInfoVO);
        });
        return roleList;
    }

    public RoleInfoVO convertOneRoleInfo(Role role) {
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

    public Long[] parseIds(String str) {
        String[] split = str.split(",");
        Long[] ids = new Long[split.length];
        for (int i = 0; i < split.length; i++) {
            long l = Long.parseLong(split[i]);
            ids[i] = l;
        }
        return ids;
    }

    private static String operationUnitToWord(String operationUnit) {
        switch (operationUnit) {
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

    public static Integer getIntCollege(String strCollege) {
        int result;
        switch (strCollege) {
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

    public static String getStrCollege(int intCollege) {
        String result;
        switch (intCollege) {
            case 1:
                result = "石油与天然气工程学院";
                break;
            case 2:
                result = "地球科学与技术学院";
                break;
            case 3:
                result = "机电工程学院";
                break;
            case 4:
                result = "化学化工学院";
                break;
            case 5:
                result = "材料科学与工程学院";
                break;
            case 6:
                result = "计算机科学学院";
                break;
            case 7:
                result = "电气信息学院";
                break;
            case 8:
                result = "土木工程与建筑学院";
                break;
            case 9:
                result = "理学院";
                break;
            case 10:
                result = "经济管理学院";
                break;
            case 11:
                result = "法学院";
                break;
            case 12:
                result = "外国语学院";
                break;
            case 13:
                result = "体育学院";
                break;
            case 14:
                result = "艺术学院";
                break;
            case 15:
                result = "马克思主义学院";
                break;
            default:
                result = "无";
        }
        return result;
    }

    public static String getStrProjectType(Integer type) {
        if (type == 1) {
            return "重点";
        } else {
            return "普通";
        }
    }

    public static String getMajorNameByNumber(Integer number) {
        switch (number) {
            case 24:
                return "计算机科学与技术";
            case 25:
                return "软件工程";
            case 26:
                return "网络工程";
            case 27:
                return "物联网工程";
            case 28:
                return "数据科学与大数据技术";
            case 29:
                return "网络空间安全";
            case 30:
                return "自动化";
            case 31:
                return "电子信息工程";
            case 32:
                return "电气工程及其自动化";
            case 33:
                return "通信工程";
            default:
                return "未知专业";
        }
    }

    public static String getGradeAndMajorByNumber(String gradeAndMajor) {
        if (gradeAndMajor == null) {
            return "";
        }
        String grade = gradeAndMajor.substring(0, 4);
        Integer major = Integer.valueOf(gradeAndMajor.substring(4));
        return grade + "级" + getMajorNameByNumber(major);
    }

    public static void main(String[] args) {
        System.err.println(getGradeAndMajorByNumber(String.valueOf(201726)));
    }

}
