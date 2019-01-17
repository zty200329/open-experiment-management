package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.service.AclService;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description:
 * 实现权限管理模块
 */
@Service
public class AclServiceImpl implements AclService {
    @Override
    public List<String> getUserAclUrl(Long userId) {
        //TODO
        return null;
    }
}
