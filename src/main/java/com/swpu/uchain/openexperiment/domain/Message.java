package com.swpu.uchain.openexperiment.domain;

import lombok.Data;

/**
 * @author panghu
 */
@Data
public class Message {

    /**
     * 消息接收者ID
     */
    private Long userId;

    /**
     * 消息内容
     */
    private String title;

}
