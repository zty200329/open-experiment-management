package com.swpu.uchain.openexperiment.DTO;

import lombok.Data;

/**
 * @author panghu
 */
@Data
public class OperationRecordDTO {

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
     * 执行者工号
     */
    private Long operationExecutorId;

    public OperationRecordDTO() {
    }

    public OperationRecordDTO(Long relatedId, String operationType, String operationContent, String operationReason) {
        this.relatedId = relatedId;
        this.operationType = operationType;
        this.operationContent = operationContent;
        this.operationReason = operationReason;
    }
}
