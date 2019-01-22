package com.swpu.uchain.openexperiment.service.impl;

import com.swpu.uchain.openexperiment.dao.AclMapper;
import com.swpu.uchain.openexperiment.dao.RoleAclMapper;
import com.swpu.uchain.openexperiment.domain.Acl;
import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.form.permission.AclUpdateForm;
import com.swpu.uchain.openexperiment.redis.AclKey;
import com.swpu.uchain.openexperiment.redis.RedisService;
import com.swpu.uchain.openexperiment.result.Result;
import com.swpu.uchain.openexperiment.service.AclService;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
public class AclServiceImpl implements AclService {
    @Autowired
    private AclMapper aclMapper;
    @Autowired
    private RoleAclMapper roleAclMapper;
    @Autowired
    private RedisService redisService;

    @Override
    public boolean insert(Acl acl) {
        return aclMapper.insert(acl) == 1;
    }

    @Override
    public boolean update(Acl acl) {
        return aclMapper.updateByPrimaryKey(acl) == 1;
    }

    @Override
    public List<String> getUserAclUrl(Long userId) {
        List<Acl> acls = redisService.get(AclKey.getByUserId, userId + "", List.class);
        List<String> list = new ArrayList<>();
        if (acls == null){
            acls = aclMapper.selectByUserId(userId);
            if (acls != null){
                redisService.set(AclKey.getByUserId, userId + "", acls);
            }
        }
        for (Acl acl : acls) {
            list.add(acl.getUrl());
        }
        return list;
    }

    @Override
    public Acl selectByUrl(String url) {
        return aclMapper.selectByUrl(url);
    }

    @Override
    public Result updateAcl(AclUpdateForm aclUpdateForm) {
        Acl acl = aclMapper.selectByPrimaryKey(aclUpdateForm.getAclId());
        if (acl == null) {
            return Result.error(CodeMsg.ACL_NOT_EXIST);
        }
        acl.setDescription(aclUpdateForm.getDescription());
        if (update(acl)) {
            return Result.success();
        }
        return Result.error(CodeMsg.UPDATE_ERROR);
    }

    @Override
    public Result selectByRandom(String info) {
        return Result.success(aclMapper.selectByRandom(info));
    }

    @Override
    public Result selectAll() {
        return Result.success(aclMapper.selectAll());
    }

}
