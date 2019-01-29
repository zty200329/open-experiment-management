package com.swpu.uchain.openexperiment.VO.project;

import com.swpu.uchain.openexperiment.domain.Funds;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-24
 * @Description:
 * 重点项目立项申请表信息展示
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplyKeyFormInfoVO extends ApplyGeneralFormInfoVO{
    /**
     * 经费详情
     */
    private List<Funds> fundsDetails;

}
