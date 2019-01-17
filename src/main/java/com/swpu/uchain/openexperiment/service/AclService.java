package com.swpu.uchain.openexperiment.service;

import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description:
 * 权限模块
 */
public interface AclService {
    /**
     * 获取当前用户能够访问的
     * @param userId
     * @return
     */
    List<String> getUserAclUrl(Long userId);
}
