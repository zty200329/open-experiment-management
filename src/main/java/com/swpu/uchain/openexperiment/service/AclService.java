package com.swpu.uchain.openexperiment.service;

import com.swpu.uchain.openexperiment.domain.Acl;
import com.swpu.uchain.openexperiment.result.Result;

import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description: 权限模块
 */
public interface AclService {
    /**
     * 获取当前用户能够访问的
     *
     * @param userId
     * @return
     */
    List<String> getUserAclUrl(Long userId);

    /**
     * 添加Url
     *
     * @param acl
     * @return:
     */
    boolean insert(Acl acl);

    /**
     * @param
     * @return:
     */
    boolean update(Acl acl);

    /**
     * 删除
     *
     * @param
     * @return:
     */
    boolean delete(Long id);

    /**
     * 查找一个acl
     *
     * @param name
     * @return:
     */
    Acl selectByName(String name);

    /**
     * 添加acl
     *
     * @param acl
     * @return:
     */
    Result insertAcl(Acl acl);

    /**
     * 更新acl
     *
     * @param acl
     * @return:
     */
    Result updateAcl(Acl acl);

    /**
     * 删除acl
     *
     * @param id
     * @return:
     */
    Result deleteAcl(Long id);
}
