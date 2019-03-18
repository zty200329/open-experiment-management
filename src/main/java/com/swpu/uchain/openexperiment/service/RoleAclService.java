package com.swpu.uchain.openexperiment.service;

import com.swpu.uchain.openexperiment.domain.RoleAcl;
import com.swpu.uchain.openexperiment.form.permission.RoleAclForm;
import com.swpu.uchain.openexperiment.result.Result;

/**
 * @Author: clf
 * @Date: 19-1-22
 * @Description:
 * 角色权限管理模块
 */
public interface RoleAclService {

    /**
     * 添加新的RoleAcl
     * @param roleAcl
     * @return
     */
    boolean insertRoleAcl(RoleAcl roleAcl);

    /**
     * 删除某个角色的权限
     * @param roleId
     * @param aclId
     */
    void deleteByRoleIdAclId(Long roleId, Long aclId);

    /**
     * 按角色id和权限id进行查找
     * @param roleId
     * @param aclId
     * @return
     */
    RoleAcl selectByRoleIdAndAclId(Long roleId, Long aclId);

    /**
     * 添加角色的权限
     * @param roleAclForm
     * @return
     */
    Result addRoleAcl(RoleAclForm roleAclForm);
}
