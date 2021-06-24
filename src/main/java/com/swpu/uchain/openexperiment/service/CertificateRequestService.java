package com.swpu.uchain.openexperiment.service;

import com.swpu.uchain.openexperiment.domain.Certificate;
import com.swpu.uchain.openexperiment.result.Result;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @Author zty
 * @Date 2020/2/28 下午2:45
 * @Description: 证书申领模块
 */
public interface CertificateRequestService {
    /**
     * 判断接口是否打开
     * @return
     */
    Result judgeInterface();

    /**
     * 将获取到满足的转存
     * @param year
     * @return
     */
    Result getAllList(Integer year);

    /**
     * 关闭申领接口
     * @return
     */
    Result closeOpen();

    /**
     * 获取自己能申请证书的项目列表
     * @return
     */
    Result getOwnCertificate();

    Result choseCertificate(Integer[] primaryKey);

    /**
     * 查出某年份的申请名单
     * @param year
     * @return
     */
    Result getFinalList(Integer year);

    void downloadList(Integer year , HttpServletResponse response);
}
