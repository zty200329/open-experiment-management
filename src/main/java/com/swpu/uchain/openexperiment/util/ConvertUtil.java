package com.swpu.uchain.openexperiment.util;

import com.swpu.uchain.openexperiment.VO.ApplyGeneralFormInfoVO;
import com.swpu.uchain.openexperiment.VO.ApplyKeyFormInfoVO;
import com.swpu.uchain.openexperiment.VO.UserDetailVO;
import com.swpu.uchain.openexperiment.domain.Acl;
import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.enums.UserType;
import org.springframework.beans.BeanUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-23
 * @Description:
 */
public class ConvertUtil {
    public static List<String> fromAclsTogetUrls(List<Acl> acls){
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
            UserDetailVO userDetailVO = new UserDetailVO();
            BeanUtils.copyProperties(user, userDetailVO);
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
}
