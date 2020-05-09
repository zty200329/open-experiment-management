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
}
