package com.swpu.uchain.openexperiment.VO.project;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author zty
 * @date 2020/6/2 下午2:49
 * @description:
 */
@Data
public class ProjectOutcomeVO {
    private Long id;

    private Integer value;

    private String provenance;

    private Date issuingTime;

    private String issuingName;
}
