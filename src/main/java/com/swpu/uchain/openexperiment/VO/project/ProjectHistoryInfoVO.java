package com.swpu.uchain.openexperiment.VO.project;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

/**
 * @author panghu
 */
@Data
public class ProjectHistoryInfoVO {

    /**
     * 操作类型
     */
    private String operationType;

    /**
     * 理由
     */
    private String reason;

    /**
     * 时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date operationTime;

    /**
     * 具体的操作
     */
    private String operationContent;

}
