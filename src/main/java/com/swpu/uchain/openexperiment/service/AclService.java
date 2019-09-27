package com.swpu.uchain.openexperiment.service;

import com.swpu.uchain.openexperiment.domain.Acl;
import com.swpu.uchain.openexperiment.form.permission.AclUpdateForm;
import com.swpu.uchain.openexperiment.result.Result;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-17
 * @Description:
 * 权限模块
 */
public interface AclService {

    /**
     * 封装dao插入
     * @param acl
     * @return
     */
    boolean insert(Acl acl);
    /**
     * 更新权限信息
     * @param acl
     * @return
     */
    boolean update(Acl acl);

    /**
     * 获取当前用户能够访问的
     * @param userId
     * @return
     */
    List<String> getUserAclUrl(Long userId);

    /**
     * 按url进行查找
     * @param url
     * @return
     */
    Acl selectByUrl(String url);

    /**
     * 更新acl
     * @param aclUpdateForm
     * @return:
     */
    Result updateAcl(AclUpdateForm aclUpdateForm);

    /**
     * 任意查找
     * @param info
     * @return
     */
    Result selectByRandom(@Param("info") String info);

    /**
     * 查找所有
     * @return
     */
    Result selectAll();
}
