package com.swpu.uchain.openexperiment.util;

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
}
