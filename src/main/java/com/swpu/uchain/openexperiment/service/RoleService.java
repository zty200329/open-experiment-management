package com.swpu.uchain.openexperiment.service;

import com.swpu.uchain.openexperiment.domain.Role;

import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description:
 * 角色模块
 */
public interface RoleService {
    /**
     * 获取当前用户的所有角色
     * @param userCode
     * @return
     */
    List<Role> getUserRole(String userCode);
}
