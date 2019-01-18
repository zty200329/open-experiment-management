package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.dao.AclMapper;
import com.swpu.uchain.openexperiment.domain.Acl;
import com.swpu.uchain.openexperiment.service.AclService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description:
 * 实现权限管理模块
 */
@Service
public class AclServiceImpl implements AclService {
    @Autowired
    private AclMapper aclMapper;
    @Override
    public List<String> getUserAclUrl(Long userId) {
        List<Acl> acls = aclMapper.selectByUserId(userId);
        List<String> list = new ArrayList<>();
        for (Acl acl : acls) {
            list.add(acl.getUrl());
        }
        return list;
    }
}
