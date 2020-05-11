package com.swpu.uchain.openexperiment.service;

import com.swpu.uchain.openexperiment.form.certificate.ApplyCertificateForm;
import com.swpu.uchain.openexperiment.form.certificate.DeleteCertificateForm;
import com.swpu.uchain.openexperiment.result.Result;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
    Result applyCertificate(ApplyCertificateForm applyCertificate);

    /**
     * 查看我的申请
     * @return
     */
    Result viewMyApplication();

    /**
     * 删除我的申请
     * @param deleteCertificate
     * @return
     */
    Result deleteMyApplication(DeleteCertificateForm deleteCertificate);

    /**
     * 管理员打开申请
     * @return
     */
    Result openApply();

    /**
     * 管理员关闭申领
     * @return
     */
    Result closeApply();

    /**
     * 下载excel
     * @param response
     */
    Result downloadList(HttpServletResponse response);

    /**
     * 清空数据库
     * @return
     */
    Result emptyTheTable();

}
