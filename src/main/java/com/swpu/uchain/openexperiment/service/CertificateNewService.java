package com.swpu.uchain.openexperiment.service;

import com.swpu.uchain.openexperiment.form.certificate.ApplyCertificate;
import com.swpu.uchain.openexperiment.result.Result;

/**
 * @author zty
 * @date 2020/5/9 下午9:38
 * @description:
 */
public interface CertificateNewService {
    /**
     * 申请证书
     * @param applyCertificate
     * @return
     */
    Result applyCertificate(ApplyCertificate applyCertificate);

    /**
     * 查看我的申请
     * @return
     */
    Result viewMyApplication();

    /**
     * 删除我的申请
     * @param id
     * @return
     */
    Result deleteMyApplication(Long[] id);

    /**
     * 管理员打开申请
     * @return
     */
    Result openApply();
}
