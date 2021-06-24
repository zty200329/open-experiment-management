package com.swpu.uchain.openexperiment.util;

import com.swpu.uchain.openexperiment.domain.Funds;

import java.util.List;

/**
 * @Author: clf
 * @Date: 19-1-23
 * @Description:
 * 计算工具类
 */
public class CountUtil {

    /**
     * 获取最大申请人数
     * @param fitNum 最大申请人数为适宜人数的1.5倍
     * @return
     */
    public static Integer getMaxApplyNum(Integer fitNum){
        return fitNum / 2 + fitNum;
    }

    /**
     * 获取资金总额
     * @param fundsList
     * @return
     */
    public static Integer countFundsTotalAmount(List<Funds> fundsList){
        int total = 0;
        for (Funds funds : fundsList) {
            total += funds.getAmount();
        }
        return total;
    }
}
