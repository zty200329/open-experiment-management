package com.swpu.uchain.openexperiment.service;

import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.enums.UserType;
import com.swpu.uchain.openexperiment.form.LoginForm;
import com.swpu.uchain.openexperiment.result.Result;

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
    Result login(String clientIp, LoginForm loginForm);

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
     * 获取当前用户
     * @return
     */
    User getCurrentUser();

    /**
     * 根据编号查询用户
     * @param userCode
     * @return
     */
    User selectByUserCode(String userCode);

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
     * @return
     */
    List<User> selectByKeyWord(String keyWord);

    /**
     * 创建老师参与项目关系
     * @param userCodes
     * @param projectGroupId
     * @return
     */
    Result createUserJoin(String[] userCodes, Long projectGroupId, UserType userType);

}
