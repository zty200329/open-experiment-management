package com.swpu.uchain.openexperiment.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import java.util.Date;

/**
 * @author panghu
 */
@Data
public class ProjectHistoryInfo {

    /**
     * 主键
     */
    private Long id;

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


    /**
     * 阅读状态 Y已读 N 未读
     */
    private String readStatus;

    /**
     * 执行者工号
     */
    @JsonIgnore
    private Long operationExecutorId;

    /**
     * 项目编号
     */
    private String projectId;

}
