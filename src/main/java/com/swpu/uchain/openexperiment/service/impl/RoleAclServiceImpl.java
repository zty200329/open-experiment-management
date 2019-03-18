package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.dao.RoleAclMapper;
import com.swpu.uchain.openexperiment.domain.RoleAcl;
import com.swpu.uchain.openexperiment.domain.UserRole;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.form.permission.RoleAclForm;
import com.swpu.uchain.openexperiment.redis.RedisService;
import com.swpu.uchain.openexperiment.redis.key.AclKey;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.RoleAclService;
import com.swpu.uchain.openexperiment.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-22
 * @Description:
 * 实现角色权限管理模块
 */
@Service
public class RoleAclServiceImpl implements RoleAclService {
    @Autowired
    private RoleAclMapper roleAclMapper;
    @Autowired
    private RedisService redisService;
    @Autowired
    private UserRoleService userRoleService;
    
    @Override
    public boolean insertRoleAcl(RoleAcl roleAcl) {
        if (roleAclMapper.insert(roleAcl) == 1){
            //获取拥有当前角色的所有用户id
            List<UserRole> userRoles = userRoleService.selectUsersByRoleId(roleAcl.getRoleId());
            for (UserRole userRole : userRoles) {
                //删除对应的变更后用户的权限缓存
                redisService.delete(AclKey.getUrlsByUserId, userRole.getUserId() + "");
            }
            return true;
        }
        return false;
    }

    @Override
    public void deleteByRoleIdAclId(Long roleId, Long aclId) {
        roleAclMapper.deleteRoleIdAndAclId(roleId, aclId);
    }

    @Override
    public RoleAcl selectByRoleIdAndAclId(Long roleId, Long aclId) {
        return roleAclMapper.selectByRoleIdAndAclId(roleId, aclId);
    }

    @Override
    public Result addRoleAcl(RoleAclForm roleAclForm) {
        RoleAcl roleAcl = selectByRoleIdAndAclId(roleAclForm.getRoleId(), roleAclForm.getAclId());
        if (roleAcl != null){
            return Result.error(CodeMsg.ROLE_ACL_HAD_EXIST);
        }
        roleAcl = new RoleAcl();
        roleAcl.setRoleId(roleAclForm.getRoleId());
        roleAcl.setAclId(roleAclForm.getAclId());
        if (insertRoleAcl(roleAcl)){
            return Result.success();
        }
        return Result.error(CodeMsg.ADD_ERROR);
    }

}
