package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.dao.AclMapper;
import com.swpu.uchain.openexperiment.domain.Acl;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.AclService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description: 实现权限管理模块
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

    @Override
    public boolean insert(Acl acl) {
        return (aclMapper.insert(acl) == 1);
    }

    @Override
    public boolean update(Acl acl) {
        return (aclMapper.updateByPrimaryKey(acl) == 1);
    }

    @Override
    public boolean delete(Long id) {
        return (aclMapper.deleteByPrimaryKey(id) == 1);
    }

    @Override
    public Acl selectByName(String name) {
        return aclMapper.selectByName(name);
    }

    @Override
    public Result insertAcl(Acl acl) {
        if (selectByName(acl.getName()) != null) {
            return Result.error(CodeMsg.ACL_EXIST);
        }
        if (insert(acl)) {
            return Result.success(acl);
        }
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @Override
    public Result updateAcl(Acl acl) {
        if (selectByName(acl.getName()) == null) {
            return Result.error(CodeMsg.ACL_NOT_EXIST);
        }
        if (update(acl)) {
            return Result.success(acl);
        }
        return Result.error(CodeMsg.SERVER_ERROR);
    }

    @Override
    public Result deleteAcl(Long id) {
        if (aclMapper.selectByPrimaryKey(id) == null) {
            return Result.error(CodeMsg.ACL_NOT_EXIST);
        }
        if (delete(id)) {
            return Result.success();
        }
        return Result.error(CodeMsg.SERVER_ERROR);
    }
}
