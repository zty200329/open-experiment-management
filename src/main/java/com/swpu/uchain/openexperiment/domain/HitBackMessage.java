package com.swpu.uchain.openexperiment.domain;

import lombok.Data;

import java.io.Serializable;
import java.util.Date;

@Data
public class HitBackMessage implements Serializable {
    private Long id;

    /**
     * 接收者id
     */
    private Long receiveUserId;

    /**
     * 发送人
     */
    private String sender;

    /**
     * 内容
     */
    private String content;

    /**
     * 发送时间
     */
    private Date sendTime;

    /**
     * 是否已读
     */
    private Boolean isRead;


}