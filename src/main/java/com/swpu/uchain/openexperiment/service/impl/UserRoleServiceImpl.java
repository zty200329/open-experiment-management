package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.enums.RoleType;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import com.swpu.uchain.openexperiment.mapper.UserRoleMapper;
import com.swpu.uchain.openexperiment.domain.UserRole;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.form.permission.UserRoleForm;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.UserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-22
 * @Description:
 * 实现用户角色管理模块
 */
@Service
public class UserRoleServiceImpl implements UserRoleService {
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Override
    public boolean insert(UserRole userRole) {
        return userRoleMapper.insert(userRole) == 1;
    }

    @Override
    public void deleteByUserIdRoleId(Long userId, Long roleId) {
        if (userRoleMapper.selectByUserId(userId) == null){
            throw new GlobalException(CodeMsg.USER_NO_EXIST);
        }
        userRoleMapper.deleteByUserIdAndRoleId(userId, roleId);
    }

    @Override
    public Result addUserRole(UserRoleForm userRoleForm) {
        UserRole userRole = userRoleMapper.selectByUserIdAndRoleId(userRoleForm.getUserId(), userRoleForm.getRoleId());
        if (userRole != null){
            return Result.error(CodeMsg.USER_ROLE_HAD_EXIST);
        }
        userRole = new UserRole();
        userRole.setUserId(userRoleForm.getUserId());
        userRole.setRoleId(userRoleForm.getRoleId());
        if (insert(userRole)){
            return Result.success();
        }
        return Result.error(CodeMsg.ADD_ERROR);
    }

    @Override
    public List<UserRole> selectUsersByRoleId(Long roleId) {
        return userRoleMapper.selectByRoleId(roleId);
    }

    @Override
    public Result getUserInfoByRole() {
        return Result.success(userRoleMapper.getUserInfoByRole(RoleType.MENTOR.getValue()));
    }
}
