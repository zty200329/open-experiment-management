package com.swpu.uchain.openexperiment.util;

import com.swpu.uchain.openexperiment.enums.CodeMsg;
import com.swpu.uchain.openexperiment.enums.CollegeType;
import com.swpu.uchain.openexperiment.enums.ProjectType;
import com.swpu.uchain.openexperiment.exception.GlobalException;
import io.swagger.models.auth.In;

import java.util.Calendar;

/**
 * @author dengg
 */
public class SerialNumberUtil {

    /**
     * @param college  学院类型
     * @param projectType  项目类型，重点，普通
     * @param index 该院的第几个项目
     * @return 完整项目编号
     */
    public static String getSerialNumberOfProject(CollegeType college, ProjectType projectType,Integer index){

        if (projectType == null){
            throw new GlobalException(CodeMsg.PROJECT_TYPE_NULL_ERROR);
        }

        if (college == null) {
            throw new GlobalException(CodeMsg.COLLEGE_TYPE_NULL_ERROR);
        }

        String serialNumber = null;
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);

        String projectTypeValue;
        //如果重点项目
        if (projectType == ProjectType.KEY) {
            projectTypeValue = "KSZ";
        //如果是普通项目
        }else {
            projectTypeValue = "KSP";
        }
        serialNumber = year+projectTypeValue+String.format("%02d", college.getValue())+String.format("%03d", index);
        return serialNumber;
    }

    public static void main(String[] args) {
        String number = getSerialNumberOfProject(CollegeType.MARXISM_COLLEGE,ProjectType.KEY,1);
        System.err.println(number);
    }

}
