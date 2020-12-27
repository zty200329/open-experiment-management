package com.swpu.uchain.openexperiment.service;

import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.enums.UserType;
import com.swpu.uchain.openexperiment.form.user.*;
import com.swpu.uchain.openexperiment.result.Result;
import org.springframework.web.bind.annotation.RequestBody;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description:
 * 用户模块
 */
public interface UserService {
    /**
     * 添加用户
     * @param user
     * @return
     */
    boolean insert(User user);

    /**
     * 更新用户信息
     * @param user
     * @return
     */
    boolean update(User user);

    /**
     * 删除用户
     * @param id
     */
    void delete(Long id);

    /**
     * 登录接口
     * @param clientIp
     * @param loginForm
     * @return
     */
    Result loginFirst(String clientIp, FirstLoginForm loginForm);
    /**
     * 登录接口
     * @param loginForm
     * @return
     */
    Result login(LoginForm loginForm);
    /**
     * 登录接口
     * @param loginForm
     * @return
     */
    Result loginChange(LoginChangeForm loginForm);

    /**
     * 获取用户权限
     * @param getAllPermissions
     * @return
     */
    Result getAllPermissions(GetAllPermissions getAllPermissions);
    /**
     * 发送验证码
     * @param clientIp
     * @return
     * @throws IOException
     */
    String sendVerifyCode(String clientIp) throws IOException;

    /**
     * 检验验证码
     * @param clientIp
     * @param code
     * @return
     */
    boolean checkVerifyCode(String clientIp,String code);



    /**
     * 根据id进行查询
     * @param userId
     * @return
     */
    User selectByUserId(Long userId);

    /**
     * 查询某项目参与的人员
     * @param projectId
     * @return
     */
    List<User> selectProjectJoinedUsers(Long projectId);

    /**
     * 按关键字进行查找用户信息
     * @param keyWord
     * @param isTeacher
     * @return
     */
    List<User> selectByKeyWord(String keyWord, boolean isTeacher);

    /**
     * 创建老师参与项目关系
     * @param userCodes  用户学工号
     * @param projectGroupId 项目组ID
     * @param userType 用户类型
     * @return 方法调用返回结果
     */
    Result createUserJoin(String[] userCodes, Long projectGroupId, UserType userType);

    /**
     * 查询项目组组长
     * @param projectGroupId
     * @return
     */
    User selectGroupLeader(Long projectGroupId);

    /**
     * 更新用户基本信息
     * @param userUpdateForm
     * @return
     */
    Result updateUserInfo(UserUpdateForm userUpdateForm);

    /**
     * 获取当前用户的个人基本信息
     * @return
     */
    Result getMyInfo();

    /**
     * 管理员根据关键字获取用户信息
     * @param keyWord
     * @return
     */
    Result getManageUsersByKeyWord(String keyWord);

    Result getUserInfoByUserId(Long userId);

    Result getInfoByUserId(Long userId);


}
