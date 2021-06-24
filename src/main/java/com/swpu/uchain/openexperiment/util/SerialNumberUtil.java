package com.swpu.uchain.openexperiment.util;

import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.enums.CollegeType;
import com.swpu.uchain.openexperiment.enums.ProjectType;
import com.swpu.uchain.openexperiment.exception.GlobalException;

import java.util.Calendar;

/**
 * @author zty200329
 */
public class SerialNumberUtil {

    /**
     * @param college  学院类型
     * @param projectType  项目类型，重点，普通
     * @param maxSerialNumber 最大编号   --防止删除后出现问题
     * @return 完整项目编号
     */
    public synchronized static String getSerialNumberOfProject(Integer college, Integer projectType,String maxSerialNumber){

        if (projectType == null){
            throw new GlobalException(CodeMsg.PROJECT_TYPE_NULL_ERROR);
        }

        if (college == null) {
            throw new GlobalException(CodeMsg.COLLEGE_TYPE_NULL_ERROR);
        }

        String serialNumber;
//        Calendar calendar = Calendar.getInstance();
//        int year = calendar.get(Calendar.YEAR);

        int year = 2020;

        String projectTypeValue;
        //如果重点项目
        if (projectType.equals(ProjectType.KEY.getValue())) {
            projectTypeValue = "KSZ";
        //如果是普通项目
        }else {
            projectTypeValue = "KSP";
        }
        //有隐藏bug
//        if(maxSerialNumber != null){
//            int lastYear = Integer.parseInt(maxSerialNumber.substring(0,4));
//        }
        int index = 1;
        //验证非空
        if (maxSerialNumber != null && !"".equals(maxSerialNumber)){
            //如果年份不一样又要重1来
//            if(year==lastYear) {
                index = Integer.parseInt(maxSerialNumber.substring(maxSerialNumber.length() - 3)) + 1;
//            }
        }
        serialNumber = year+projectTypeValue+String.format("%02d", college)+String.format("%03d", index);
        return serialNumber;
    }

    public static void main(String[] args) {
        String number = getSerialNumberOfProject(CollegeType.ELECTRICAL_INFORMATION_COLLEGE.getValue(),ProjectType.GENERAL.getValue(),"2020KSP06009");
        System.err.println(number);
    }
}
