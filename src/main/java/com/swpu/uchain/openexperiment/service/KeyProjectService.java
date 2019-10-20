package com.swpu.uchain.openexperiment.service;

import com.swpu.uchain.openexperiment.form.project.KeyProjectApplyForm;
import com.swpu.uchain.openexperiment.result.Result;

/**
 * @author dengg
 */
public interface KeyProjectService {

    /**
     * 重点项目申请
     * @param form 申请表单
     * @return
     */
    Result createKeyApply(KeyProjectApplyForm form);

    /**
     * 指导教师获取带审批的重点项目申请表
     * @return
     */
    Result getKeyProjectApplyingList();
}
