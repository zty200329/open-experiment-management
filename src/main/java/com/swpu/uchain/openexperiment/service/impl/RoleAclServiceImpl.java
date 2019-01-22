package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.dao.RoleAclMapper;
import com.swpu.uchain.openexperiment.domain.RoleAcl;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.form.permission.RoleAclForm;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.RoleAclService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
    
    @Override
    public boolean insertRoleAcl(RoleAcl roleAcl) {
        return roleAclMapper.insert(roleAcl) == 1;
    }

    @Override
    public void delete(Long id) {
        roleAclMapper.deleteByPrimaryKey(id);
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
