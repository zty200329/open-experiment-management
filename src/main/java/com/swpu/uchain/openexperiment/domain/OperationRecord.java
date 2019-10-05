package com.swpu.uchain.openexperiment.domain;

import lombok.Data;

import java.util.Date;

/**
 * @author panghu
 */
@Data
public class OperationRecord {

    /**
     * 主键
     */
    private Long id;

    /**
     * 所关联的主键
     */
    private Long relatedId;

    /**
     * 操作类型
     */
    private String operationType;


    /**
     * 具体操作
     */
    private String operationContent;

    /**
     * 操作理由
     */
    private String operationReason;

    /**
     * 执行时间
     */
    private Date operationTime;

    /**
     * 执行者ID
     */
    private Long operationExecutorId;
}
