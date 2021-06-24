package com.swpu.uchain.openexperiment.VO.project;

import lombok.Data;

/**
 * @author zty
 * @date 2020/5/22 下午2:16
 * @description:
 */
@Data
public class SelectByKeywordProjectVO {
    private Long id;
    private String serialNumber;
    private String projectName;
    private Integer projectType;
    private Integer subordinateCollege;
}
