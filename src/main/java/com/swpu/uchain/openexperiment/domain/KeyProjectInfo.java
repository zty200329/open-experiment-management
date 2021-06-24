package com.swpu.uchain.openexperiment.domain;

import com.swpu.uchain.openexperiment.enums.CollegeType;
import lombok.Data;

/**
 * @author dengg
 *
 * 重点项目状态记录
 */
@Data
public class KeyProjectInfo {

    /**
     * 项目编号
     */
    private Long projectId;

    /**
     * 项目状态
     */
    private Integer status;

    /**
     * 项目所属的学院。冗余字段，方便查询  {@link CollegeType#getValue()}
     */
    private Integer subordinateCollege;

    private Long creatorId;

}
