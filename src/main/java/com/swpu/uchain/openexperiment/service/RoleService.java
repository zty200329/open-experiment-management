package com.swpu.uchain.openexperiment.service;

import com.swpu.uchain.openexperiment.domain.Role;
import com.swpu.uchain.openexperiment.form.permission.RoleForm;
import com.swpu.uchain.openexperiment.result.Result;

import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description:
 * 角色模块
 */
public interface RoleService {

    /**
     * 添加角色
     * @param role
     * @return
     */
    boolean insert(Role role);

    /**
     * 更新角色
     * @param role
     * @return
     */
    boolean update(Role role);

    /**
     * 删除role
     * @param roleId
     */
    void delete(Long roleId);

    /**
     * 按id查找角色
     * @param id
     * @return
     */
    Role selectByRoleId(Long id);

    /**
     * 根据角色名进行查找
     * @param roleName
     * @return
     */
    Role selectRoleName(String roleName);

    /**
     * 添加角色
     * @param roleName
     * @return
     */
    Result addRole(String roleName);

    /**
     * 更新角色名
     * @param roleForm
     * @return
     */
    Result updateRoleName(RoleForm roleForm);

    /**
     * 查询所有角色信息,包括当前角所拥有的权限
     * @return
     */
    Result selectAllRole();

    /**
     * 根据角色id获取当前角色和权限的所有信息
     * @param roleId
     * @return
     */
    Result selectRoleInfo(Long roleId);

    /**
     * 根据用户id获取角色内容
     * @param userId
     * @return
     */
    Role getUserRoles(Long userId);

    /**
     * 根据用户id以及权限
     * @param userId
     * @param roleId
     * @return
     */
    Role getUserRoles(Long userId,Long roleId);
}
