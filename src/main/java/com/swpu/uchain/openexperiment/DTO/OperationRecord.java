package com.swpu.uchain.openexperiment.DTO;

import io.swagger.models.auth.In;
import lombok.Data;

/**
 * @author panghu
 */
@Data
public class OperationRecord {

    /**
     * 所关联的主键
     */
    private Long relatedId;

    /**
     * 操作类型
     */
    private Integer operationType;


    /**
     * 具体操作
     */
    private Integer operationUnit;

    /**
     * 操作者所属的学院
     */
    private Integer operationCollege;

    /**
     * 操作理由
     */
    private String operationReason;

    /**
     * 执行者工号
     */
    private Long operationExecutorId;

    public OperationRecord() {
    }

    public OperationRecord(Long relatedId, Integer operationType, Integer operationUnit, String operationReason, Long operationExecutorId) {
        this.relatedId = relatedId;
        this.operationType = operationType;
        this.operationUnit = operationUnit;
        this.operationReason = operationReason;
        this.operationExecutorId = operationExecutorId;
    }
}
