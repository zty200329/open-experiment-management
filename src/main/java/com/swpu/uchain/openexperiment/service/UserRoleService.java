package com.swpu.uchain.openexperiment.service;

import com.swpu.uchain.openexperiment.domain.User;
import com.swpu.uchain.openexperiment.domain.UserRole;
import com.swpu.uchain.openexperiment.enums.RoleType;
import com.swpu.uchain.openexperiment.form.permission.UserRoleForm;
import com.swpu.uchain.openexperiment.result.Result;

import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-22
 * @Description: 用户权限模块
 */
public interface UserRoleService {
    /**
     * 验证是否拥有角色
     *
     * @param user
     * @param roleType
     * @return
     */
    boolean validContainsUserRole(User user, RoleType roleType);

    /**
     * 验证是否拥有该角色
     */
    boolean validContainsUserRole(RoleType roleType);

    /**
     * 插入UserRole
     *
     * @param userRole
     * @return
     */
    boolean insert(UserRole userRole);

    /**
     * 删除用户的某个角色
     *
     * @param userId
     * @param roleId
     */
    Result deleteByUserIdRoleId(Long userId, Integer roleId);

    /**
     * 为用户配置角色
     *
     * @param userRoleForm
     * @return
     */
    Result addUserRole(UserRoleForm userRoleForm);

    /**
     * 查找拥有某一角色的所有用户
     *
     * @param roleId
     * @return
     */
    List<UserRole> selectUsersByRoleId(Long roleId);

    Result getUserInfoByRole();

    Result getCollegeUserInfoByCollege();
}
