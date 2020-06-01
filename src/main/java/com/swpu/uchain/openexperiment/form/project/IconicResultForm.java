package com.swpu.uchain.openexperiment.form.project;

import lombok.Data;

import java.util.Date;

/**
 * @author zty
 * @date 2020/6/1 下午8:55
 * @description: 标志性结果表单
 */
@Data
public class IconicResultForm {

    private Integer value;

    private String provenance;

    private Date issuingTime;

    private String issuingName;
}
