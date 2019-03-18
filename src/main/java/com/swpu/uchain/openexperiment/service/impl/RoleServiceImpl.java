package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.dao.AclMapper;
import com.swpu.uchain.openexperiment.dao.RoleMapper;
import com.swpu.uchain.openexperiment.domain.Role;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import com.swpu.uchain.openexperiment.form.permission.RoleForm;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.RoleService;
import com.swpu.uchain.openexperiment.util.ConvertUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-22
 * @Description:
 * 实现角色管理模块
 */
@Service
public class RoleServiceImpl implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private AclMapper aclMapper;
    @Override
    public boolean insert(Role role) {
        return roleMapper.insert(role) == 1;
    }

    @Override
    public boolean update(Role role) {
        return roleMapper.updateByPrimaryKey(role) == 1;
    }

    @Override
    public void delete(Long id) {
        Role role = selectByRoleId(id);
        if (role != null){
            if ("ADMIN".equals(role.getName())) {
                throw new GlobalException(CodeMsg.ADMIN_CANT_DELETE);
            }
            roleMapper.deleteByPrimaryKey(id);
        }
    }

    @Override
    public Result addRole(String roleName) {
        Role role = selectRoleName(roleName);
        if (role != null){
            return Result.error(CodeMsg.ROLE_HAD_EXIST);
        }
        role = new Role();
        role.setName(roleName);
        Date currentDate = new Date();
        role.setCreateTime(currentDate);
        role.setUpdateTime(currentDate);
        if (insert(role)){
            return Result.success();
        }
        return Result.error(CodeMsg.ADD_ERROR);
    }

    @Override
    public Result updateRoleName(RoleForm roleForm) {
        Role role = selectByRoleId(roleForm.getRoleId());
        if (role == null){
            return Result.error(CodeMsg.ROLE_NOT_EXIST);
        }
        if ("ADMIN".equals(role.getName())){
            return Result.error(CodeMsg.ADMIN_CANT_CHANGE);
        }
        role.setName(roleForm.getRoleName());
        role.setUpdateTime(new Date());
        if (update(role)){
            return Result.success();
        }
        return Result.error(CodeMsg.UPDATE_ERROR);
    }

    @Override
    public Result selectAllRole() {
        List<Role> roles = roleMapper.selectAll();
        return Result.success(ConvertUtil.convertRoles(roles, aclMapper));
    }

    @Override
    public Result selectRoleInfo(Long roleId) {
        Role role = selectByRoleId(roleId);
        return Result.success(ConvertUtil.convertOneRoleInfo(role, aclMapper));
    }

    @Override
    public List<Role> getUserRoles(Long userId) {
        return roleMapper.selectByUserId(userId);
    }

    @Override
    public Role selectByRoleId(Long id) {
        return roleMapper.selectByPrimaryKey(id);
    }

    @Override
    public Role selectRoleName(String roleName) {
        return roleMapper.selectByRoleName(roleName);
    }
}
